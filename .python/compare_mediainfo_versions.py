# -*- coding: utf-8 -*-
"""Compare one published MediaInfo plugin APK with a source-built candidate.

The runner is intentionally conservative: it requires an exact adb serial,
refuses physical devices unless explicitly allowed, refuses pre-existing plugin
or test packages, stages the selected media only once, and verifies the installed
APK hash for both versions. The full reports are written only when --raw-output
is supplied. The normal --output artifact contains hashes, structural changes,
and a fixed set of non-sensitive technical query values suitable for review.
"""

from __future__ import annotations

import argparse
import difflib
import hashlib
import json
import re
import subprocess
import sys
from datetime import datetime, timezone
from pathlib import Path
from typing import Any

import run_mediainfo_benchmark as common
import run_real_media_validation as real_media

RESULT_SCHEMA = "autojs6-plugin-mediainfo-version-diff-v1"
RAW_RESULT_SCHEMA = "autojs6-plugin-mediainfo-version-diff-raw-v1"
TECHNICAL_FIELD_FRAGMENTS = (
    "bitdepth",
    "bitrate",
    "channel",
    "chroma",
    "codec",
    "colorspace",
    "compression",
    "default",
    "displayaspectratio",
    "duration",
    "encodedapplication",
    "encodedlibrary",
    "filesize",
    "forced",
    "format",
    "framecount",
    "framerate",
    "hdr",
    "height",
    "language",
    "overallbitrate",
    "samplingrate",
    "scantype",
    "streamsize",
    "width",
)


def resolve_file(value: str, description: str) -> Path:
    path = Path(value).expanduser().resolve()
    if not path.is_file():
        raise common.BenchmarkRunnerError(f"Missing {description}: {path}")
    return path


def output_path(value: str, overwrite: bool, description: str) -> Path:
    path = Path(value)
    if not path.is_absolute():
        path = common.PROJECT_ROOT / path
    path = path.resolve()
    if path.suffix.lower() != ".json":
        raise common.BenchmarkRunnerError(f"{description} must use the .json extension")
    if path.exists() and not overwrite:
        raise common.BenchmarkRunnerError(f"Output already exists: {path}; pass --overwrite to replace it")
    return path


def write_json(path: Path, payload: dict[str, Any]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Result: {path}")


def digest_text(value: str) -> str:
    return hashlib.sha256(value.encode("utf-8")).hexdigest()


def digest_json(value: Any) -> str:
    rendered = json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))
    return digest_text(rendered)


def normalize_report(value: str) -> str:
    return value.replace("\r\n", "\n").replace("\r", "\n").rstrip() + "\n"


def install_update(adb_executable: str, serial: str, apk: Path) -> None:
    result = common.adb(adb_executable, serial, "install", "-r", "-t", str(apk), timeout=300)
    if "Success" not in result.stdout:
        raise common.BenchmarkRunnerError(
            f"adb did not confirm update to {apk.name}:\n{result.stdout}\n{result.stderr}"
        )


def installed_identity(
    adb_executable: str,
    serial: str,
    apk: Path,
    label: str,
    engine_version: str,
) -> dict[str, Any]:
    package_result = common.adb(
        adb_executable,
        serial,
        "shell",
        "pm",
        "path",
        common.APP_ID,
        check=False,
        timeout=30,
    )
    package_lines = [line.strip() for line in package_result.stdout.splitlines() if line.startswith("package:")]
    if not package_lines:
        raise common.BenchmarkRunnerError(f"Could not resolve installed APK for {label}")
    remote_apk = package_lines[0].removeprefix("package:")
    remote_hash_result = common.adb(
        adb_executable,
        serial,
        "shell",
        "sha256sum",
        remote_apk,
        timeout=60,
    )
    remote_hash = remote_hash_result.stdout.strip().split()[0].lower()
    local_hash = real_media.sha256(apk)
    if remote_hash != local_hash:
        raise common.BenchmarkRunnerError(
            f"Installed APK hash mismatch for {label}: local={local_hash} device={remote_hash}"
        )

    package_dump = common.adb(
        adb_executable,
        serial,
        "shell",
        "dumpsys",
        "package",
        common.APP_ID,
        timeout=60,
    ).stdout
    version_name_match = re.search(r"^\s*versionName=(.+)$", package_dump, re.MULTILINE)
    version_code_match = re.search(r"^\s*versionCode=(\d+)", package_dump, re.MULTILINE)
    if not version_name_match or not version_code_match:
        raise common.BenchmarkRunnerError(f"Could not resolve installed version metadata for {label}")
    return {
        "label": label,
        "pluginVersionName": version_name_match.group(1).strip(),
        "pluginVersionCode": int(version_code_match.group(1)),
        "mediaInfoVersion": engine_version,
        "apkFileName": apk.name,
        "apkSizeBytes": apk.stat().st_size,
        "apkSha256": local_hash,
        "installedApkSha256": remote_hash,
    }


