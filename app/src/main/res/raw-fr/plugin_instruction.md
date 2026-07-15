Lire les métadonnées MediaInfo depuis un fichier multimédia:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

Les types de flux pris en charge sont `general`, `video`, `audio`, `text`, `other`, `image` et `menu`.

`includeInform` contrôle le rapport texte. `includeSections` contrôle l'objet `sections` analysé.

Pour plus d'exemples, consultez la section [MediaInfo](https://docs.autojs6.com/#/mediainfo) de la documentation AutoJs6.
