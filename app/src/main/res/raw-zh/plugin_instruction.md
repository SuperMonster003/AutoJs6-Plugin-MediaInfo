MediaInfo 插件 (MediaInfo Plugin) 为 AutoJs6 提供媒体文件信息读取能力. 安装后, 脚本只需一行代码即可获取视频, 音频, 图片等文件的容器格式, 编码, 时长, 分辨率, 码率, 声道等上百项技术参数, AutoJs6 文件列表中的媒体信息对话框也将由本插件提供完整解析报告. 解析能力来自与桌面端 MediaInfo 同源的开源库 MediaInfoLib.

### 使用方法

1. 从 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 页面下载与设备匹配的插件 APK 并安装到运行 AutoJs6 的设备上; 拿不准选哪个时, 可直接选 `universal` 包, 或参考下方 `如何选择安装包`.
2. 打开 AutoJs6 的插件中心, 确认 `MediaInfo` 插件已被识别并处于启用状态.
3. 在脚本中按下方 `脚本 API` 的示例调用 `mediainfo` 模块; 也可以在 AutoJs6 文件列表中打开媒体文件的媒体信息对话框直接查看完整报告.

若插件中心未显示该插件, 请先将 AutoJs6 升级到较新版本 (内部版本号 3923 及以上). 插件自身支持 Android 7.0 (API 24) 及以上的设备.

### 脚本 API

Node 环境 (脚本首行声明 `"nodejs"`) 中通过 `require("mediainfo")` 获取模块, 全部方法返回 Promise:

```javascript
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4");
  console.log(snapshot.sections.general[0].format);

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

### 流类型

`get()` 的 `streamKind` 参数支持以下流类型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不区分大小写, 会映射到 MediaInfo 原生流类型; 查询不存在的流或无值参数时返回空字符串.

更多用法与字段说明可参阅 [AutoJs6 MediaInfo 文档](https://docs.autojs6.com/#/mediainfo) 与 [项目主页](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
