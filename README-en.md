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
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
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

The AutoJs6 MediaInfo Plugin provides MediaInfo-powered media metadata reading for AutoJs6. It can produce full reports, query individual parameters, and output structured JSON snapshots.

******

### Features

******

- Provides the `mediainfo` plugin service with plugin ID `mediainfo`.
- Supports `mediainfo.read(path, options)` and `mediainfo.get(path, streamKind, parameter, options)` in the AutoJs6 Node environment.
- Supports host discovery and invocation through `org.autojs.plugin.MEDIAINFO`, with `inform`, `get`, and `snapshot` exposed through the underlying AIDL interface.
- Bundles `libmediainfo.so` for `arm64-v8a`, `armeabi-v7a`, `x86_64`, and `x86`.
- Plugin metadata, usage instructions, README, and changelog are localized for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese.

******

### Usage

******

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);

  const format = await mediainfo.get("sample.mp4", "general", "Format");
  console.log(format);
})();
```

The path must point to a media file the host can access. In the Node environment, relative paths inside the working directory are supported.

******

### Stream Kinds

******

Supported MediaInfo stream kinds include:

```text
general, video, audio, text, other,
image, menu
```

`streamKind` in `mediainfo.get()` is case-insensitive and maps to native MediaInfo stream kinds.

******

### Snapshot Options

******

- `includeInform`: include the MediaInfo text report in the snapshot, default `true`.
- `includeSections`: parse the report into `sections`, default `true`.

******

### Release History

******

# v1.0.0

###### 2026/07/15

* `Feature` Added the MediaInfo plugin service with plugin ID `mediainfo` and engine `mediainfo`
* `Feature` Added host discovery and invocation through `org.autojs.plugin.MEDIAINFO`
* `Feature` Added `inform`, `get`, and `snapshot` capabilities for full media reports, single parameter lookup, and structured JSON snapshots
* `Feature` Bundled `libmediainfo.so` for `arm64-v8a`, `armeabi-v7a`, `x86_64`, and `x86`, with a `universal` APK variant
* `Feature` Added supported ABI metadata to plugin runtime info and release APK filenames with version, ABI variant, and CRC32 digest
* `Feature` Added localized plugin metadata, usage instructions, README, and changelog for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese

##### For more release history

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-en.md)

******

### Build

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release build:

```powershell
.\gradlew.bat :app:assembleRelease
```

Build parameters come from `version.properties`; the current minimum SDK is 24 and target SDK is 36.

******

### Resource Layout

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contains localized plugin descriptions and error messages; `plugin_instruction.md` contains usage instructions displayed by the host. README and CHANGELOG files are generated from JSON sources by `.python/generate_markdown.py`.

******

### Links

******

- AutoJs6 MediaInfo documentation: https://docs.autojs6.com/#/mediainfo
- MediaInfo official project: https://mediaarea.net/en/MediaInfo
- MediaInfoLib official project: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android project: https://github.com/olegazyx/MediaInfoLib-android
