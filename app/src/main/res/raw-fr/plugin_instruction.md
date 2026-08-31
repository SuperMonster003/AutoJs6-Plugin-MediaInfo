Le plugin MediaInfo (MediaInfo Plugin) apporte a AutoJs6 la lecture des informations des fichiers multimédias. Une fois installé, une seule ligne de script suffit pour obtenir des centaines de paramètres techniques des fichiers vidéo, audio et image, tels que le format du conteneur, le codec, la durée, la résolution, le débit et les canaux; la boite de dialogue d'informations multimédias de la liste de fichiers d'AutoJs6 affiche également le rapport complet fourni par ce plugin. L'analyse repose sur MediaInfoLib, la bibliothèque open source qui anime l'application MediaInfo de bureau.

### Utilisation

1. Téléchargez l'APK du plugin correspondant a l'appareil depuis la page [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) et installez-le sur l'appareil exécutant AutoJs6; en cas de doute, choisissez le paquet `universal` ou consultez `Choisir un APK` ci-dessous.
2. Ouvrez le centre de plugins d'AutoJs6 et vérifiez que le plugin `MediaInfo` est reconnu et activé.
3. Appelez le module `mediainfo` dans les scripts comme montré dans `API De Script` ci-dessous, ou ouvrez la boite de dialogue d'informations multimédias d'un fichier dans la liste de fichiers d'AutoJs6 pour consulter directement le rapport complet.

Si le plugin n'apparait pas dans le centre de plugins, mettez d'abord AutoJs6 a niveau vers une version récente (build interne 3923 ou supérieur). Le plugin lui-même prend en charge les appareils sous Android 7.0 (API 24) et supérieur.

### API De Script

Dans l'environnement Node (scripts commençant par la directive `"nodejs"`), obtenez le module via `require("mediainfo")`; toutes les méthodes renvoient une Promise:

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

`read(path, options?)` renvoie un objet instantané structuré (voir `Structure De L'instantané Et Options` ci-dessous); `get(path, streamKind?, parameter)` renvoie le texte brut du paramètre, `streamKind` valant `general` par défaut. Par sécurité, les scripts Node ne peuvent accéder qu'aux fichiers du répertoire du projet, et les chemins relatifs sont résolus depuis la racine du projet.

Dans l'environnement Rhino (moteur de script par défaut d'AutoJs6), `mediainfo` est un module global; `mediainfo(path)` et `mediainfo.read(path)` sont équivalents et renvoient de manière synchrone un objet analysé:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

Sur l'objet renvoyé, `path` et `inform` contiennent le chemin résolu et le rapport texte complet; chaque type de flux (comme `general`, `video`, `audio`) fonctionne a la fois comme propriété exposant les champs analysés (comme `mi.video.width`, noms de champs en camelCase) et comme fonction pour interroger en direct les paramètres bruts (comme `mi.audio("BitRate")`). Les scripts Rhino peuvent accéder a tout chemin que l'hôte est autorisé a lire.

### Types De Flux

Le paramètre `streamKind` de `get()` prend en charge les types de flux suivants:

```text
general, video, audio, text, other, image, menu
```

`streamKind` est insensible a la casse et correspond aux types de flux natifs de MediaInfo; interroger un flux inexistant ou un paramètre sans valeur renvoie une chaine vide.

Pour plus de détails d'utilisation et la référence des champs, consultez la [documentation AutoJs6 MediaInfo](https://docs.autojs6.com/#/mediainfo) et la [page du projet](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