def report_difference(baseline: str, candidate: str) -> dict[str, Any]:
    baseline_report = normalize_report(baseline)
    candidate_report = normalize_report(candidate)
    baseline_lines = baseline_report.splitlines()
    candidate_lines = candidate_report.splitlines()
    matcher = difflib.SequenceMatcher(a=baseline_lines, b=candidate_lines, autojunk=False)
    inserted = 0
    removed = 0
    replaced_baseline = 0
    replaced_candidate = 0
    for operation, baseline_start, baseline_end, candidate_start, candidate_end in matcher.get_opcodes():
        if operation == "insert":
            inserted += candidate_end - candidate_start
        elif operation == "delete":
            removed += baseline_end - baseline_start
        elif operation == "replace":
            replaced_baseline += baseline_end - baseline_start
            replaced_candidate += candidate_end - candidate_start
    unified = "\n".join(
        difflib.unified_diff(
            baseline_lines,
            candidate_lines,
            fromfile="baseline",
            tofile="candidate",
            lineterm="",
        )
    )
    return {
        "changed": baseline_report != candidate_report,
        "baselineLength": len(baseline),
        "candidateLength": len(candidate),
        "baselineLineCount": len(baseline_lines),
        "candidateLineCount": len(candidate_lines),
        "baselineSha256": digest_text(baseline_report),
        "candidateSha256": digest_text(candidate_report),
        "insertedLineCount": inserted,
        "removedLineCount": removed,
        "replacedBaselineLineCount": replaced_baseline,
        "replacedCandidateLineCount": replaced_candidate,
        "unifiedDiffSha256": digest_text(unified),
    }


def reveal_field(path: str) -> bool:
    normalized = re.sub(r"[^a-z0-9]", "", path.lower())
    return any(fragment in normalized for fragment in TECHNICAL_FIELD_FRAGMENTS)


def value_evidence(path: str, value: str) -> dict[str, Any]:
    result: dict[str, Any] = {
        "length": len(value),
        "sha256": digest_text(value),
    }
    if reveal_field(path):
        result["value"] = value
    return result


def query_changes(baseline: dict[str, Any], candidate: dict[str, Any]) -> list[dict[str, Any]]:
    changes: list[dict[str, Any]] = []
    for path in sorted(set(baseline) | set(candidate)):
        baseline_present = path in baseline
        candidate_present = path in candidate
        baseline_value = str(baseline.get(path, ""))
        candidate_value = str(candidate.get(path, ""))
        if baseline_present == candidate_present and baseline_value == candidate_value:
            continue
        change: dict[str, Any] = {
            "path": path,
            "kind": "changed" if baseline_present and candidate_present else "added" if candidate_present else "removed",
        }
        if baseline_present:
            change["baseline"] = value_evidence(path, baseline_value)
        if candidate_present:
            change["candidate"] = value_evidence(path, candidate_value)
        changes.append(change)
    return changes


