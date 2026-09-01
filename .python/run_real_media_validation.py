# -*- coding: utf-8 -*-
"""Stage selected real media on one Android target and validate the plugin.

The runner reuses the benchmark runner's device and package safety checks. It
refuses physical devices by default and replaces an existing plugin only when
explicitly requested. It limits the aggregate transfer to 2 GiB unless explicitly
overridden, stores samples only in the app-specific external directory, removes
every staged sample, and uninstalls only packages it installed.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import shlex
import subprocess
import sys
from pathlib import Path
from typing import Any

import run_mediainfo_benchmark as common

RESULT_SCHEMA = "autojs6-plugin-mediainfo-real-media-v1"
RESULT_FILE_NAME = "mediainfo-real-media-result.json"
VALIDATION_CLASS = f"{common.APP_ID}.MediainfoRealMediaValidationTest"
REMOTE_DIRECTORY = f"/sdcard/Android/data/{common.APP_ID}/files/media-validation"
DEFAULT_MAX_TRANSFER_BYTES = 2 * 1024 * 1024 * 1024


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as source:
        while chunk := source.read(1024 * 1024):
            digest.update(chunk)
    return digest.hexdigest()


def validate_samples(values: list[str], allow_large_transfer: bool) -> list[Path]:
    samples = [Path(value).expanduser().resolve() for value in values]
    missing = [str(path) for path in samples if not path.is_file()]
    if missing:
        raise common.BenchmarkRunnerError("Missing real-media samples: " + ", ".join(missing))
    if len(set(samples)) != len(samples):
        raise common.BenchmarkRunnerError("Duplicate --sample paths are not allowed")
    total = sum(path.stat().st_size for path in samples)
    if total > DEFAULT_MAX_TRANSFER_BYTES and not allow_large_transfer:
        raise common.BenchmarkRunnerError(
            f"Selected samples total {total} bytes; pass --allow-large-transfer to exceed 2 GiB"
        )
    return samples


def output_path(value: str | None, overwrite: bool) -> Path | None:
    if value is None:
        return None
    path = Path(value)
    if not path.is_absolute():
        path = common.PROJECT_ROOT / path
    path = path.resolve()
    if path.suffix.lower() != ".json":
        raise common.BenchmarkRunnerError("Validation output must use the .json extension")
    if path.exists() and not overwrite:
        raise common.BenchmarkRunnerError(f"Output already exists: {path}; pass --overwrite to replace it")
    return path


def stage_samples(adb_executable: str, serial: str, samples: list[Path]) -> list[dict[str, Any]]:
    common.adb(adb_executable, serial, "shell", "mkdir", "-p", REMOTE_DIRECTORY, timeout=60)
    metadata: list[dict[str, Any]] = []
    for index, sample in enumerate(samples):
        staged_name = f"{index:03d}-{sample.name}"
        remote_path = f"{REMOTE_DIRECTORY}/{staged_name}"
        sample_size = sample.stat().st_size
        transfer_timeout = max(3_600, sample_size // (5 * 1024 * 1024) + 600)
        print(f"Staging {sample.name} ({sample_size} bytes)...", flush=True)
        result = common.adb(
            adb_executable,
            serial,
            "push",
            "-Z",
            str(sample),
            remote_path,
            timeout=transfer_timeout,
        )
        if result.returncode != 0:
            raise common.BenchmarkRunnerError(f"adb push failed for {sample}:\n{result.stdout}\n{result.stderr}")
        metadata.append(
            {
                "stagedName": staged_name,
                "sourceLabel": f"{sample.parent.name}/{sample.name}",
                "sizeBytes": sample_size,
                "sha256": sha256(sample),
            }
        )
    return metadata


def staged_paths(samples: list[Path]) -> list[str]:
    return [
        f"{REMOTE_DIRECTORY}/{index:03d}-{sample.name}"
        for index, sample in enumerate(samples)
    ]


def cleanup_staged_samples(adb_executable: str, serial: str, samples: list[Path]) -> None:
    cleanup_failures: list[str] = []
    for remote_path in staged_paths(samples):
        if not remote_path.startswith(f"{REMOTE_DIRECTORY}/"):
            raise common.BenchmarkRunnerError(f"Refusing unsafe staged path: {remote_path}")
        quoted_path = shlex.quote(remote_path)
        result = common.adb(
            adb_executable,
            serial,
            "shell",
            f"rm -f -- {quoted_path}",
            check=False,
            timeout=120,
        )
        if result.returncode != 0:
            cleanup_failures.append(
                f"could not remove {remote_path}: {result.stdout.strip()} {result.stderr.strip()}"
            )
            continue
        verification = common.adb(
            adb_executable,
            serial,
            "shell",
            f"test ! -e {quoted_path}",
            check=False,
            timeout=30,
        )
        if verification.returncode != 0:
            cleanup_failures.append(f"staged sample still exists after removal: {remote_path}")
    if cleanup_failures:
        raise common.BenchmarkRunnerError("Real-media cleanup failed:\n" + "\n".join(cleanup_failures))


def run_validation(adb_executable: str, serial: str, capture_details: bool = False) -> None:
    target = f"{common.TEST_APP_ID}/{common.RUNNER}"
    command = [
        "shell",
        "am",
        "instrument",
        "-w",
        "-r",
        "-e",
        "class",
        VALIDATION_CLASS,
        "-e",
        "realMediaValidation",
        "true",
    ]
    if capture_details:
        command.extend(["-e", "realMediaCaptureDetails", "true"])
    command.append(target)
    result = common.adb(adb_executable, serial, *command, timeout=1_800)
    output = "\n".join(part for part in (result.stdout.strip(), result.stderr.strip()) if part)
    failure_markers = ("FAILURES!!!", "INSTRUMENTATION_FAILED", "Process crashed", "shortMsg=Process crashed")
    if any(marker in output for marker in failure_markers) or "OK (1 test)" not in output:
        raise common.BenchmarkRunnerError(f"Real-media instrumentation did not pass:\n{output}")
    common.print_tail("Instrumentation", output)


def collect_result(adb_executable: str, serial: str) -> dict[str, Any]:
    result = common.adb(
        adb_executable,
        serial,
        "exec-out",
        "run-as",
        common.APP_ID,
        "cat",
        f"files/{RESULT_FILE_NAME}",
        timeout=60,
    )
    try:
        payload = json.loads(result.stdout)
    except json.JSONDecodeError as error:
        raise common.BenchmarkRunnerError(f"Invalid real-media result JSON: {error}\n{result.stdout}") from None
    if payload.get("schema") != RESULT_SCHEMA:
        raise common.BenchmarkRunnerError(f"Unexpected real-media result schema: {payload.get('schema')!r}")
    return payload


def merge_sample_metadata(payload: dict[str, Any], metadata: list[dict[str, Any]]) -> dict[str, Any]:
    by_name = {item["stagedName"]: item for item in metadata}
    for item in payload["files"]:
        source = by_name.get(item["stagedName"])
        if source is None:
            raise common.BenchmarkRunnerError(f"Unexpected staged result: {item['stagedName']}")
        item["sourceLabel"] = source["sourceLabel"]
        item["sha256"] = source["sha256"]
        if item["sizeBytes"] != source["sizeBytes"]:
            raise common.BenchmarkRunnerError(f"Size changed while staging {item['stagedName']}")
    payload["source"] = common.git_metadata()
    return payload


def print_summary(payload: dict[str, Any]) -> None:
    print("\nsample                                      size MiB  format       cold ms  cached ms  report")
    print("------------------------------------------- --------- ------------ --------- ---------- -------")
    for item in payload["files"]:
        print(
            f"{item['sourceLabel'][:43]:<43} {item['sizeBytes'] / 1_048_576:>9.2f} "
            f"{item['generalFormat'][:12]:<12} {item['coldInformNanos'] / 1_000_000:>9.3f} "
            f"{item['cachedInformNanos'] / 1_000_000:>10.3f} {item['informLength']:>7}"
        )


def write_result(payload: dict[str, Any], path: Path | None) -> None:
    rendered = json.dumps(payload, ensure_ascii=False, indent=2) + "\n"
    if path is None:
        print(rendered, end="")
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(rendered, encoding="utf-8")
    print(f"Result: {path}")


def parser() -> argparse.ArgumentParser:
    result = argparse.ArgumentParser(description=__doc__)
    result.add_argument("--serial", required=True, help="Exact adb serial; an emulator is required by default")
    result.add_argument("--sample", action="append", required=True, help="Real media path; repeat for each file")
    result.add_argument("--output", help="Optional JSON output path")
    result.add_argument("--overwrite", action="store_true")
    result.add_argument("--skip-build", action="store_true")
    result.add_argument("--allow-physical-device", action="store_true")
    result.add_argument("--allow-large-transfer", action="store_true", help="Allow aggregate samples over 2 GiB")
    result.add_argument(
        "--capture-details",
        action="store_true",
        help=(
            "Include full reports, parsed sections, native MediaInfo JSON, and fixed technical field "
            "queries in the JSON output"
        ),
    )
    result.add_argument(
        "--update-existing-package",
        action="store_true",
        help="Update an existing plugin with adb install -r and preserve it after validation",
    )
    return result


def main() -> int:
    args = parser().parse_args()
    samples = validate_samples(args.sample, args.allow_large_transfer)
    destination = output_path(args.output, args.overwrite)
    adb_executable = common.adb_path()
    app_preexisting = common.package_installed(adb_executable, args.serial, common.APP_ID)
    device = common.validate_device(
        adb_executable,
        args.serial,
        args.allow_physical_device,
        allow_existing_app=args.update_existing_package,
    )
    if args.update_existing_package and not app_preexisting:
        raise common.BenchmarkRunnerError(
            f"--update-existing-package requires {common.APP_ID} to be installed"
        )
    print(
        f"Device: serial={device['serial']} model={device['model']} api={device['api']} "
        f"abi={device['abi']} emulator={device['isEmulator']}"
    )
    if not args.skip_build:
        common.build_apks()
    app_apk, test_apk = common.apk_paths(device["abi"])

    app_installed = False
    test_installed = False
    try:
        if app_preexisting:
            result = common.adb(
                adb_executable,
                args.serial,
                "install",
                "-r",
                "-t",
                str(app_apk),
                timeout=300,
            )
            if "Success" not in result.stdout:
                raise common.BenchmarkRunnerError(
                    f"adb did not confirm update of {app_apk.name}:\n{result.stdout}\n{result.stderr}"
                )
        else:
            common.install(adb_executable, args.serial, app_apk)
            app_installed = True
        common.install(adb_executable, args.serial, test_apk)
        test_installed = True
        metadata = stage_samples(adb_executable, args.serial, samples)
        run_validation(adb_executable, args.serial, args.capture_details)
        payload = merge_sample_metadata(collect_result(adb_executable, args.serial), metadata)
        print_summary(payload)
        write_result(payload, destination)
    finally:
        cleanup_staged_samples(adb_executable, args.serial, samples)
        common.uninstall_if_needed(adb_executable, args.serial, common.TEST_APP_ID, test_installed)
        common.uninstall_if_needed(adb_executable, args.serial, common.APP_ID, app_installed)
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (common.BenchmarkRunnerError, subprocess.TimeoutExpired) as error:
        print(f"REAL_MEDIA_ERROR {error}", file=sys.stderr)
        raise SystemExit(1)
