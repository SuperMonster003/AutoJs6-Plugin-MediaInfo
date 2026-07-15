Read MediaInfo metadata from a media file:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

Supported stream kinds are `general`, `video`, `audio`, `text`, `other`, `image`, and `menu`.

`includeInform` controls the text report. `includeSections` controls the parsed `sections` object.

For more usage examples, refer to the [MediaInfo](https://docs.autojs6.com/#/mediainfo) section in the AutoJs6 documentation.