def section_occurrences(sections: dict[str, Any]) -> dict[str, dict[str, str]]:
    flattened: dict[str, dict[str, str]] = {}
    for section_name, occurrences in sections.items():
        if not isinstance(occurrences, list):
            continue
        for index, fields in enumerate(occurrences):
            if isinstance(fields, dict):
                flattened[f"{section_name}[{index}]"] = {str(key): str(value) for key, value in fields.items()}
    return flattened


def section_changes(baseline: dict[str, Any], candidate: dict[str, Any]) -> dict[str, Any]:
    baseline_occurrences = section_occurrences(baseline)
    candidate_occurrences = section_occurrences(candidate)
    added_occurrences = sorted(set(candidate_occurrences) - set(baseline_occurrences))
    removed_occurrences = sorted(set(baseline_occurrences) - set(candidate_occurrences))
    fields: list[dict[str, Any]] = []
    for occurrence in sorted(set(baseline_occurrences) & set(candidate_occurrences)):
        baseline_fields = baseline_occurrences[occurrence]
        candidate_fields = candidate_occurrences[occurrence]
        for field_name in sorted(set(baseline_fields) | set(candidate_fields)):
            path = f"{occurrence}.{field_name}"
            baseline_present = field_name in baseline_fields
            candidate_present = field_name in candidate_fields
            baseline_value = baseline_fields.get(field_name, "")
            candidate_value = candidate_fields.get(field_name, "")
            if baseline_present == candidate_present and baseline_value == candidate_value:
                continue
            change: dict[str, Any] = {
                "path": path,
                "kind": "changed" if baseline_present and candidate_present else "added" if candidate_present else "removed",
            }
            if baseline_present:
                change["baseline"] = value_evidence(path, baseline_value)
            if candidate_present:
                change["candidate"] = value_evidence(path, candidate_value)
            fields.append(change)
    return {
        "addedOccurrences": added_occurrences,
        "removedOccurrences": removed_occurrences,
        "fieldChanges": fields,
    }


def compare_file(baseline: dict[str, Any], candidate: dict[str, Any]) -> dict[str, Any]:
    if baseline.get("sha256") != candidate.get("sha256"):
        raise common.BenchmarkRunnerError(
            f"Sample identity mismatch for {baseline.get('sourceLabel')}: "
            f"{baseline.get('sha256')} != {candidate.get('sha256')}"
        )
    baseline_snapshot = baseline.get("snapshot")
    candidate_snapshot = candidate.get("snapshot")
    if not isinstance(baseline_snapshot, dict) or not isinstance(candidate_snapshot, dict):
        raise common.BenchmarkRunnerError("Detailed snapshot capture is missing from a comparison result")
    baseline_report = str(baseline_snapshot.get("inform", ""))
    candidate_report = str(candidate_snapshot.get("inform", ""))
    baseline_sections = baseline_snapshot.get("sections", {})
    candidate_sections = candidate_snapshot.get("sections", {})
    sections = section_changes(
        baseline_sections if isinstance(baseline_sections, dict) else {},
        candidate_sections if isinstance(candidate_sections, dict) else {},
    )
    queries = query_changes(
        baseline.get("queries", {}) if isinstance(baseline.get("queries"), dict) else {},
        candidate.get("queries", {}) if isinstance(candidate.get("queries"), dict) else {},
    )
    report = report_difference(baseline_report, candidate_report)
    changed = bool(
        report["changed"]
        or sections["addedOccurrences"]
        or sections["removedOccurrences"]
        or sections["fieldChanges"]
        or queries
    )
    return {
        "sourceLabel": baseline["sourceLabel"],
        "sizeBytes": baseline["sizeBytes"],
        "sha256": baseline["sha256"],
        "baselineFormat": baseline.get("generalFormat", ""),
        "candidateFormat": candidate.get("generalFormat", ""),
        "baselineSectionCount": baseline.get("snapshotSectionCount"),
        "candidateSectionCount": candidate.get("snapshotSectionCount"),
        "changed": changed,
        "report": report,
        "queries": {
            "baselineSha256": digest_json(baseline.get("queries", {})),
            "candidateSha256": digest_json(candidate.get("queries", {})),
            "changes": queries,
        },
        "sections": {
            "baselineSha256": digest_json(baseline_sections),
            "candidateSha256": digest_json(candidate_sections),
            **sections,
        },
    }


