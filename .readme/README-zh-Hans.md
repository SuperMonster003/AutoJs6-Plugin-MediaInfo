<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用于读取媒体文件信息的 MediaInfo 插件</p>

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

### 语言 (Languages)

******

当前 README.md 支持以下语言:

- 简体中文 [zh-Hans] # 当前
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### 简介

******

AutoJs6 MediaInfo 插件为 AutoJs6 提供基于 MediaInfo 的媒体元数据读取能力, 可生成完整报告, 查询单个参数, 并输出结构化 JSON 快照.

******

### 功能

******

- 提供 `mediainfo` 插件服务, 插件 ID 为 `mediainfo`.
- 支持 AutoJs6 Node 环境中的 `mediainfo.read(path, options)` 和 `mediainfo.get(path, streamKind, parameter, options)`.
- 支持通过 `org.autojs.plugin.MEDIAINFO` 发现并调用插件, 底层 AIDL 暴露 `inform`/`get`/`snapshot`.
- 内置 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`.
- 插件信息, 使用说明, README 与 CHANGELOG 均支持西班牙语/法语/俄语/阿拉伯语/日语/韩语/英语/简体中文/香港繁体/台湾繁体.

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

路径应为宿主允许访问的媒体文件路径, Node 环境可使用工作目录内的相对路径.

******

### 流类型

******

支持的 MediaInfo 流类型包括:

```text
general, video, audio, text, other,
image, menu
```

`mediainfo.get()` 的 `streamKind` 不区分大小写, 会映射到 MediaInfo 原生流类型.

******

### 快照选项

******

- `includeInform`: 是否在快照中包含 MediaInfo 文本报告, 默认 `true`.
- `includeSections`: 是否解析报告并写入 `sections`, 默认 `true`.

******

### 发行历史

******

# v1.0.0

###### 2026/07/15

* `新增` MediaInfo 插件服务, 插件 ID 为 `mediainfo`, 引擎为 `mediainfo`
* `新增` 支持通过 `org.autojs.plugin.MEDIAINFO` 发现并调用插件
* `新增` 支持 `inform`/`get`/`snapshot` 能力, 可生成完整媒体报告, 查询单个参数, 输出结构化 JSON 快照
* `新增` 内置 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`, 并支持 `universal` 通用包
* `新增` 插件运行时信息上报支持的 ABI 列表, 发布 APK 文件名包含版本号/ABI 变体和 CRC32 摘要
* `新增` 插件信息, 使用说明, README 与 CHANGELOG 的多语言资源: 西班牙语/法语/俄语/阿拉伯语/日语/韩语/英语/简体中文/香港繁体/台湾繁体

##### 更多发行历史可参阅

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-zh-Hans.md)

******

### 构建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 构建:

```powershell
.\gradlew.bat :app:assembleRelease
```

构建参数来自 `version.properties`, 当前最低 SDK 为 24, 目标 SDK 为 36.

******

### 资源结构

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供插件描述和错误信息本地化; `plugin_instruction.md` 提供宿主侧展示的插件使用说明. README 与 CHANGELOG 由 `.python/generate_markdown.py` 根据 JSON 源文件生成.

******

### 相关链接

******

- AutoJs6 MediaInfo 文档: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方项目: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 官方项目: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 项目: https://github.com/olegazyx/MediaInfoLib-android
