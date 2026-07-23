<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用於讀取媒體文件信息的 MediaInfo 插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### 簡介

******

AutoJs6 MediaInfo 插件為 AutoJs6 提供基於 MediaInfo 的媒體元數據讀取能力, 可生成完整報告, 查詢單個參數, 並輸出結構化 JSON 快照.

******

### 功能

******

- 提供 `mediainfo` 插件服務, 插件 ID 為 `mediainfo`.
- 支援 AutoJs6 Node 環境中的 `mediainfo.read(path, options)` 和 `mediainfo.get(path, streamKind, parameter, options)`.
- 支援通過 `org.autojs.plugin.MEDIAINFO` 發現並調用插件, 底層 AIDL 暴露 `inform`/`get`/`snapshot`.
- 內置 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`.
- 插件信息, 使用說明, README 與 CHANGELOG 均支援西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體.

******

### 使用示例

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

路徑應為宿主允許存取的媒體文件路徑, Node 環境可使用工作目錄內的相對路徑.

******

### 流類型

******

支援的 MediaInfo 流類型包括:

```text
general, video, audio, text, other,
image, menu
```

`mediainfo.get()` 的 `streamKind` 不區分大小寫, 會映射到 MediaInfo 原生流類型.

******

### 快照選項

******

- `includeInform`: 是否在快照中包含 MediaInfo 文本報告, 預設 `true`.
- `includeSections`: 是否解析報告並寫入 `sections`, 預設 `true`.

******

### 發行歷史

******

# v1.0.0

###### 2026/07/15

* `新增` MediaInfo 插件服務, 插件 ID 為 `mediainfo`, 引擎為 `mediainfo`
* `新增` 支援通過 `org.autojs.plugin.MEDIAINFO` 發現並調用插件
* `新增` 支援 `inform`/`get`/`snapshot` 能力, 可生成完整媒體報告, 查詢單個參數, 輸出結構化 JSON 快照
* `新增` 內置 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`, 並支援 `universal` 通用包
* `新增` 插件運行時信息上報支援的 ABI 列表, 發布 APK 文件名包含版本號/ABI 變體和 CRC32 摘要
* `新增` 插件信息, 使用說明, README 與 CHANGELOG 的多語言資源: 西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 構建:

```powershell
.\gradlew.bat :app:assembleRelease
```

構建參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36.

******

### 資源結構

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件描述和錯誤信息本地化; `plugin_instruction.md` 提供宿主側展示的插件使用說明. README 與 CHANGELOG 由 `.python/generate_markdown.py` 根據 JSON 源文件生成.

******

### 相關連結

******

- AutoJs6 MediaInfo 文件: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方項目: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 官方項目: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 項目: https://github.com/olegazyx/MediaInfoLib-android