def compare_payloads(
    baseline: dict[str, Any],
    candidate: dict[str, Any],
    baseline_identity: dict[str, Any],
    candidate_identity: dict[str, Any],
    raw_sha256: str | None,
) -> dict[str, Any]:
    if baseline.get("failureCount") != 0 or candidate.get("failureCount") != 0:
        raise common.BenchmarkRunnerError("Cannot compare validation payloads containing failures")
    if not baseline.get("capturedDetails") or not candidate.get("capturedDetails"):
        raise common.BenchmarkRunnerError("Both validation payloads must be captured with details enabled")
    candidate_by_sha = {item["sha256"]: item for item in candidate["files"]}
    comparisons: list[dict[str, Any]] = []
    for baseline_file in baseline["files"]:
        candidate_file = candidate_by_sha.pop(baseline_file["sha256"], None)
        if candidate_file is None:
            raise common.BenchmarkRunnerError(f"Candidate result is missing {baseline_file['sourceLabel']}")
        comparisons.append(compare_file(baseline_file, candidate_file))
    if candidate_by_sha:
        raise common.BenchmarkRunnerError("Candidate result contains unexpected additional samples")

    payload: dict[str, Any] = {
        "schema": RESULT_SCHEMA,
        "generatedAtUtc": datetime.now(timezone.utc).isoformat(timespec="milliseconds").replace("+00:00", "Z"),
        "device": baseline["device"],
        "baseline": baseline_identity,
        "candidate": candidate_identity,
        "files": comparisons,
        "summary": {
            "sampleCount": len(comparisons),
            "changedSampleCount": sum(1 for item in comparisons if item["changed"]),
            "unchangedSampleCount": sum(1 for item in comparisons if not item["changed"]),
            "reportChangedSampleCount": sum(1 for item in comparisons if item["report"]["changed"]),
            "queryChangeCount": sum(len(item["queries"]["changes"]) for item in comparisons),
            "sectionOccurrenceChangeCount": sum(
                len(item["sections"]["addedOccurrences"]) + len(item["sections"]["removedOccurrences"])
                for item in comparisons
            ),
            "sectionFieldChangeCount": sum(len(item["sections"]["fieldChanges"]) for item in comparisons),
        },
        "review": {
            "status": "pending",
            "compatibility": None,
            "notes": [],
        },
        "source": common.git_metadata(),
    }
    if raw_sha256 is not None:
        payload["rawReviewSha256"] = raw_sha256
    return payload


def print_summary(payload: dict[str, Any]) -> None:
    summary = payload["summary"]
    print(
        "\nComparison: "
        f"samples={summary['sampleCount']} changed={summary['changedSampleCount']} "
        f"queryChanges={summary['queryChangeCount']} "
        f"sectionOccurrenceChanges={summary['sectionOccurrenceChangeCount']} "
        f"sectionFieldChanges={summary['sectionFieldChangeCount']}"
    )
    for item in payload["files"]:
        print(
            f"- {item['sourceLabel']}: reportChanged={item['report']['changed']} "
            f"queries={len(item['queries']['changes'])} "
            f"sectionFields={len(item['sections']['fieldChanges'])}"
        )


