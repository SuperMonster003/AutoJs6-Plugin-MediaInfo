# MediaInfo device screenshots

These six unedited PNGs were captured on 2026-09-10 from AutoJs6 on the
replacement Samsung SM-A566B rental at `localhost:54105`: Android 16 / API 36,
ARM64, kernel page size 16384 bytes. Each theme includes the running Rhino
example, the media file information dialog and the full MediaInfo report.

The sample is the generated silent two-track Matroska file in
`app/src/androidTest/assets/mediainfo-two-audio.mka`. The runnable source is
[`demo/mediainfo.js`](../../demo/mediainfo.js). Copy the fixture to
`/sdcard/two-audio.mka` before running the script. It prints 8000 Hz / one channel
for the first track and 16000 Hz / two channels for the second track.

Capture used AutoJs6 6.8.0 (5279), MediaInfo 2.1.0 (12) Release and Node Runtime
1.4.0 (154). The host's `MediaInfoDialogInstrumentationTest` injects a real touch
at the neutral button, waits for `DisplayMediaInfoActivity`, and verifies the
displayed `Complete name` against the source path in both themes. It restores
the previous AutoJs6 theme settings afterward. To repeat the capture with the
host test APK installed:

```shell
adb -s <serial> push app/src/androidTest/assets/mediainfo-two-audio.mka /sdcard/two-audio.mka
adb -s <serial> shell mkdir -p /sdcard/Download/MediaInfo-demo
adb -s <serial> push demo/mediainfo.js /sdcard/Download/MediaInfo-demo/mediainfo.js
adb -s <serial> shell am instrument -w -r \
  -e class org.autojs.autojs.ui.main.scripts.MediaInfoDialogInstrumentationTest \
  -e mediainfoDemoPath /sdcard/two-audio.mka \
  -e mediainfoDemoScript /storage/emulated/0/Download/MediaInfo-demo/mediainfo.js \
  -e mediainfoScreenshots true \
  org.autojs.autojs6.test/androidx.test.runner.AndroidJUnitRunner
adb -s <serial> pull /sdcard/Android/data/org.autojs.autojs6/files/mediainfo-ui-evidence
```

AutoJs6 needs file access for this external fixture. Complete any first-launch
system dialogs before capturing. The Samsung developer pointer overlay was
disabled for these screenshots. The replacement RDB endpoint became unreachable
after all tests and image transfers; restoring that device setting and removing
the temporary test packages/files could not be confirmed.

The host currently contains three legacy 4 KiB-aligned terminal libraries and
Android displayed its page-size compatibility warning on first launch. That
warning was acknowledged before the touch-based capture; compatibility mode was
not disabled. The MediaInfo plugin's native library is independently 16 KiB
aligned. The warning screenshot, exact APK hashes, host library inventory and
separate records for both rental sessions are retained in
[`2026-09-10-mediainfo-main-integration.json`](../../benchmark/results/2026-09-10-mediainfo-main-integration.json).

The generated README uses HTML `picture` sources to select the dark images when
`prefers-color-scheme: dark` matches, with light images as the fallback.
