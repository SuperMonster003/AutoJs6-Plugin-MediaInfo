<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>MediaInfo plugin for reading media file information</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Languages

******

The current README.md supports the following languages:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- English [en] # current
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### Introduction

******

The MediaInfo Plugin brings media file inspection to AutoJs6. Once installed, a single line of script fetches hundreds of technical parameters of video, audio, and image files, such as container format, codec, duration, resolution, bit rate, and channels, and the media info dialog in the AutoJs6 file list shows the full parsed report powered by this plugin. Parsing comes from MediaInfoLib, the same open-source library behind the desktop MediaInfo application.

The plugin runs in its own process and is discovered by AutoJs6 automatically with zero configuration. To read a file, the host hands the media content to the plugin as a read-only file descriptor. Seekable regular descriptors are read directly by MediaInfoLib through `/proc/self/fd`; non-seekable descriptors such as pipes, or unsuccessful direct parses, fall back to a temporary copy in the private cache that is deleted as soon as the call ends. No network access is involved at any point, and no sensitive system permission is requested.

******

### Features

******

- Works out of the box: no configuration required; AutoJs6 discovers the plugin automatically, and both scripts and the media info dialog in the file list can use it right away.
- Comprehensive details: container format, codec, duration, resolution, frame rate, bit rate, channels, sampling rate, and more, all in one call.
- Three reading modes: full text report (`inform`), single parameter lookup (`get`), and structured JSON snapshot (`read`/`snapshot`), whichever fits the job.
- Two script engines: the Node environment calls it asynchronously via `require("mediainfo")`; the Rhino environment uses the global `mediainfo(path)` module that returns a property-accessible parsed object synchronously.
- Broad format coverage: parsing is powered by MediaInfoLib, the library behind the desktop MediaInfo, supporting a large number of common and niche video, audio, and image formats.
- Five APK flavors: four single-ABI packages (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) plus an all-in-one `universal` package, so each device installs only what it needs.
- Privacy friendly: parsing happens in an isolated process that only receives read-only file descriptors from the host, with no network or sensitive system permissions.
- Multilingual: plugin metadata, instructions, README, and changelog are available in 10 languages.

******

### Usage

******

1. Download the plugin APK matching the device from the [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) page and install it on the device running AutoJs6; when unsure, pick the `universal` package or see `Choosing an APK` below.
2. Open the AutoJs6 plugin center and confirm that the `MediaInfo` plugin is recognized and enabled.
3. Call the `mediainfo` module in scripts as shown in `Script API` below, or open the media info dialog of any media file in the AutoJs6 file list to view the full report directly.

> If the plugin does not show up in the plugin center, upgrade AutoJs6 to a recent version first (internal build 3923 or above). The plugin itself supports devices running Android 7.0 (API 24) and above.

******

### Choosing an APK

******

Each release ships 5 APKs that differ only in which native library architectures they bundle:

| Package | Best for |
|---|---|
| `arm64-v8a` | The vast majority of modern Android phones and tablets (64-bit ARM); the first choice |
| `armeabi-v7a` | Older 32-bit ARM devices |
| `x86_64` | 64-bit x86 emulators and a few x86 devices |
| `x86` | 32-bit x86 emulators and a few x86 devices |
| `universal` | Bundles all 4 architectures and is the largest; works on any device and is the safe pick when unsure |

The plugin loads the native library inside its own process. If a single-ABI package that does not match the device was installed by mistake, calls fail with `MediaInfo library is not available`; switching to the `universal` package resolves it.

******

### Script API

******

In the Node environment (scripts starting with the `"nodejs"` directive), obtain the module via `require("mediainfo")`; every method returns a Promise:

```javascript
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4");
  console.log(snapshot.sections.general[0].format);
  console.log(snapshot.sections.video[0].width);

  const duration = await mediainfo.get("sample.mp4", "general", "Duration");
  console.log(duration);
})();
```

`read(path, options?)` returns a structured snapshot object (see `Snapshot Structure and Options` below); `get(path, streamKind?, parameter)` returns the raw parameter text, with `streamKind` defaulting to `general`. For safety, Node scripts can only access files inside the project directory, and relative paths resolve against the project root.

