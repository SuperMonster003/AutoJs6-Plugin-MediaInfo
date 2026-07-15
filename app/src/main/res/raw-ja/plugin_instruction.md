メディアファイルから MediaInfo メタデータを読み取ります:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

サポートされるストリーム種別は `general`, `video`, `audio`, `text`, `other`, `image`, `menu` です.

`includeInform` はテキストレポートを制御します. `includeSections` は解析済みの `sections` オブジェクトを制御します.

その他の使用例は AutoJs6 ドキュメントの [MediaInfo](https://docs.autojs6.com/#/mediainfo) セクションを参照してください.
