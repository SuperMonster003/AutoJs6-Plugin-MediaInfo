#!/usr/bin/env bash
set -euo pipefail

readonly app_package="io.github.supermonster003.autojs6.plugin.mediainfo"
readonly test_package="${app_package}.test"
readonly release_abi="${MEDIAINFO_RELEASE_SMOKE_ABI:-x86_64}"
readonly release_apk="app/build/outputs/apk/release/app-${release_abi}-release.apk"
readonly test_apk="app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
readonly release_test_class="${app_package}.MediainfoReleaseSmokeTest"
readonly instrumentation_runner="${test_package}/androidx.test.runner.AndroidJUnitRunner"

cleanup_release_smoke() {
  adb uninstall "${test_package}" >/dev/null 2>&1 || true
  adb uninstall "${app_package}" >/dev/null 2>&1 || true
}

trap cleanup_release_smoke EXIT

chmod +x gradlew
./gradlew -Pmediainfo.releaseSmokeDebugSigning=true :app:assembleRelease :app:assembleDebugAndroidTest --stacktrace

test -f "${release_apk}"
test -f "${test_apk}"
cleanup_release_smoke

adb install -t "${release_apk}"
adb install -t "${test_apk}"

if ! release_output="$(
  adb shell am instrument -w -r \
    -e class "${release_test_class}" \
    "${instrumentation_runner}" 2>&1
)"; then
  printf '%s\n' "${release_output}"
  exit 1
fi
printf '%s\n' "${release_output}"
grep -Fq 'OK (1 test)' <<<"${release_output}"

cleanup_release_smoke
./gradlew :app:connectedDebugAndroidTest --stacktrace
