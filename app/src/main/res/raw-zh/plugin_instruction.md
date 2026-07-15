读取媒体文件的 MediaInfo 信息:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

支持的流类型包括 `general`, `video`, `audio`, `text`, `other`, `image`, `menu`.

`includeInform` 控制文本报告. `includeSections` 控制解析后的 `sections` 对象.

更多用法可参考 AutoJs6 文档的 [MediaInfo](https://docs.autojs6.com/#/mediainfo) 章节.
