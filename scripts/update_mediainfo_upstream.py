#!/usr/bin/env python3
"""Fetch pinned MediaArea tags or update the source lock to latest stable releases."""

from __future__ import annotations

import argparse
from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
from typing import Any
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen


REPOSITORY_ROOT = Path(__file__).resolve().parents[1]
LOCK_PATH = REPOSITORY_ROOT / "native" / "upstream.lock.json"
GITHUB_API_VERSION = "2022-11-28"
TAG_PATTERN = re.compile(r"^v(?P<version>\d+(?:\.\d+){1,2})$")
COMMIT_PATTERN = re.compile(r"^[0-9a-f]{40}$")
PACKAGED_LICENSES = {
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


class UpstreamUpdateError(RuntimeError):
    pass


def run(command: list[str], *, cwd: Path = REPOSITORY_ROOT) -> str:
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
        detail = "\n".join(
            part for part in (completed.stdout.strip(), completed.stderr.strip()) if part
        )
        raise UpstreamUpdateError(
            f"Command failed ({completed.returncode}): {' '.join(command)}\n{detail}"
        )
    return completed.stdout.strip()


def load_lock() -> dict[str, Any]:
    try:
        lock = json.loads(LOCK_PATH.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as error:
        raise UpstreamUpdateError(f"Unable to read {LOCK_PATH}: {error}") from error
    if lock.get("schemaVersion") != 1:
        raise UpstreamUpdateError(f"Unsupported source-lock schema: {lock.get('schemaVersion')!r}")
    if set(lock.get("sources", {})) != set(PACKAGED_LICENSES):
        raise UpstreamUpdateError("Source-lock entries do not match the supported upstream projects")
    return lock


def validate_tag(tag: str) -> tuple[int, ...]:
    match = TAG_PATTERN.fullmatch(tag)
    if match is None:
        raise UpstreamUpdateError(f"Unsupported upstream release tag: {tag!r}")
    return tuple(int(component) for component in match.group("version").split("."))


def github_repository(source: dict[str, Any]) -> str:
    match = re.fullmatch(
        r"https://github\.com/(?P<owner>[^/]+)/(?P<repository>[^/]+?)(?:\.git)?",
        source["repository"],
    )
    if match is None:
        raise UpstreamUpdateError(f"Unsupported GitHub repository URL: {source['repository']!r}")
    return f"{match.group('owner')}/{match.group('repository')}"


def latest_stable_release(source: dict[str, Any]) -> str:
    repository = github_repository(source)
    request = Request(
        f"https://api.github.com/repos/{repository}/releases/latest",
        headers={
            "Accept": "application/vnd.github+json",
            "User-Agent": "AutoJs6-Plugin-MediaInfo-upstream-tracker",
            "X-GitHub-Api-Version": GITHUB_API_VERSION,
        },
    )
    token = os.environ.get("GITHUB_TOKEN")
    if token:
        request.add_header("Authorization", f"Bearer {token}")
    try:
        with urlopen(request, timeout=30) as response:
            payload = json.load(response)
    except (HTTPError, URLError, TimeoutError, json.JSONDecodeError) as error:
        raise UpstreamUpdateError(f"Unable to query the latest release for {repository}: {error}") from error

    if payload.get("draft") or payload.get("prerelease"):
        raise UpstreamUpdateError(f"GitHub latest endpoint returned a non-stable release for {repository}")
    tag = payload.get("tag_name")
    if not isinstance(tag, str):
        raise UpstreamUpdateError(f"GitHub latest endpoint returned no tag for {repository}")
    validate_tag(tag)
    return tag


def source_path(source: dict[str, Any]) -> Path:
    path = (REPOSITORY_ROOT / source["submodulePath"]).resolve()
    try:
        path.relative_to(REPOSITORY_ROOT.resolve())
    except ValueError as error:
        raise UpstreamUpdateError(f"Submodule path escapes the repository: {path}") from error
    if not path.is_dir():
        raise UpstreamUpdateError(
            f"Missing submodule {source['submodulePath']}; run git submodule update --init --recursive"
        )
    return path


def fetch_tag(source: dict[str, Any], tag: str) -> str:
    validate_tag(tag)
    path = source_path(source)
    tag_ref = f"refs/tags/{tag}"
    run(
        ["git", "fetch", "--force", "--depth=1", "origin", f"{tag_ref}:{tag_ref}"],
        cwd=path,
    )
    commit = run(["git", "rev-parse", f"{tag_ref}^{{commit}}"], cwd=path).lower()
    if COMMIT_PATTERN.fullmatch(commit) is None:
        raise UpstreamUpdateError(f"Tag {tag} resolved to an invalid commit: {commit!r}")
    return commit


def fetch_locked(lock: dict[str, Any]) -> dict[str, str]:
    commits: dict[str, str] = {}
    for name, source in lock["sources"].items():
        expected = source["commit"].lower()
        if COMMIT_PATTERN.fullmatch(expected) is None:
            raise UpstreamUpdateError(f"Invalid locked commit for {name}: {expected!r}")
        actual = fetch_tag(source, source["tag"])
        if actual != expected:
            raise UpstreamUpdateError(
                f"SECURITY: {name} tag {source['tag']} now resolves to {actual}, "
                f"but the source lock pins {expected}"
            )
        commits[name] = actual
    return commits


def write_lock(lock: dict[str, Any]) -> None:
    LOCK_PATH.write_text(
        json.dumps(lock, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
        newline="\n",
    )


def copy_license(name: str, source: dict[str, Any]) -> None:
    upstream_license = source_path(source) / source["licensePath"]
    if not upstream_license.is_file():
        raise UpstreamUpdateError(f"Missing upstream license for {name}: {upstream_license}")
    packaged_license = PACKAGED_LICENSES[name]
    packaged_license.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(upstream_license, packaged_license)


def apply_latest(lock: dict[str, Any]) -> dict[str, Any]:
    result: dict[str, Any] = {"changed": False, "sources": {}}
    changed_sources: list[str] = []

    for name, source in lock["sources"].items():
        locked_tag = source["tag"]
        latest_tag = latest_stable_release(source)
        locked_version = validate_tag(locked_tag)
        latest_version = validate_tag(latest_tag)
        if latest_version < locked_version:
            raise UpstreamUpdateError(
                f"Latest stable release for {name} ({latest_tag}) is older than the lock ({locked_tag})"
            )

        latest_commit = fetch_tag(source, latest_tag)
        locked_commit = source["commit"].lower()
        if latest_tag == locked_tag and latest_commit != locked_commit:
            raise UpstreamUpdateError(
                f"SECURITY: {name} tag {locked_tag} moved from {locked_commit} to {latest_commit}"
            )

        source_changed = latest_version > locked_version
        if source_changed:
            run(["git", "checkout", "--detach", latest_commit], cwd=source_path(source))
            source["tag"] = latest_tag
            source["commit"] = latest_commit
            copy_license(name, source)
            changed_sources.append(name)

        result["sources"][name] = {
            "previousTag": locked_tag,
            "tag": latest_tag,
            "commit": latest_commit,
            "changed": source_changed,
        }

    if changed_sources:
        lock["lockedAt"] = datetime.now(timezone.utc).date().isoformat()
        write_lock(lock)
        result["changed"] = True
        result["changedSources"] = changed_sources
    return result


def write_github_output(path: Path, result: dict[str, Any]) -> None:
    values = {
        "changed": str(result.get("changed", False)).lower(),
        "mediainfo_tag": result.get("sources", {}).get("mediaInfoLib", {}).get("tag", ""),
        "mediainfo_commit": result.get("sources", {}).get("mediaInfoLib", {}).get("commit", ""),
        "zenlib_tag": result.get("sources", {}).get("zenLib", {}).get("tag", ""),
        "zenlib_commit": result.get("sources", {}).get("zenLib", {}).get("commit", ""),
    }
    with path.open("a", encoding="utf-8", newline="\n") as stream:
        for name, value in values.items():
            if "\n" in value or "\r" in value:
                raise UpstreamUpdateError(f"Unsafe multiline GitHub output for {name}")
            stream.write(f"{name}={value}\n")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    mode = parser.add_mutually_exclusive_group(required=True)
    mode.add_argument(
        "--fetch-locked",
        action="store_true",
        help="Fetch every locked tag and reject any tag-to-commit mismatch",
    )
    mode.add_argument(
        "--apply-latest",
        action="store_true",
        help="Move pins only when GitHub reports a newer stable release",
    )
    parser.add_argument(
        "--github-output",
        type=Path,
        help="Append machine-readable fields to a GitHub Actions output file",
    )
    args = parser.parse_args()

    try:
        lock = load_lock()
        if args.fetch_locked:
            commits = fetch_locked(lock)
            result: dict[str, Any] = {
                "changed": False,
                "sources": {
                    name: {
                        "tag": lock["sources"][name]["tag"],
                        "commit": commit,
                        "changed": False,
                    }
                    for name, commit in commits.items()
                },
            }
        else:
            result = apply_latest(lock)
        if args.github_output is not None:
            write_github_output(args.github_output, result)
        print(json.dumps(result, ensure_ascii=False, indent=2))
        return 0
    except UpstreamUpdateError as error:
        print(f"UPSTREAM_UPDATE_ERROR: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