In the Rhino environment (the default AutoJs6 script engine), `mediainfo` is a global module; `mediainfo(path)` and `mediainfo.read(path)` are equivalent and return a parsed object synchronously:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

On the returned object, `path` and `inform` hold the resolved path and the full text report; each stream kind (such as `general`, `video`, `audio`) works both as a property exposing parsed fields (such as `mi.video.width`, field names in camelCase) and as a function for live raw parameter queries (such as `mi.audio("BitRate")`). Rhino scripts may access any path the host is allowed to read.

******

### Snapshot Structure and Options

******

The snapshot returned by `read()` in the Node environment looks like this (the JSON returned by the plugin AIDL `snapshot` method is identical except that its `schema` is `autojs6-plugin-mediainfo-snapshot-v1` and it has no `path` field):

```json
{
  "schema": "autojs6-node-mediainfo-snapshot-v1",
  "path": "sample.mp4",
  "fileName": "sample.mp4",
  "sizeBytes": 10485760,
  "inform": "General\nComplete name : sample.mp4\n...",
  "sections": {
    "general": [{ "format": "MPEG-4", "duration": "10 s 0 ms" }],
    "video": [{ "format": "AVC", "width": "1 920 pixels" }],
    "audio": [{ "format": "AAC LC", "channels": "2 channels" }]
  }
}
```

- `includeInform`: whether to include the `inform` text report, default `true`; set it to `false` to get an empty string and a smaller payload.
- `includeSections`: whether to parse the report into `sections`, default `true`; set it to `false` to get an empty object.

`sections` uses the lowercased report section names as keys (with multiple streams of one kind, section names may carry an index such as `audio #1`), and every value is an array of objects; field names are converted to camelCase while field values keep the original MediaInfo text, including units and thousands-separating spaces such as `1 920 pixels`.

******

### Stream Kinds

******

The `streamKind` parameter of `get()` supports the following stream kinds:

```text
general, video, audio, text, other, image, menu
```

`streamKind` is case-insensitive and maps to native MediaInfo stream kinds; querying a stream that does not exist or a parameter with no value returns an empty string.

******

### FAQ

******

#### How can I confirm the plugin is working?

Open the AutoJs6 plugin center; seeing the `MediaInfo` plugin there means the host has recognized it. Then run any of the `mediainfo` script examples; results coming back normally means the plugin works.

#### Why is there no plugin icon in the app list?

This is expected. The plugin has no standalone interface and creates no launcher icon; after installation it is discovered and driven entirely by AutoJs6 in the background, and every interaction happens inside AutoJs6.

#### A Node script fails with `path must stay inside the scoped working directory`?

For safety, the Node engine only allows access to files inside the project directory. Move or copy the media file into the project directory before reading it; to access other locations (such as the gallery or download folders), use a Rhino engine script instead.

#### `get()` returned an empty string?

