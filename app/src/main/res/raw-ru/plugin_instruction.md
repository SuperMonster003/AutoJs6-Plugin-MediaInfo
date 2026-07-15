Читайте метаданные MediaInfo из медиафайла:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

Поддерживаемые типы потоков: `general`, `video`, `audio`, `text`, `other`, `image` и `menu`.

`includeInform` управляет текстовым отчетом. `includeSections` управляет разобранным объектом `sections`.

Дополнительные примеры см. в разделе [MediaInfo](https://docs.autojs6.com/#/mediainfo) документации AutoJs6.
