#!/usr/bin/env python3
"""Verify pinned upstream sources and Android libmediainfo.so artifacts."""

from __future__ import annotations

import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
from typing import Any
import zipfile


REPOSITORY_ROOT = Path(__file__).resolve().parents[1]
LOCK_PATH = REPOSITORY_ROOT / "native" / "upstream.lock.json"
ALLOWED_ANDROID_DEPENDENCIES = {"libz.so", "libm.so", "libdl.so", "libc.so"}
EXPECTED_MACHINE_TEXT = {
    "arm64-v8a": "AArch64",
    "armeabi-v7a": "ARM",
    "x86": "Intel 80386",
    "x86_64": "Advanced Micro Devices X86-64",
}
EXPECTED_ELF_CLASS = {
    "arm64-v8a": "ELF64",
    "armeabi-v7a": "ELF32",
    "x86": "ELF32",
    "x86_64": "ELF64",
}


class VerificationError(RuntimeError):
    pass


def run(command: list[str], cwd: Path = REPOSITORY_ROOT) -> str:
    completed = subprocess.run(
        command,
        cwd=cwd,
        check=False,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True,
        encoding="utf-8",
        errors="replace",
    )
    if completed.returncode != 0:
        detail = "\n".join(part for part in (completed.stdout.strip(), completed.stderr.strip()) if part)
        raise VerificationError(f"Command failed ({completed.returncode}): {' '.join(command)}\n{detail}")
    return completed.stdout.strip()


