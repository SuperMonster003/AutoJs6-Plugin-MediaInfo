Lee metadatos MediaInfo desde un archivo multimedia:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

Los tipos de flujo admitidos son `general`, `video`, `audio`, `text`, `other`, `image` y `menu`.

`includeInform` controla el informe de texto. `includeSections` controla el objeto `sections` analizado.

Para más ejemplos de uso, consulta la sección [MediaInfo](https://docs.autojs6.com/#/mediainfo) de la documentación de AutoJs6.
