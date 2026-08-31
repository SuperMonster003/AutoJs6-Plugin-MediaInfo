# -*- coding: utf-8 -*-
"""Run the explicit MediaInfo end-to-end benchmark on one selected Android device.

The runner deliberately requires a serial, refuses physical devices by default and
refuses to replace an existing plugin/test installation. It builds the debug APKs,
installs only the matching ABI split, runs the manual instrumentation benchmark,
collects its JSON result with run-as, and removes the two packages it installed.

Examples:
  py .python/run_mediainfo_benchmark.py --serial emulator-5556 --profile smoke
  py .python/run_mediainfo_benchmark.py --serial emulator-5556 --profile full \
      --output benchmark/results/api36-x86_64.json
"""

from __future__ import annotations

import argparse
import hashlib
import json
import platform
import shutil
import subprocess
import sys
from pathlib import Path
from typing import Any, Sequence

PROJECT_ROOT = Path(__file__).resolve().parents[1]
APP_ID = "io.github.supermonster003.autojs6.plugin.mediainfo"
TEST_APP_ID = f"{APP_ID}.test"
RUNNER = "androidx.test.runner.AndroidJUnitRunner"
BENCHMARK_CLASS = f"{APP_ID}.MediainfoPerformanceBenchmarkTest"
RESULT_FILE_NAME = "mediainfo-benchmark-result.json"
RESULT_SCHEMA = "autojs6-plugin-mediainfo-benchmark-v1"
RUNNER_SCHEMA_VERSION = 1
BENCHMARK_INPUT_PATTERNS = (
    ".python/run_mediainfo_benchmark.py",
    ".python/run_real_media_validation.py",
    "app/build.gradle.kts",
    "app/src/main/AndroidManifest.xml",
    "app/src/main/java/**/*.kt",
    "app/src/main/jniLibs/**/*.so",
    "app/src/androidTest/**/*.kt",
    "libs/*.aar",
    "version.properties",
)


class BenchmarkRunnerError(Exception):
    """Raised for an expected, user-actionable benchmark runner failure."""


def run(
    command: Sequence[str | Path],
    *,
    check: bool = True,
    timeout: int = 900,
    cwd: Path = PROJECT_ROOT,
) -> subprocess.CompletedProcess[str]:
    result = subprocess.run(
        [str(item) for item in command],
        cwd=cwd,
        check=False,
        capture_output=True,
        text=True,
        encoding="utf-8",
        errors="replace",
        timeout=timeout,
    )
    if check and result.returncode != 0:
        details = "\n".join(part.strip() for part in (result.stdout, result.stderr) if part.strip())
        raise BenchmarkRunnerError(
            f"Command failed with exit code {result.returncode}: {' '.join(map(str, command))}\n{details}"
        )
    return result


def adb_path() -> str:
    path = shutil.which("adb")
    if not path:
        raise BenchmarkRunnerError("adb is not on PATH")
    return path


def adb(adb_executable: str, serial: str, *arguments: str, check: bool = True, timeout: int = 900) -> subprocess.CompletedProcess[str]:
    return run([adb_executable, "-s", serial, *arguments], check=check, timeout=timeout)


def device_property(adb_executable: str, serial: str, name: str) -> str:
    return adb(adb_executable, serial, "shell", "getprop", name).stdout.strip()


def package_installed(adb_executable: str, serial: str, package_name: str) -> bool:
    result = adb(adb_executable, serial, "shell", "pm", "path", package_name, check=False)
    return result.returncode == 0 and any(line.startswith("package:") for line in result.stdout.splitlines())


def validate_device(
    adb_executable: str,
    serial: str,
    allow_physical_device: bool,
    allow_existing_app: bool = False,
) -> dict[str, Any]:
    state = adb(adb_executable, serial, "get-state", timeout=30).stdout.strip()
    if state != "device":
        raise BenchmarkRunnerError(f"Device {serial!r} is not ready: state={state!r}")

    is_emulator = serial.startswith("emulator-") or device_property(adb_executable, serial, "ro.kernel.qemu") == "1"
    if not is_emulator and not allow_physical_device:
        raise BenchmarkRunnerError(
            f"Refusing physical device {serial!r}; pass --allow-physical-device only after explicit user approval"
        )

    installed = [
        package_name
        for package_name in (APP_ID, TEST_APP_ID)
        if package_installed(adb_executable, serial, package_name)
    ]
    unexpected = [
        package_name
        for package_name in installed
        if package_name != APP_ID or not allow_existing_app
    ]
    if unexpected:
        raise BenchmarkRunnerError(
            "Refusing to replace existing packages on the selected device: " + ", ".join(unexpected)
        )

    return {
        "serial": serial,
        "isEmulator": is_emulator,
        "api": int(device_property(adb_executable, serial, "ro.build.version.sdk")),
        "abi": device_property(adb_executable, serial, "ro.product.cpu.abi"),
        "model": device_property(adb_executable, serial, "ro.product.model"),
    }


