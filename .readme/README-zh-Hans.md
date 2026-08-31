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
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
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

MediaInfo 插件 (MediaInfo Plugin) 为 AutoJs6 提供媒体文件信息读取能力. 安装后, 脚本只需一行代码即可获取视频, 音频, 图片等文件的容器格式, 编码, 时长, 分辨率, 码率, 声道等上百项技术参数, AutoJs6 文件列表中的媒体信息对话框也将由本插件提供完整解析报告. 解析能力来自与桌面端 MediaInfo 同源的开源库 MediaInfoLib.

插件运行在独立进程中, 由 AutoJs6 自动发现, 无需任何手动配置. 读取文件时, 宿主将媒体内容以只读文件描述符交给插件; 对可随机访问的常规文件描述符, 插件通过 `/proc/self/fd` 交由 MediaInfoLib 直接读取, 遇到管道等不可随机访问描述符或直读解析失败时, 才回退到私有缓存临时副本并在调用结束立即删除. 全程无需网络, 也不申请任何敏感系统权限.

******

### 功能亮点

******

- 开箱即用: 安装后无需任何配置, AutoJs6 自动发现插件, 脚本与文件列表的媒体信息对话框即可直接使用.
- 信息全面: 容器格式, 编码, 时长, 分辨率, 帧率, 码率, 声道, 采样率等技术参数一次调用全部获取.
- 三种读取方式: 完整文本报告 (`inform`), 单项参数查询 (`get`), 结构化 JSON 快照 (`read`/`snapshot`), 按需选用.
- 双引擎支持: Node 环境通过 `require("mediainfo")` 异步调用; Rhino 环境通过全局模块 `mediainfo(path)` 同步返回可属性访问的解析对象.
- 格式覆盖广: 解析能力来自与桌面端 MediaInfo 同源的 MediaInfoLib, 支持视频, 音频, 图片等大量常见与小众格式.
- 五种安装包: 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四种单架构包与包含全部架构的 `universal` 包, 按设备按需选择.
- 隐私友好: 插件在独立进程中解析, 仅接收宿主传入的只读文件描述符, 不申请网络与任何敏感系统权限.
- 多语言: 插件信息, 使用说明, README 与更新日志覆盖 10 种语言.

******

### 使用方法

******

1. 从 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 页面下载与设备匹配的插件 APK 并安装到运行 AutoJs6 的设备上; 拿不准选哪个时, 可直接选 `universal` 包, 或参考下方 `如何选择安装包`.
2. 打开 AutoJs6 的插件中心, 确认 `MediaInfo` 插件已被识别并处于启用状态.
3. 在脚本中按下方 `脚本 API` 的示例调用 `mediainfo` 模块; 也可以在 AutoJs6 文件列表中打开媒体文件的媒体信息对话框直接查看完整报告.

> 若插件中心未显示该插件, 请先将 AutoJs6 升级到较新版本 (内部版本号 3923 及以上). 插件自身支持 Android 7.0 (API 24) 及以上的设备.

******

### 如何选择安装包

******

每个发行版本包含 5 个 APK, 差别仅在于内置了哪些架构的原生库:

| 安装包 | 适用对象 |
|---|---|
| `arm64-v8a` | 绝大多数现代 Android 手机与平板 (64 位 ARM), 优先选择 |
| `armeabi-v7a` | 较早期的 32 位 ARM 设备 |
| `x86_64` | 64 位 x86 模拟器与少数 x86 设备 |
| `x86` | 32 位 x86 模拟器与少数 x86 设备 |
| `universal` | 内置全部 4 种架构, 体积最大; 适用于任何设备, 也是拿不准架构时的稳妥选择 |

插件在自身进程中加载原生库. 若误装了与设备架构不匹配的单架构包, 调用时会提示 MediaInfo 库不可用 (`MediaInfo library is not available`), 换装 `universal` 包即可解决.

******

### 脚本 API

******

Node 环境 (脚本首行声明 `"nodejs"`) 中通过 `require("mediainfo")` 获取模块, 全部方法返回 Promise:

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

`read(path, options?)` 返回结构化快照对象 (见下方 `快照结构与选项`); `get(path, streamKind?, parameter)` 返回参数原始文本, `streamKind` 缺省为 `general`. 出于安全限制, Node 脚本只能访问工程目录内的文件, 相对路径基于工程根目录解析.