def load_lock() -> dict[str, Any]:
    try:
        lock = json.loads(LOCK_PATH.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        raise VerificationError(f"Unable to read {LOCK_PATH}: {error}") from error
    if lock.get("schemaVersion") != 1:
        raise VerificationError(f"Unsupported upstream lock schema: {lock.get('schemaVersion')!r}")
    return lock


def normalize_repository_url(value: str) -> str:
    return value.strip().removesuffix("/").removesuffix(".git").lower()


def verify_sources(lock: dict[str, Any], verify_tags: bool) -> dict[str, Any]:
    source_results: dict[str, Any] = {}
    packaged_licenses = {
        "mediaInfoLib": REPOSITORY_ROOT
        / "app"
        / "src"
        / "main"
        / "assets"
        / "licenses"
        / "MediaInfoLib-LICENSE.txt",
        "zenLib": REPOSITORY_ROOT
        / "app"
        / "src"
        / "main"
        / "assets"
        / "licenses"
        / "ZenLib-LICENSE.txt",
    }
    if set(lock["sources"]) != set(packaged_licenses):
        raise VerificationError("Source-lock entries do not match the packaged upstream licenses")
    for source_name, source in lock["sources"].items():
        relative_path = source["submodulePath"]
        source_path = REPOSITORY_ROOT / relative_path
        if not source_path.is_dir():
            raise VerificationError(
                f"Missing submodule {relative_path}; run git submodule update --init --recursive"
            )

        expected_commit = source["commit"].lower()
        worktree_commit = run(["git", "rev-parse", "HEAD"], cwd=source_path).lower()
        if worktree_commit != expected_commit:
            raise VerificationError(
                f"{source_name} worktree is {worktree_commit}, expected {expected_commit}"
            )

        index_line = run(["git", "ls-files", "--stage", "--", relative_path])
        index_fields = index_line.split()
        if len(index_fields) < 2 or index_fields[0] != "160000" or index_fields[1].lower() != expected_commit:
            raise VerificationError(
                f"Root gitlink for {relative_path} does not pin {expected_commit}: {index_line!r}"
            )

        status = run(["git", "status", "--porcelain", "--untracked-files=no"], cwd=source_path)
        if status:
            raise VerificationError(f"Pinned submodule has local modifications: {relative_path}\n{status}")

        origin = run(["git", "remote", "get-url", "origin"], cwd=source_path)
        if normalize_repository_url(origin) != normalize_repository_url(source["repository"]):
            raise VerificationError(
                f"{source_name} origin {origin!r} does not match {source['repository']!r}"
            )

        modules_url = run(
            ["git", "config", "-f", ".gitmodules", "--get", f"submodule.{relative_path}.url"]
        )
        if normalize_repository_url(modules_url) != normalize_repository_url(source["repository"]):
            raise VerificationError(
                f".gitmodules URL for {relative_path} does not match the source lock"
            )

        license_path = source_path / source["licensePath"]
        if not license_path.is_file():
            raise VerificationError(f"Missing upstream license file: {license_path}")
        packaged_license = packaged_licenses[source_name]
        if not packaged_license.is_file():
            raise VerificationError(f"Missing APK license notice: {packaged_license}")
        if normalized_text_bytes(packaged_license) != normalized_text_bytes(license_path):
            raise VerificationError(
                f"Packaged license does not exactly match {source_name} {source['tag']}: "
                f"{packaged_license}"
            )

        if verify_tags:
            tag_commit = run(
                ["git", "rev-parse", f"refs/tags/{source['tag']}^{{commit}}"],
                cwd=source_path,
            ).lower()
            if tag_commit != expected_commit:
                raise VerificationError(
                    f"{source_name} tag {source['tag']} resolves to {tag_commit}, expected {expected_commit}"
                )

        source_results[source_name] = {
            "tag": source["tag"],
            "commit": expected_commit,
            "repository": source["repository"],
            "license": source["license"],
        }

    return source_results


def resolve_ndk(lock: dict[str, Any], argument: str | None) -> Path:
    candidates: list[Path] = []
    if argument:
        candidates.append(Path(argument))
    for variable in ("ANDROID_NDK_ROOT", "ANDROID_NDK_HOME"):
        if os.environ.get(variable):
            candidates.append(Path(os.environ[variable]))
    sdk_root = os.environ.get("ANDROID_SDK_ROOT") or os.environ.get("ANDROID_HOME")
    if sdk_root:
        candidates.append(Path(sdk_root) / "ndk" / lock["toolchain"]["androidNdk"])

    for candidate in candidates:
        if candidate.is_dir():
            return candidate.resolve()
    rendered = ", ".join(str(candidate) for candidate in candidates) or "no candidates"
    raise VerificationError(f"Unable to locate Android NDK {lock['toolchain']['androidNdk']}: {rendered}")


def resolve_llvm_tool(ndk: Path, name: str) -> Path:
    executable_name = f"{name}.exe" if os.name == "nt" else name
    matches = sorted((ndk / "toolchains" / "llvm" / "prebuilt").glob(f"*/bin/{executable_name}"))
    if not matches:
        fallback = shutil.which(name)
        if fallback:
            return Path(fallback)
        raise VerificationError(f"Unable to find {name} below {ndk}")
    return matches[0]


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def normalized_text_bytes(path: Path) -> bytes:
    return path.read_bytes().replace(b"\r\n", b"\n")


def verify_library(
    abi: str,
    library: Path,
    readelf: Path,
    nm: Path,
    minimum_page_size: int,
) -> dict[str, Any]:
    if abi not in EXPECTED_MACHINE_TEXT:
        raise VerificationError(f"Unsupported ABI in verification request: {abi}")
    if not library.is_file():
        raise VerificationError(f"Missing {abi} library: {library}")

    elf_header = run([str(readelf), "-h", str(library)])
    machine_match = re.search(r"^\s*Machine:\s*(.+)$", elf_header, re.MULTILINE)
    class_match = re.search(r"^\s*Class:\s*(\S+)$", elf_header, re.MULTILINE)
    machine = machine_match.group(1).strip() if machine_match else ""
    elf_class = class_match.group(1).strip() if class_match else ""
    if EXPECTED_MACHINE_TEXT[abi] not in machine:
        raise VerificationError(f"{abi} artifact has unexpected machine {machine!r}: {library}")
    if elf_class != EXPECTED_ELF_CLASS[abi]:
        raise VerificationError(f"{abi} artifact has unexpected ELF class {elf_class!r}: {library}")

    program_headers = run([str(readelf), "-lW", str(library)])
    load_alignments = [
        int(match.group(1), 16)
        for match in re.finditer(r"^\s*LOAD\s+.*\s+(0x[0-9a-fA-F]+)\s*$", program_headers, re.MULTILINE)
    ]
    if not load_alignments:
        raise VerificationError(f"No ELF LOAD segments found in {library}")
    misaligned = [alignment for alignment in load_alignments if alignment < minimum_page_size]
    if misaligned:
        rendered = ", ".join(hex(value) for value in load_alignments)
        raise VerificationError(
            f"{abi} LOAD alignment is below {hex(minimum_page_size)}: {rendered}"
        )

    dynamic = run([str(readelf), "-d", str(library)])
    dependencies = set(re.findall(r"\(NEEDED\).*\[([^\]]+)\]", dynamic))
    if dependencies != ALLOWED_ANDROID_DEPENDENCIES:
        raise VerificationError(
            f"{abi} DT_NEEDED mismatch: {sorted(dependencies)}; "
            f"expected {sorted(ALLOWED_ANDROID_DEPENDENCIES)}"
        )
    soname_match = re.search(r"\(SONAME\).*\[([^\]]+)\]", dynamic)
    if soname_match is None or soname_match.group(1) != "libmediainfo.so":
        raise VerificationError(f"{abi} SONAME is not libmediainfo.so: {library}")

    symbols_output = run([str(nm), "-D", "--defined-only", str(library)])
    symbols = {
        line.split()[-1].split("@", 1)[0]
        for line in symbols_output.splitlines()
        if line.split()
    }
    if symbols != {"JNI_OnLoad"}:
        raise VerificationError(f"{abi} exported symbols are {sorted(symbols)}, expected only JNI_OnLoad")

    sibling_names = {path.name for path in library.parent.glob("*.so")}
    forbidden_siblings = sibling_names.intersection({"libc++_shared.so", "libzen.so"})
    if forbidden_siblings:
        raise VerificationError(f"{abi} contains forbidden shared dependencies: {sorted(forbidden_siblings)}")

    return {
        "path": str(library.resolve()),
        "bytes": library.stat().st_size,
        "sha256": sha256(library),
        "machine": machine,
        "elfClass": elf_class,
        "loadAlignments": [hex(value) for value in load_alignments],
        "needed": sorted(dependencies),
        "exports": sorted(symbols),
    }


def parse_library_argument(value: str) -> tuple[str, Path]:
    if "=" not in value:
        raise argparse.ArgumentTypeError("library must use ABI=PATH")
    abi, raw_path = value.split("=", 1)
    if not abi or not raw_path:
        raise argparse.ArgumentTypeError("library must use ABI=PATH")
    return abi, Path(raw_path)


def verify_apks(
    apk_directory: Path,
    lock: dict[str, Any],
    libraries: dict[str, Path],
) -> dict[str, Any]:
    if not apk_directory.is_dir():
        raise VerificationError(f"APK output directory does not exist: {apk_directory}")

    apks = sorted(apk_directory.glob("*.apk"))
    if len(apks) != 5:
        raise VerificationError(f"Expected five APKs in {apk_directory}, found {[path.name for path in apks]}")

    abis = list(lock["toolchain"]["abis"])
    expected_assets = {
        "assets/mediainfo-upstream.lock.json": LOCK_PATH,
        "assets/licenses/MediaInfoLib-LICENSE.txt": (
            REPOSITORY_ROOT / "app" / "src" / "main" / "assets" / "licenses" / "MediaInfoLib-LICENSE.txt"
        ),
        "assets/licenses/ZenLib-LICENSE.txt": (
            REPOSITORY_ROOT / "app" / "src" / "main" / "assets" / "licenses" / "ZenLib-LICENSE.txt"
        ),
    }

    selected: dict[str, tuple[Path, list[str]]] = {}
    for abi in abis:
        matches = [path for path in apks if f"-{abi}-" in path.name]
        if len(matches) != 1:
            raise VerificationError(f"Expected one {abi} APK, found {[path.name for path in matches]}")
        selected[abi] = (matches[0], [abi])
    universal_matches = [path for path in apks if "-universal-" in path.name]
    if len(universal_matches) != 1:
        raise VerificationError(
            f"Expected one universal APK, found {[path.name for path in universal_matches]}"
        )
    selected["universal"] = (universal_matches[0], abis)
    if {path for path, _ in selected.values()} != set(apks):
        raise VerificationError("APK directory contains an unexpected split name")

    report: dict[str, Any] = {}
    for variant_name, (apk, packaged_abis) in selected.items():
        with zipfile.ZipFile(apk) as archive:
            corrupt_entry = archive.testzip()
            if corrupt_entry is not None:
                raise VerificationError(f"APK CRC validation failed for {apk}: {corrupt_entry}")

            names = set(archive.namelist())
            native_entries = sorted(
                name for name in names if name.startswith("lib/") and name.endswith(".so")
            )
            expected_native_entries = sorted(
                f"lib/{abi}/libmediainfo.so" for abi in packaged_abis
            )
            if native_entries != expected_native_entries:
                raise VerificationError(
                    f"{apk.name} native entries are {native_entries}, expected {expected_native_entries}"
                )

            for archive_path, source_path in expected_assets.items():
                if archive_path not in names:
                    raise VerificationError(f"{apk.name} is missing {archive_path}")
                if archive.read(archive_path) != source_path.read_bytes():
                    raise VerificationError(f"{apk.name} contains a stale copy of {archive_path}")

            native_hashes: dict[str, str] = {}
            for abi in packaged_abis:
                archive_path = f"lib/{abi}/libmediainfo.so"
                archive_hash = hashlib.sha256(archive.read(archive_path)).hexdigest()
                native_hashes[abi] = archive_hash
                if abi in libraries and archive_hash != sha256(libraries[abi]):
                    raise VerificationError(
                        f"{apk.name} {abi} library does not match the audited stripped artifact"
                    )

        report[variant_name] = {
            "path": str(apk.resolve()),
            "bytes": apk.stat().st_size,
            "sha256": sha256(apk),
            "abis": packaged_abis,
            "nativeSha256": native_hashes,
            "metadata": sorted(expected_assets),
        }
    return report


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ndk", help="Path to the pinned Android NDK")
    parser.add_argument(
        "--library-root",
        type=Path,
        help="Directory containing <abi>/libmediainfo.so for every locked ABI",
    )
    parser.add_argument(
        "--library",
        action="append",
        default=[],
        type=parse_library_argument,
        metavar="ABI=PATH",
        help="Verify one explicitly located library; may be repeated",
    )
    parser.add_argument(
        "--apk-dir",
        type=Path,
        help="Directory containing four ABI split APKs and one universal APK",
    )
    parser.add_argument(
        "--skip-tags",
        action="store_true",
        help="Skip local upstream tag-to-commit checks (commit pins are still mandatory)",
    )
    args = parser.parse_args()

    try:
        lock = load_lock()
        report: dict[str, Any] = {
            "lock": str(LOCK_PATH.relative_to(REPOSITORY_ROOT)),
            "sources": verify_sources(lock, verify_tags=not args.skip_tags),
            "artifacts": {},
            "apks": {},
        }

        libraries: dict[str, Path] = dict(args.library)
        if args.library_root is not None:
            for abi in lock["toolchain"]["abis"]:
                libraries[abi] = args.library_root / abi / "libmediainfo.so"

        if libraries:
            ndk = resolve_ndk(lock, args.ndk)
            readelf = resolve_llvm_tool(ndk, "llvm-readelf")
            nm = resolve_llvm_tool(ndk, "llvm-nm")
            minimum_page_size = int(lock["toolchain"]["elfMaxPageSize"])
            if args.library_root is not None and set(libraries) != set(lock["toolchain"]["abis"]):
                raise VerificationError("Full library-root verification must cover exactly the four locked ABIs")
            report["artifacts"] = {
                abi: verify_library(abi, path, readelf, nm, minimum_page_size)
                for abi, path in sorted(libraries.items())
            }

        if args.apk_dir is not None:
            report["apks"] = verify_apks(args.apk_dir, lock, libraries)

        print(json.dumps(report, ensure_ascii=False, indent=2, sort_keys=True))
        return 0
    except VerificationError as error:
        print(f"native verification failed: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