def build_apks() -> None:
    wrapper = PROJECT_ROOT / ("gradlew.bat" if platform.system() == "Windows" else "gradlew")
    if not wrapper.is_file():
        raise BenchmarkRunnerError(f"Missing Gradle wrapper: {wrapper}")
    result = run(
        [wrapper, ":app:assembleDebug", ":app:assembleDebugAndroidTest", "--stacktrace"],
        timeout=1_200,
    )
    print_tail("Gradle", result.stdout)


def apk_paths(abi: str) -> tuple[Path, Path]:
    app_apk = PROJECT_ROOT / "app" / "build" / "outputs" / "apk" / "debug" / f"app-{abi}-debug.apk"
    if not app_apk.is_file():
        app_apk = PROJECT_ROOT / "app" / "build" / "outputs" / "apk" / "debug" / "app-universal-debug.apk"
    test_apk = (
        PROJECT_ROOT
        / "app"
        / "build"
        / "outputs"
        / "apk"
        / "androidTest"
        / "debug"
        / "app-debug-androidTest.apk"
    )
    for path in (app_apk, test_apk):
        if not path.is_file():
            raise BenchmarkRunnerError(f"Missing benchmark APK: {path}")
    return app_apk, test_apk


def install(adb_executable: str, serial: str, apk: Path) -> None:
    result = adb(adb_executable, serial, "install", "-t", str(apk), timeout=300)
    if "Success" not in result.stdout:
        raise BenchmarkRunnerError(f"adb did not confirm installation of {apk.name}:\n{result.stdout}\n{result.stderr}")


def instrumentation_arguments(args: argparse.Namespace) -> list[str]:
    values: list[tuple[str, str]] = [
        ("class", BENCHMARK_CLASS),
        ("mediainfoBenchmark", "true"),
        ("benchmarkProfile", args.profile),
    ]
    optional = [
        ("benchmarkFormats", args.formats),
        ("benchmarkDirectSizesMiB", args.direct_sizes_mib),
        ("benchmarkFallbackSizesMiB", args.fallback_sizes_mib),
        ("benchmarkWarmups", args.warmups),
        ("benchmarkIterations", args.iterations),
    ]
    values.extend((key, str(value)) for key, value in optional if value is not None)
    flattened: list[str] = []
    for key, value in values:
        flattened.extend(["-e", key, value])
    return flattened


def run_instrumentation(adb_executable: str, serial: str, args: argparse.Namespace) -> str:
    target = f"{TEST_APP_ID}/{RUNNER}"
    command = ["shell", "am", "instrument", "-w", "-r", *instrumentation_arguments(args), target]
    result = adb(adb_executable, serial, *command, timeout=1_800)
    output = "\n".join(part for part in (result.stdout.strip(), result.stderr.strip()) if part)
    failure_markers = ("FAILURES!!!", "INSTRUMENTATION_FAILED", "Process crashed", "shortMsg=Process crashed")
    if any(marker in output for marker in failure_markers) or "OK (1 test)" not in output:
        raise BenchmarkRunnerError(f"Benchmark instrumentation did not pass:\n{output}")
    print_tail("Instrumentation", output)
    return output


def collect_result(adb_executable: str, serial: str) -> dict[str, Any]:
    result = adb(
        adb_executable,
        serial,
        "exec-out",
        "run-as",
        APP_ID,
        "cat",
        f"files/{RESULT_FILE_NAME}",
        timeout=60,
    )
    try:
        payload = json.loads(result.stdout)
    except json.JSONDecodeError as error:
        raise BenchmarkRunnerError(f"Invalid benchmark JSON returned by the device: {error}\n{result.stdout}") from None
    if payload.get("schema") != RESULT_SCHEMA:
        raise BenchmarkRunnerError(f"Unexpected benchmark schema: {payload.get('schema')!r}")
    return payload


def git_metadata() -> dict[str, Any]:
    revision = run(["git", "rev-parse", "HEAD"], timeout=30).stdout.strip()
    status = run(["git", "status", "--porcelain"], timeout=30).stdout
    return {
        "revision": revision,
        "dirty": bool(status.strip()),
        "benchmarkInputsSha256": benchmark_inputs_digest(),
    }


def benchmark_inputs_digest() -> str:
    paths: set[Path] = set()
    for pattern in BENCHMARK_INPUT_PATTERNS:
        paths.update(path for path in PROJECT_ROOT.glob(pattern) if path.is_file())
    if not paths:
        raise BenchmarkRunnerError("No benchmark input files were found for the source digest")

    digest = hashlib.sha256()
    for path in sorted(paths, key=lambda item: item.relative_to(PROJECT_ROOT).as_posix()):
        relative = path.relative_to(PROJECT_ROOT).as_posix().encode("utf-8")
        digest.update(len(relative).to_bytes(4, "big"))
        digest.update(relative)
        digest.update(path.stat().st_size.to_bytes(8, "big"))
        with path.open("rb") as source:
            while chunk := source.read(1024 * 1024):
                digest.update(chunk)
    return digest.hexdigest()