def parser() -> argparse.ArgumentParser:
    result = argparse.ArgumentParser(description=__doc__)
    result.add_argument("--serial", required=True, help="Exact adb serial; an emulator is required by default")
    result.add_argument("--baseline-apk", required=True, help="Published baseline APK for the target ABI")
    result.add_argument("--candidate-apk", help="Candidate APK; defaults to the built APK matching the device ABI")
    result.add_argument("--test-apk", help="Instrumentation APK; defaults to the current debug androidTest APK")
    result.add_argument("--sample", action="append", required=True, help="Real media path; repeat for each file")
    result.add_argument("--output", required=True, help="Sanitized JSON evidence path")
    result.add_argument("--raw-output", help="Optional local-only JSON containing full reports and snapshots")
    result.add_argument("--baseline-label", default="v1.1.0")
    result.add_argument("--candidate-label", default="v2.0.0 candidate")
    result.add_argument("--baseline-engine", default="0.7.83")
    result.add_argument("--candidate-engine", default="26.05")
    result.add_argument("--overwrite", action="store_true")
    result.add_argument("--skip-build", action="store_true")
    result.add_argument("--allow-physical-device", action="store_true")
    result.add_argument("--allow-large-transfer", action="store_true")
    return result


def main() -> int:
    args = parser().parse_args()
    destination = output_path(args.output, args.overwrite, "Comparison output")
    raw_destination = (
        output_path(args.raw_output, args.overwrite, "Raw comparison output") if args.raw_output else None
    )
    baseline_apk = resolve_file(args.baseline_apk, "baseline APK")
    samples = real_media.validate_samples(args.sample, args.allow_large_transfer)
    adb_executable = common.adb_path()
    device = common.validate_device(adb_executable, args.serial, args.allow_physical_device)
    print(
        f"Device: serial={device['serial']} model={device['model']} api={device['api']} "
        f"abi={device['abi']} emulator={device['isEmulator']}"
    )
    if not args.skip_build:
        common.build_apks()
    default_candidate, default_test = common.apk_paths(device["abi"])
    candidate_apk = resolve_file(args.candidate_apk, "candidate APK") if args.candidate_apk else default_candidate
    test_apk = resolve_file(args.test_apk, "androidTest APK") if args.test_apk else default_test

    app_installed = False
    test_installed = False
    try:
        common.install(adb_executable, args.serial, baseline_apk)
        app_installed = True
        common.install(adb_executable, args.serial, test_apk)
        test_installed = True
        metadata = real_media.stage_samples(adb_executable, args.serial, samples)

        baseline_identity = installed_identity(
            adb_executable,
            args.serial,
            baseline_apk,
            args.baseline_label,
            args.baseline_engine,
        )
        real_media.run_validation(adb_executable, args.serial, capture_details=True)
        baseline = real_media.merge_sample_metadata(
            real_media.collect_result(adb_executable, args.serial),
            metadata,
        )

        install_update(adb_executable, args.serial, candidate_apk)
        candidate_identity = installed_identity(
            adb_executable,
            args.serial,
            candidate_apk,
            args.candidate_label,
            args.candidate_engine,
        )
        real_media.run_validation(adb_executable, args.serial, capture_details=True)
        candidate = real_media.merge_sample_metadata(
            real_media.collect_result(adb_executable, args.serial),
            metadata,
        )

        raw_sha256: str | None = None
        if raw_destination is not None:
            raw_payload = {
                "schema": RAW_RESULT_SCHEMA,
                "baseline": baseline,
                "candidate": candidate,
            }
            rendered_raw = json.dumps(raw_payload, ensure_ascii=False, indent=2) + "\n"
            raw_destination.parent.mkdir(parents=True, exist_ok=True)
            raw_destination.write_text(rendered_raw, encoding="utf-8")
            raw_sha256 = real_media.sha256(raw_destination)
            print(f"Raw review result: {raw_destination}")

        evidence = compare_payloads(
            baseline,
            candidate,
            baseline_identity,
            candidate_identity,
            raw_sha256,
        )
        print_summary(evidence)
        write_json(destination, evidence)
    finally:
        if app_installed:
            real_media.cleanup_staged_samples(adb_executable, args.serial, samples)
        common.uninstall_if_needed(adb_executable, args.serial, common.TEST_APP_ID, test_installed)
        common.uninstall_if_needed(adb_executable, args.serial, common.APP_ID, app_installed)
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (common.BenchmarkRunnerError, subprocess.TimeoutExpired) as error:
        print(f"VERSION_DIFF_ERROR {error}", file=sys.stderr)
        raise SystemExit(1)
