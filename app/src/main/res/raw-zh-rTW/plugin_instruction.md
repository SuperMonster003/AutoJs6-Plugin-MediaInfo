讀取媒體檔案的 MediaInfo 資訊:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

支援的串流類型包括 `general`, `video`, `audio`, `text`, `other`, `image`, `menu`.

`includeInform` 控制文字報告. `includeSections` 控制解析後的 `sections` 物件.

更多用法可參考 AutoJs6 文件的 [MediaInfo](https://docs.autojs6.com/#/mediainfo) 章節.