Rhino 环境 (AutoJs6 默认脚本引擎) 中 `mediainfo` 为全局模块, `mediainfo(path)` 与 `mediainfo.read(path)` 等价, 同步返回解析对象:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返回对象上, `path` 与 `inform` 分别为解析后的路径与完整文本报告; 各流类型 (如 `general`, `video`, `audio`) 既可作为属性读取已解析字段 (如 `mi.video.width`, 字段名为 camelCase), 也可作为函数实时查询原始参数 (如 `mi.audio("BitRate")`). Rhino 脚本可访问宿主有权读取的任意路径.

******

### 快照结构与选项

******

Node 环境 `read()` 返回的快照结构如下 (插件 AIDL `snapshot` 方法返回的 JSON 与之一致, 但 `schema` 为 `autojs6-plugin-mediainfo-snapshot-v1` 且不含 `path` 字段):

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

- `includeInform`: 是否包含 `inform` 文本报告, 默认 `true`; 置为 `false` 时 `inform` 为空字符串, 可减小返回体积.
- `includeSections`: 是否解析报告并生成 `sections`, 默认 `true`; 置为 `false` 时 `sections` 为空对象.

`sections` 以报告小节名的小写形式作为键 (存在多条同类流时, 小节名可能带编号, 如 `audio #1`), 值一律为对象数组; 字段名转换为 camelCase, 字段值保留 MediaInfo 原始文本 (含单位与千位分隔空格, 如 `1 920 pixels`).

******

### 流类型

******

`get()` 的 `streamKind` 参数支持以下流类型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不区分大小写, 会映射到 MediaInfo 原生流类型; 查询不存在的流或无值参数时返回空字符串.

******

### 常见问题

******

#### 如何确认插件已经生效?

打开 AutoJs6 的插件中心, 能看到 `MediaInfo` 插件即表示宿主已识别. 随后运行任意 `mediainfo` 脚本示例, 能正常返回结果即说明插件工作正常.

#### 为什么应用列表里没有插件的图标?

这是正常现象. 插件没有独立界面, 也不在桌面创建启动图标, 安装后由 AutoJs6 在后台自动发现和调用, 全部交互都在 AutoJs6 内完成.

#### Node 脚本提示路径必须位于工作目录内 (path must stay inside the scoped working directory)?

Node 引擎出于安全限制只允许访问工程目录内的文件. 请将媒体文件放入工程目录内再读取; 若需要访问其他路径 (如相册或下载目录), 可改用 Rhino 引擎脚本.

#### `get()` 返回了空字符串?