def enrich_result(payload: dict[str, Any], device: dict[str, Any], args: argparse.Namespace) -> dict[str, Any]:
    payload["source"] = git_metadata()
    payload["runner"] = {
        "schemaVersion": RUNNER_SCHEMA_VERSION,
        "profile": args.profile,
        "deviceWasEmulator": device["isEmulator"],
        "python": platform.python_version(),
    }
    return payload


def write_result(payload: dict[str, Any], output: str | None, overwrite: bool) -> None:
    rendered = json.dumps(payload, ensure_ascii=False, indent=2) + "\n"
    if output is None:
        print(rendered, end="")
        return

    path = Path(output)
    if not path.is_absolute():
        path = PROJECT_ROOT / path
    path = path.resolve()
    if path.suffix.lower() != ".json":
        raise BenchmarkRunnerError("Benchmark output must use the .json extension")
    if path.exists() and not overwrite:
        raise BenchmarkRunnerError(f"Output already exists: {path}; pass --overwrite to replace it")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(rendered, encoding="utf-8")
    print(f"Result: {path}")


def print_summary(payload: dict[str, Any]) -> None:
    print("\nformat       mode               size MiB   median ms    min ms    max ms   MiB/s")
    print("------------ ------------------ ---------- ----------- --------- --------- --------")
    for item in payload["measurements"]:
        size_mib = item["sizeBytes"] / 1_048_576
        median_ms = item["medianNanos"] / 1_000_000
        min_ms = item["minNanos"] / 1_000_000
        max_ms = item["maxNanos"] / 1_000_000
        print(
            f"{item['format']:<12} {item['mode']:<18} {size_mib:>10.0f} "
            f"{median_ms:>11.3f} {min_ms:>9.3f} {max_ms:>9.3f} {item['throughputMiBPerSecond']:>8.1f}"
        )


def print_tail(label: str, text: str, lines: int = 12) -> None:
    content = text.strip().splitlines()
    print(f"{label}:\n" + "\n".join(content[-lines:]))


def uninstall_if_needed(adb_executable: str, serial: str, package_name: str, installed_by_runner: bool) -> None:
    if not installed_by_runner:
        return
    result = adb(adb_executable, serial, "uninstall", package_name, check=False, timeout=120)
    if result.returncode != 0 or "Success" not in result.stdout:
        print(f"WARNING: could not uninstall {package_name}: {result.stdout} {result.stderr}", file=sys.stderr)


def parser() -> argparse.ArgumentParser:
    result = argparse.ArgumentParser(description=__doc__)
    result.add_argument("--serial", required=True, help="Exact adb serial; an emulator is required by default")
    result.add_argument("--profile", choices=("smoke", "full"), default="smoke")
    result.add_argument("--formats", help="Comma-separated format IDs: wave_pcm,bitmap_rgb")
    result.add_argument("--direct-sizes-mib", help="Comma-separated regular-FD sizes, each 1..1024")
    result.add_argument("--fallback-sizes-mib", help="Comma-separated pipe sizes, each 1..256")
    result.add_argument("--warmups", type=int, help="Positive warm-up count for the smallest case")
    result.add_argument("--iterations", type=int, help="Positive measured iteration count per case")
    result.add_argument("--output", help="Optional JSON result path, relative to the repository root")
    result.add_argument("--overwrite", action="store_true", help="Allow replacing an existing --output file")
    result.add_argument("--skip-build", action="store_true", help="Reuse already assembled APKs")
    result.add_argument(
        "--allow-physical-device",
        action="store_true",
        help="Allow the explicitly selected serial to be a physical device",
    )
    return result


def main() -> int:
    args = parser().parse_args()
    adb_executable = adb_path()
    device = validate_device(adb_executable, args.serial, args.allow_physical_device)
    print(
        f"Device: serial={device['serial']} model={device['model']} api={device['api']} "
        f"abi={device['abi']} emulator={device['isEmulator']}"
    )
    if not args.skip_build:
        build_apks()
    app_apk, test_apk = apk_paths(device["abi"])

    app_installed = False
    test_installed = False
    try:
        install(adb_executable, args.serial, app_apk)
        app_installed = True
        install(adb_executable, args.serial, test_apk)
        test_installed = True
        run_instrumentation(adb_executable, args.serial, args)
        payload = enrich_result(collect_result(adb_executable, args.serial), device, args)
        print_summary(payload)
        write_result(payload, args.output, args.overwrite)
    finally:
        uninstall_if_needed(adb_executable, args.serial, TEST_APP_ID, test_installed)
        uninstall_if_needed(adb_executable, args.serial, APP_ID, app_installed)
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (BenchmarkRunnerError, subprocess.TimeoutExpired) as error:
        print(f"BENCHMARK_ERROR {error}", file=sys.stderr)
        raise SystemExit(1)