The parameter name must be a native MediaInfo parameter (such as `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), and the target stream must actually exist. Use `read()` first to inspect the fields actually available in `sections`, or look at the full `inform` report.

#### Reading large files is slow?

Regular files are now parsed directly without a full-file cache copy, so large files avoid copy time that grows linearly with file size. Non-seekable descriptors such as pipes, or formats that fail direct parsing, still use a temporary copy whose fallback cost grows with the bytes received; MediaInfoLib's own analysis time remains format- and content-dependent.

#### Are parsing results cached, and what happens on timeout?

Yes. On Android 8.1 (API 27) and later, for an unchanged regular file with a stable identity, the plugin caches reports, queries, and snapshots in the current process: at most 32 files, 64 queries per file, a sliding 10-minute lifetime, and about 2 MiB of text in total. Caching is conservatively disabled on API 24-26 because nanosecond file timestamps are unavailable. The cache is cleared on low memory or process shutdown. Each AIDL call is limited to 30 seconds; on timeout, native parsing or fallback copying is cooperatively canceled, temporary files are deleted, and the exception contains `MEDIAINFO_TIMEOUT`.

#### The file has multiple audio tracks or subtitles; how do I read the second and later streams?

The snapshot `sections` keeps every section of the report (with multiple streams, section names carry an index such as `audio #2`), so read them from there; `get()` currently always queries the first stream of a kind, and stream index selection is planned in [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### Does the plugin access the network or request sensitive permissions?

No. Its manifest contains no network, storage, camera, or other sensitive system permissions; it only declares the plugin permission used to communicate with AutoJs6. Media content arrives from the host as a read-only descriptor, and the temporary copy made for parsing is deleted right away.

******

### Permissions and Security

******

Media files may come from untrusted sources, so the design puts several lines of defense around parsing:

- Process isolation: parsing happens in the plugin's own process and the native library is never injected into the host process, so even a parsing failure leaves AutoJs6 running normally.
- Minimal data surface: the plugin cannot read device storage by itself; it only receives a read-only file descriptor opened by the host plus a display name.
- Direct when possible, purge on fallback: seekable regular descriptors create no media copy; only the compatibility fallback writes to the private cache, and that temporary file is deleted as soon as the call finishes.
- Minimal permissions: no network, storage, camera, or other sensitive system permissions; the service and the wake entry are both guarded by the AutoJs6 plugin permission (`org.autojs.permission.PLUGIN`), so third-party apps cannot call them directly.
- Open and auditable: the plugin code, build scripts, and documentation pipeline are fully open source, and the origins of the native library and the JNI wrapper are stated in the license section.

Install the plugin only from the official [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) page or other trusted channels; packages from unknown origins may be tampered with even when the name and version number look identical.

******

### Plugin Interface

******

The following information targets AutoJs6 host and plugin developers; the host uses these identifiers to discover the plugin and negotiate capabilities:

```text
application id: io.github.supermonster003.autojs6.plugin.mediainfo
plugin id: mediainfo
engine: mediainfo
variant: default
discovery action: org.autojs.plugin.MEDIAINFO
discovery category: mediainfo
wake action: org.autojs.plugin.action.WAKE
binder interface: IMediainfoPlugin
minimum host build: 3923
native library: libmediainfo.so
snapshot schema: autojs6-plugin-mediainfo-snapshot-v1
```

`MediainfoPluginService` exposes four methods, `getInfo`/`inform`/`get`/`snapshot`, through the AIDL interface `IMediainfoPlugin`; media content is passed as a read-only `ParcelFileDescriptor` plus a display name, and `snapshot` additionally accepts a `Bundle` of options carrying `includeInform`/`includeSections`. Both the service and `WakeActivity` are guarded by the `org.autojs.permission.PLUGIN` permission.

The plugin scans the installed base / split APKs and dynamically reports the ABIs that actually contain `libmediainfo.so`; a single-ABI package reports only its ABI, while `universal` reports all 4. If APK paths cannot be read, it safely falls back to the current process bitness when an extracted native library is present.

******

### Roadmap

******

The plugin's planned capabilities and their completion status are maintained as a checkable list in ROADMAP.md, organized by milestones with acceptance criteria, covering stream index queries, copy-free parsing, dynamic ABI reporting, native library evolution, and continuous integration. Unchecked items are intentions rather than shipped capabilities; discussion via Issues is welcome.

- [View ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### Release History

******

#### v1.1.0

_2026/08/31_

- `Feature` Copy-free parsing: seekable regular file descriptors are read directly by MediaInfoLib through `/proc/self/fd`; only pipes or failed direct parses use a private temporary copy
- `Feature` Process-local result cache: on API 27+, full reports, field queries, and snapshots use stable file identity, LRU eviction, a sliding 10-minute lifetime, and low-memory cleanup to avoid repeated parsing
- `Feature` Cooperative cancellation and timeout: every AIDL call has a 30-second limit; timeout stops native parsing or fallback copying, releases temporary resources, and returns `MEDIAINFO_TIMEOUT`
- `Fix` Fixed avoidable whole-file copying for large regular descriptors and ensured descriptors and fallback files close on success, failure, validation errors, cancellation, and timeout
- `Fix` Fixed cache identity precision for rapid file changes by using nanosecond mtime / ctime and conservatively disabling cache reuse on API 24-26
- `Improvement` Supported ABI reporting is generated dynamically from the `libmediainfo.so` files actually present in installed base or split APKs, with a safe process-ABI fallback
- `Improvement` Hardened snapshot section parsing for repeated and numbered streams, malformed lines, embedded colons, duplicate fields, and independent output options
- `Improvement` Added reproducible synthetic benchmark and real-media validation tooling, with complete x86, x86_64, and ARM64 performance baselines
- `Improvement` Rebuilt the 10-language README, plugin instructions, and changelog generation pipeline with drift validation and GitHub Actions gates

#### v1.0.0

_2026/07/15_

- `Feature` First stable release: brings MediaInfoLib-powered media file inspection to AutoJs6, fetching container format, codec, duration, resolution, bit rate, channels, and more in a single call
- `Feature` Script API: the Node environment gets async `read`/`get` via `require("mediainfo")`; the Rhino environment gets the global `mediainfo(path)` module returning a property-accessible parsed object synchronously
- `Feature` Three reading capabilities: full text report (`inform`), single parameter lookup (`get`), and structured JSON snapshot (`snapshot`, schema `autojs6-plugin-mediainfo-snapshot-v1`)
- `Feature` Discovered automatically by AutoJs6 through `org.autojs.plugin.MEDIAINFO`; the plugin receives and parses media content in its own process via read-only file descriptors, requesting no network or sensitive system permissions
- `Feature` Ships four single-ABI packages (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) plus an all-architecture `universal` package, with release filenames carrying version, ABI, and CRC32 digest
- `Feature` Plugin metadata, instructions, README, and changelog cover 10 languages: Simplified Chinese, Hong Kong Traditional Chinese, Taiwan Traditional Chinese, English, French, Spanish, Japanese, Korean, Russian, and Arabic

##### For more release history

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-en.md)

******

### Build

******

This section targets developers who want to build the plugin from source.

Clone the repository together with both pinned official submodules before building:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

Build debug APKs:

```powershell
.\gradlew.bat :app:assembleDebug
```

Build release APKs (ABI splits are enabled, producing 4 single-ABI packages plus 1 `universal` package in one go; configure the untracked `sign.properties` for automatic signing):

```powershell
.\gradlew.bat :app:assembleRelease
```

For release archiving, run the `:app:appendDigestToReleasedFiles` task, which copies the APKs under `app/release` into `app/releases` and renames them to the `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk` pattern.

Build parameters are centralized in `version.properties`: minimum SDK 24 (Android 7.0), target SDK 36, current version 1.1.0.

******

### Localization and Docs Generation

******

```text
.readme/common.json
.readme/lang_*.json
.readme/template_readme.md
.readme/template_plugin_instruction.md
.changelog/lang_*.json
.changelog/template_changelog.md
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` holds the localized plugin description and error messages, and `plugin_instruction.md` holds the instructions displayed in the host plugin center. README, changelog, and instructions are all generated from JSON sources: edit the sources under `.readme/` and `.changelog/`, then run `py .python/generate_markdown.py` to regenerate every artifact; generated artifacts are never edited by hand. Run `py .python/generate_markdown.py --check` to verify sources and artifacts are in sync (CI checks this automatically as well).

******

### License

******

Project code is licensed under the [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). On the v2 source-build line, `libmediainfo.so` is built from the official [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) and [ZenLib](https://github.com/MediaArea/ZenLib) (zlib license) sources, while the compatibility JNI bridge is maintained in this repository. The frozen v1.1.0 binary provenance remains documented separately.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### Links

******

- AutoJs6 MediaInfo documentation: https://docs.autojs6.com/#/mediainfo
- MediaInfo official website: https://mediaarea.net/en/MediaInfo
- MediaInfoLib project: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android wrapper: https://github.com/olegazyx/MediaInfoLib-android