参数名需使用 MediaInfo 原生参数 (如 `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), 且目标流需实际存在. 可先用 `read()` 查看 `sections` 中实际可用的字段, 或直接查看 `inform` 完整报告.

#### 读取大文件时比较慢?

常规文件现已免整文件复制直接解析, 大文件可避开与文件大小线性相关的复制开销. 管道等不可随机访问描述符或直读失败的格式仍会使用临时副本, 此回退路径耗时仍与传入字节数相关; MediaInfoLib 自身解析耗时则取决于格式与内容.

#### 解析结果会缓存吗, 超时如何处理?

会. 在 Android 8.1 (API 27) 及以上系统中, 对身份稳定且未变化的常规文件, 插件在当前进程内缓存报告, 查询与快照: 最多 32 个文件, 每个文件 64 个查询, 10 分钟滑动有效期, 文本总量约 2 MiB. API 24-26 因无法取得纳秒级文件时间戳而保守禁用缓存; 低内存或进程结束时也会清空. 每次 AIDL 调用上限为 30 秒; 超时会协作取消原生解析或中断回退复制, 删除临时文件, 并返回包含 `MEDIAINFO_TIMEOUT` 的异常.

#### 文件有多条音轨或字幕, 如何读取第二条及之后的流?

快照 `sections` 会完整保留报告中的全部小节 (多流时小节名带编号, 如 `audio #2`), 可直接从中读取; `get()` 目前固定查询同类流中的第 1 条, 指定流序号的能力已列入 [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### 插件会联网或申请敏感权限吗?

不会. 插件清单不含网络, 存储, 相机等任何敏感系统权限, 仅声明与 AutoJs6 通信所需的插件权限. 媒体内容由宿主以只读描述符传入, 解析产生的临时副本随即删除.

******

### 权限与安全

******

媒体文件可能来自不可信来源, 插件在设计上为解析过程设置了多道防线:

- 进程隔离: 解析在插件自身进程中完成, 原生库不注入宿主进程, 即使解析异常也不影响 AutoJs6 稳定运行.
- 最小数据面: 插件自身无法读取设备存储, 仅接收宿主打开的只读文件描述符与文件显示名.
- 能直读则直读, 回退即用即清: 可随机访问的常规描述符不产生媒体副本; 仅兼容回退写入私有缓存, 调用结束立即删除.
- 最小权限: 不申请网络, 存储, 相机等任何敏感系统权限; 服务与唤醒入口均受 AutoJs6 插件权限 (`org.autojs.permission.PLUGIN`) 保护, 第三方应用无法直接调用.
- 开源可审计: 插件代码, 构建脚本与文档生成链路全部开源, 原生库来源与 JNI 封装出处在许可章节明确标注.

请仅从官方 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 页面或其他可信渠道获取插件安装包; 来源不明的安装包即使名称与版本号相同, 也可能被篡改.

******

### 插件接口

******

以下信息面向 AutoJs6 宿主与插件开发者, 宿主通过这些标识发现插件并完成能力协商:

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

`MediainfoPluginService` 通过 AIDL 接口 `IMediainfoPlugin` 暴露 `getInfo`/`inform`/`get`/`snapshot` 四个方法; 媒体内容以只读 `ParcelFileDescriptor` 加显示名传参, `snapshot` 另接受包含 `includeInform`/`includeSections` 的 `Bundle` 选项. 服务与 `WakeActivity` 均受 `org.autojs.permission.PLUGIN` 权限保护.

插件扫描已安装的 base / split APK 中实际包含的 `libmediainfo.so` 并动态上报 ABI; 单架构包仅上报对应 ABI, `universal` 包上报全部 4 种. 若 APK 路径不可读, 则根据当前进程位数和已解压的原生库安全回退.

******

### 开发路线图

******

插件的能力规划与完成情况以可勾选清单维护在 ROADMAP.md 中, 按里程碑组织并附验收条件, 涵盖跨流查询, 免拷贝解析, 动态 ABI 上报, 原生库演进与持续集成等方向. 未勾选条目表示规划意向而非当前版本能力, 欢迎通过 Issues 参与讨论.

- [查看 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 发行历史

******

#### v2.0.0

_2026/08/31_

- `新增` 官方源码构建: 从固定的 MediaArea MediaInfoLib 26.05 与 ZenLib 0.4.41 直接生成四种 ABI, 不再依赖陈旧个人仓库的预编译库
- `新增` 可复现来源链: 在锁文件与 APK 中记录上游标签, 完整提交, NDK / CMake 配置及许可原文, 并自动审计 ELF 与五个 APK
- `新增` 上游稳定版跟踪: 每周或手动检查官方 Release, 仅以 Draft PR 提交固定版本更新, 检测标签移动且永不自动合并或发布
- `修复` 固定 JNI 包装类及其方法不被 R8 改写, 并安装实际 minified Release APK 运行公开 AIDL 冒烟测试, 防止原生库在发布构建中不可用
- `优化` MediaInfoLib 26.05 提供更丰富的编码, HDR / 色彩, 校验和与封面图元数据, 同时保持公开 AIDL 与 `autojs6-plugin-mediainfo-snapshot-v1` 契约
- `优化` 四种 ABI 均支持 16 KB page size, 并通过 API 24-37, x86 / x86_64, ARM32 / ARM64, 超时, 缓存, 真实媒体与超大文件门禁
- `优化` 同一批真实样本的 0.7.83 / 26.05 完整报告, 字段查询和 sections 差异已审阅; 容器与核心流保持兼容, 字段文本继续遵循上游解析结果
- `依赖` 原生解析引擎从冻结的 MediaInfoLib 0.7.83 升级至 26.05, 并固定 ZenLib 0.4.41 与 Android NDK 29.0.14206865

#### v1.1.0

_2026/08/31_

- `新增` 免整文件拷贝解析: 可随机访问的常规文件描述符通过 `/proc/self/fd` 交由 MediaInfoLib 直读, 管道或直读失败时才使用私有临时副本
- `新增` 进程内结果缓存: API 27+ 对完整报告, 字段查询与快照采用稳定文件身份, LRU, 10 分钟滑动有效期及低内存清理, 避免重复解析
- `新增` 协作取消与超时: 每次 AIDL 调用设有 30 秒上限, 超时中止原生解析或回退复制, 释放临时资源并返回 `MEDIAINFO_TIMEOUT`
- `修复` 修复大文件常规描述符被不必要地整文件复制的问题, 并保证成功, 失败, 参数校验, 取消和超时路径均关闭描述符与回退文件
- `修复` 修复缓存对快速文件修改的识别精度: 使用纳秒级 mtime / ctime, 并在 API 24-26 保守禁用缓存复用
- `优化` ABI 上报按已安装 base / split APK 中实际存在的 `libmediainfo.so` 动态生成, 并保留安全的进程 ABI 回退
- `优化` 增强快照小节解析, 正确处理重复和编号流, 畸形行, 值内冒号, 重复字段与独立输出选项
- `优化` 加入可复现的合成基准与真实媒体验证工具, 并记录 x86, x86_64 与 ARM64 的完整性能基线
- `优化` 重建 10 语言 README, 插件使用说明与更新日志生成链路, 加入漂移校验和 GitHub Actions 门禁

#### v1.0.0

_2026/07/15_

- `新增` 首个正式版本: 为 AutoJs6 提供基于 MediaInfoLib 的媒体文件信息读取能力, 一次调用即可获取容器格式, 编码, 时长, 分辨率, 码率, 声道等技术参数
- `新增` 脚本 API: Node 环境 `require("mediainfo")` 提供异步 `read`/`get`; Rhino 环境全局模块 `mediainfo(path)` 同步返回可属性访问的解析对象
- `新增` 三种读取能力: 完整文本报告 (`inform`), 单项参数查询 (`get`), 结构化 JSON 快照 (`snapshot`, schema 为 `autojs6-plugin-mediainfo-snapshot-v1`)
- `新增` 支持被 AutoJs6 通过 `org.autojs.plugin.MEDIAINFO` 自动发现; 插件在独立进程中以只读文件描述符接收并解析媒体内容, 不申请网络与任何敏感系统权限
- `新增` 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四种单架构安装包与包含全部架构的 `universal` 包, 发布文件名含版本号, 架构与 CRC32 摘要
- `新增` 插件信息, 使用说明, README 与更新日志覆盖 10 种语言: 简体中文, 香港繁体, 台湾繁体, 英语, 法语, 西班牙语, 日语, 韩语, 俄语与阿拉伯语

##### 更多发行历史可参阅

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hans.md)

******

### 构建

******

本节面向希望从源码构建插件的开发者.

构建前递归检出仓库及两个固定提交的官方子模块:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

构建 debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

构建 release APK (已启用 ABI 拆分, 一次产出 4 个单架构包与 1 个 `universal` 包; 在不入库的 `sign.properties` 中配置签名后自动签名):

```powershell
.\gradlew.bat :app:assembleRelease
```

发布归档可运行 `:app:appendDigestToReleasedFiles` 任务, 将 `app/release` 下的 APK 复制到 `app/releases` 并重命名为 `autojs6-plugin-mediainfo-v2.0.0-<abi>-<crc32>.apk` 形式.

构建参数集中于 `version.properties`: 最低 SDK 24 (Android 7.0), 目标 SDK 36, 当前版本 2.0.0.

******

### 本地化与文档生成

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

`strings.xml` 提供本地化插件描述与错误信息, `plugin_instruction.md` 提供宿主插件中心展示的使用说明. README, 更新日志与使用说明均由 JSON 源生成: 修改 `.readme/` 与 `.changelog/` 下的源文件后运行 `py .python/generate_markdown.py` 重新生成全部产物, 生成产物不手工编辑; 运行 `py .python/generate_markdown.py --check` 可校验源文件与产物是否同步 (CI 亦会自动校验).

******

### 许可

******

项目代码使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). v2 官方源码构建线的 `libmediainfo.so` 来自 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) 与 [ZenLib](https://github.com/MediaArea/ZenLib) (zlib 许可), 兼容 JNI 桥由本仓库维护. 已冻结的 v1.1.0 二进制来源另行记录.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### 相关链接

******

- AutoJs6 MediaInfo 文档: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方网站: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 项目: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 封装: https://github.com/olegazyx/MediaInfoLib-android
