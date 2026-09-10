<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Plugin MediaInfo pour lire les informations des fichiers multimédias</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues (Languages)

******

Le README.md actuel prend en charge les langues suivantes:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- Français [fr] # actuel
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### Introduction

******

Le plugin MediaInfo (MediaInfo Plugin) apporte a AutoJs6 la lecture des informations des fichiers multimédias. Une fois installé, une seule ligne de script suffit pour obtenir des centaines de paramètres techniques des fichiers vidéo, audio et image, tels que le format du conteneur, le codec, la durée, la résolution, le débit et les canaux; la boite de dialogue d'informations multimédias de la liste de fichiers d'AutoJs6 affiche également le rapport complet fourni par ce plugin. L'analyse repose sur MediaInfoLib, la bibliothèque open source qui anime l'application MediaInfo de bureau.

Le plugin s'exécute dans son propre processus et est découvert automatiquement par AutoJs6, sans aucune configuration. Pour lire un fichier, l'hôte transmet le contenu multimédia au plugin sous forme de descripteur en lecture seule. Les descripteurs de fichiers ordinaires positionnables sont lus directement par MediaInfoLib via `/proc/self/fd`; les descripteurs non positionnables comme les tubes, ou un échec d'analyse directe, utilisent une copie temporaire dans le cache privé, supprimée dès la fin de l'appel. Aucun accès réseau n'est utilisé et aucune permission système sensible n'est demandée.

******

### Points Forts

******

- Prêt a l'emploi: aucune configuration requise; AutoJs6 découvre le plugin automatiquement, et les scripts comme la boite de dialogue d'informations multimédias de la liste de fichiers peuvent l'utiliser immédiatement.
- Informations complètes: format du conteneur, codec, durée, résolution, fréquence d'images, débit, canaux, fréquence d'échantillonnage et plus encore, en un seul appel.
- Trois modes de lecture: rapport texte complet (`inform`), requête de paramètre unique (`get`) et instantané JSON structuré (`read`/`snapshot`), selon le besoin.
- Deux moteurs de script: l'environnement Node appelle de manière asynchrone via `require("mediainfo")`; l'environnement Rhino utilise le module global `mediainfo(path)` qui renvoie de manière synchrone un objet analysé accessible par propriétés.
- Large couverture de formats: l'analyse est assurée par MediaInfoLib, la bibliothèque du MediaInfo de bureau, prenant en charge de très nombreux formats vidéo, audio et image, courants comme rares.
- Cinq paquets APK: quatre paquets mono-architecture (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) plus un paquet `universal` tout-en-un, pour n'installer que le nécessaire.
- Respect de la vie privée: l'analyse se déroule dans un processus isolé qui ne reçoit que des descripteurs de fichier en lecture seule de l'hôte, sans permission réseau ni permission système sensible.
- Multilingue: métadonnées du plugin, instructions, README et journal des modifications disponibles en 10 langues.

******

### Utilisation

******

1. Téléchargez l'APK du plugin correspondant a l'appareil depuis la page [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) et installez-le sur l'appareil exécutant AutoJs6; en cas de doute, choisissez le paquet `universal` ou consultez `Choisir un APK` ci-dessous.
2. Ouvrez le centre de plugins d'AutoJs6 et vérifiez que le plugin `MediaInfo` est reconnu et activé.
3. Appelez le module `mediainfo` dans les scripts comme montré dans `API De Script` ci-dessous, ou ouvrez la boite de dialogue d'informations multimédias d'un fichier dans la liste de fichiers d'AutoJs6 pour consulter directement le rapport complet.

> Si le plugin n'apparait pas dans le centre de plugins, mettez d'abord AutoJs6 a niveau vers une version récente (build interne 3923 ou supérieur). Le plugin lui-même prend en charge les appareils sous Android 7.0 (API 24) et supérieur.

******

### Choisir Un APK

******

Chaque version publie 5 APK qui ne diffèrent que par les architectures de bibliothèque native incluses:

| Paquet | Convient a |
|---|---|
| `arm64-v8a` | La grande majorité des téléphones et tablettes Android modernes (ARM 64 bits); premier choix |
| `armeabi-v7a` | Appareils ARM 32 bits plus anciens |
| `x86_64` | Emulateurs x86 64 bits et quelques appareils x86 |
| `x86` | Emulateurs x86 32 bits et quelques appareils x86 |
| `universal` | Inclut les 4 architectures et est le plus volumineux; fonctionne sur tout appareil, choix sur en cas de doute |

Le plugin charge la bibliothèque native dans son propre processus. Si un paquet mono-architecture ne correspondant pas a l'appareil a été installé par erreur, les appels échouent avec `MediaInfo library is not available`; passer au paquet `universal` résout le problème.

******

### API De Script

******

Dans l'environnement Node (scripts commençant par la directive `"nodejs"`), obtenez le module via `require("mediainfo")`; toutes les méthodes renvoient une Promise:

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

`read(path, options?)` renvoie un instantané structuré (voir ci-dessous); `get(path, streamKind, parameter, options?)` renvoie le texte brut du paramètre. Les chemins relatifs partent du répertoire de travail; les chemins absolus et les répertoires parents sont aussi acceptés si Android autorise leur lecture par l’hôte. Fournissez un chemin de fichier, pas une URI content.

Dans l'environnement Rhino (moteur de script par défaut d'AutoJs6), `mediainfo` est un module global; `mediainfo(path)` et `mediainfo.read(path)` sont équivalents et renvoient de manière synchrone un objet analysé:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

Sur l'objet renvoyé, `path` et `inform` contiennent le chemin résolu et le rapport texte complet; chaque type de flux (comme `general`, `video`, `audio`) fonctionne a la fois comme propriété exposant les champs analysés (comme `mi.video.width`, noms de champs en camelCase) et comme fonction pour interroger en direct les paramètres bruts (comme `mi.audio("BitRate")`). Les scripts Rhino peuvent accéder a tout chemin que l'hôte est autorisé a lire.

Les requêtes MediaInfo prennent en charge streamNumber à partir de 0, countGet et infoKind pour les unités, descriptions et noms lisibles; Rhino et Node conservent TEXT sur le premier flux par défaut et négocient les capacités du plugin:

```javascript
// Rhino
const path = "/sdcard/Download/movie.mkv";
const count = mediainfo.countGet(path, "audio");
for (let index = 0; index < count; index++) {
  console.log(mediainfo.get(path, "audio", "SamplingRate", { streamNumber: index }));
}
console.log(mediainfo.get(path, "audio", "SamplingRate", { infoKind: "MEASURE" }));
```

```javascript
"nodejs";
const mi = require("mediainfo");
(async () => {
  const count = await mi.countGet("movie.mkv", "audio");
  for (let index = 0; index < count; index++) {
    console.log(await mi.get("movie.mkv", "audio", "SamplingRate", { streamNumber: index }));
  }
})();
```

******

### Démonstration

******

Écrans réels d’AutoJs6 en thèmes clair et sombre. L’exemple Rhino lit deux pistes PCM à 8 kHz et 16 kHz; le bouton MediaInfo ouvre le rapport complet avec le chemin source d’origine.

<table>
  <tr>
    <th>Sortie du script Rhino</th>
    <th>Informations du fichier multimédia</th>
    <th>Détails MediaInfo</th>
  </tr>
  <tr>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-script.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-script.png?raw=true" alt="Sortie du script Rhino" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-dialog.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-dialog.png?raw=true" alt="Informations du fichier multimédia" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-details.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-details.png?raw=true" alt="Détails MediaInfo" width="260" /></picture></td>
  </tr>
</table>

Exécutez le [script de démonstration](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/demo/mediainfo.js) avec le [fichier à deux pistes](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/androidTest/assets/mediainfo-two-audio.mka), ou indiquez votre propre fichier. L’échantillon contient du silence synthétique. L’affichage du chemin source et les requêtes étendues nécessitent les mises à jour coordonnées de l’hôte et des plugins.

******

### Structure De L'instantané Et Options

******

L'instantané renvoyé par `read()` dans l'environnement Node ressemble a ceci (le JSON renvoyé par la méthode AIDL `snapshot` du plugin est identique, sauf que son `schema` est `autojs6-plugin-mediainfo-snapshot-v1` et qu'il n'a pas de champ `path`):

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

- `includeInform`: inclure ou non le rapport texte `inform`, `true` par défaut; mettre a `false` pour obtenir une chaine vide et une charge plus légère.
- `includeSections`: analyser ou non le rapport en `sections`, `true` par défaut; mettre a `false` pour obtenir un objet vide.

`sections` utilise comme clés les noms de sections du rapport en minuscules (avec plusieurs flux d'un même type, les noms de sections peuvent porter un indice comme `audio #1`), et chaque valeur est un tableau d'objets; les noms de champs sont convertis en camelCase tandis que les valeurs conservent le texte MediaInfo original, unités et espaces de séparation des milliers compris, comme `1 920 pixels`.

******

### Types De Flux

******

Le paramètre `streamKind` de `get()` prend en charge les types de flux suivants:

```text
general, video, audio, text, other, image, menu
```

`streamKind` est insensible a la casse et correspond aux types de flux natifs de MediaInfo; interroger un flux inexistant ou un paramètre sans valeur renvoie une chaine vide.

******

### FAQ

******

#### Comment vérifier que le plugin fonctionne?

Ouvrez le centre de plugins d'AutoJs6; si le plugin `MediaInfo` y apparait, l'hôte l'a reconnu. Exécutez ensuite n'importe quel exemple de script `mediainfo`; un résultat renvoyé normalement signifie que le plugin fonctionne.

#### Pourquoi n'y a-t-il pas d'icône de plugin dans la liste des applications?

C'est normal. Le plugin n'a pas d'interface autonome et ne crée pas d'icône de lanceur; après installation, il est découvert et piloté entièrement par AutoJs6 en arrière-plan, et toute interaction se fait dans AutoJs6.

#### Un script Node signale `path must stay inside the scoped working directory`?

Mettez à jour AutoJs6 et le plugin Node Runtime. Les versions actuelles acceptent les chemins de fichiers ordinaires hors du projet, sous réserve des autorisations Android. Les anciens hôtes ou runtimes peuvent encore imposer la restriction au répertoire du projet.

#### `get()` a renvoyé une chaine vide?

Le nom du paramètre doit être un paramètre MediaInfo natif (comme `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), et le flux cible doit réellement exister. Utilisez d'abord `read()` pour inspecter les champs réellement disponibles dans `sections`, ou consultez le rapport `inform` complet.

#### La lecture de gros fichiers est lente?

Les fichiers ordinaires sont désormais analysés directement sans copie intégrale dans le cache, ce qui évite aux gros fichiers un temps de copie linéaire avec leur taille. Les descripteurs non positionnables comme les tubes, ou les formats dont l'analyse directe échoue, utilisent encore une copie temporaire dont le cout dépend des octets reçus; le temps d'analyse propre à MediaInfoLib reste lié au format et au contenu.

#### Les résultats d'analyse sont-ils mis en cache et que se passe-t-il en cas de délai dépassé?

Oui. Sous Android 8.1 (API 27) et versions ultérieures, pour un fichier ordinaire dont l'identité reste stable et le contenu inchangé, le plugin met en cache dans le processus courant le rapport, les requêtes et les instantanés: au plus 32 fichiers, 64 requêtes par fichier, une durée de vie glissante de 10 minutes et environ 2 MiB de texte au total. Le cache est désactivé par prudence sur API 24-26, où les horodatages de fichiers à la nanoseconde sont indisponibles; il est vidé en cas de mémoire faible ou à l'arrêt du processus. Chaque appel AIDL est limité à 30 secondes; au-delà, l'analyse native ou la copie de repli est annulée de façon coopérative, le fichier temporaire est supprimé et l'exception contient `MEDIAINFO_TIMEOUT`.

#### Le fichier a plusieurs pistes audio ou sous-titres; comment lire la deuxième piste et les suivantes?

Après mise à jour de l'hôte et du runtime Node, get(path, "audio", "Format", {streamNumber: 1}) interroge le deuxième flux audio. countGet(path, "audio") compte les flux et infoKind: "MEASURE" renvoie l'unité. Vérifiez capabilities(). Voir [le contrat](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_QUERY.md).

#### Le plugin accède-t-il au réseau ou demande-t-il des permissions sensibles?

Non. Son manifeste ne contient aucune permission réseau, stockage, caméra ni autre permission système sensible; il ne déclare que la permission de plugin utilisée pour communiquer avec AutoJs6. Le contenu multimédia arrive de l'hôte sous forme de descripteur en lecture seule, et la copie temporaire créée pour l'analyse est supprimée aussitôt.

******

### Interface Du Plugin

******

Les informations suivantes s'adressent aux développeurs de l'hôte AutoJs6 et de plugins; l'hôte utilise ces identifiants pour découvrir le plugin et négocier les capacités:

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

`MediainfoPluginService` expose six méthodes, `getInfo`/`inform`/`get`/`snapshot`/`getDetail`/`countGet`, via l'interface AIDL `IMediainfoPlugin`; le contenu multimédia est transmis sous forme de `ParcelFileDescriptor` en lecture seule plus un nom d'affichage, et `snapshot` accepte en outre un `Bundle` d'options portant `includeInform`/`includeSections`. Le service et `WakeActivity` sont tous deux protégés par la permission `org.autojs.permission.PLUGIN`.

L'analyse multimédia utilise les bibliothèques natives MediaInfoLib incluses.

******

### Feuille De Route

******

Les capacités prévues du plugin et leur état d'avancement sont maintenus sous forme de liste cochable dans ROADMAP.md, organisée par jalons avec critères d'acceptation, couvrant les requêtes par indice de flux, l'analyse sans copie, le rapport ABI dynamique, l'évolution de la bibliothèque native et l'intégration continue. Les éléments non cochés sont des intentions et non des capacités livrées; les discussions via Issues sont bienvenues.

- [Voir ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### Historique Des Versions

******

#### v2.1.0

_2026/09/10_

- `Nouveauté` Les requêtes MediaInfo prennent en charge streamNumber à partir de 0, countGet et infoKind pour les unités, descriptions et noms lisibles; Rhino et Node conservent TEXT sur le premier flux par défaut et négocient les capacités du plugin
- `Nouveauté` Le schéma snapshot v2 optionnel regroupe les flux JSON natifs en tableaux et expose la version du moteur, avec snapshot v1 par défaut
- `Correction` Complete name affiche le chemin du fichier source dans les détails et instantanés MediaInfo au lieu du cache privé ou du descripteur, sans modifier le nom de fichier de l'instantané
- `Amélioration` Actualisation de l’accès aux chemins Node et ajout de captures réelles claires/sombres avec un exemple exécutable à deux pistes

#### v2.0.0

_2026/09/01_

- `Nouveauté` Compilation depuis les sources officielles: les quatre ABI sont générées directement depuis MediaArea MediaInfoLib 26.05 et ZenLib 0.4.41 épinglés, sans les bibliothèques précompilées de l'ancien dépôt personnel
- `Nouveauté` Provenance reproductible: les tags, commits complets, réglages NDK / CMake et textes de licence sont consignés dans le verrou et chaque APK, avec audit automatique des ELF et des cinq APK
- `Nouveauté` Suivi des versions stables: les vérifications hebdomadaires ou manuelles proposent les mises à jour officielles épinglées via des Draft PR, détectent les tags déplacés et ne fusionnent ni ne publient automatiquement
- `Correction` Conservation de la classe JNI exacte et de ses méthodes avec R8, plus un test AIDL public installant le véritable APK Release minifié afin d'éviter les échecs de chargement natif en production
- `Amélioration` MediaInfoLib 26.05 fournit davantage de métadonnées de codec, HDR / couleur, somme de contrôle et pochette tout en conservant les contrats AIDL publics et `autojs6-plugin-mediainfo-snapshot-v1`
- `Amélioration` Chaque ABI prend en charge les pages de 16 KB et passe les contrôles API 24-37, x86 / x86_64, ARM32 / ARM64, délai, cache, médias réels et fichiers immenses
- `Amélioration` Les rapports complets, requêtes de champs et sections de 0.7.83 et 26.05 ont été comparés sur les mêmes médias; conteneurs et flux principaux restent compatibles tandis que le texte des champs suit l'analyse amont
- `Amélioration` Uniformiser la mise en page du README et la gestion des versions de la plateforme Gradle
- `Dépendance` Mise à niveau du moteur natif figé de MediaInfoLib 0.7.83 vers 26.05, avec ZenLib 0.4.41 et Android NDK 29.0.14206865 épinglés

#### v1.1.0

_2026/08/31_

- `Nouveauté` Analyse sans copie intégrale: les descripteurs de fichiers ordinaires positionnables sont lus directement par MediaInfoLib via /proc/self/fd; seuls les tubes ou les échecs de lecture directe utilisent une copie temporaire privée
- `Nouveauté` Cache de résultats dans le processus: sur API 27+, les rapports complets, requêtes de champs et instantanés utilisent une identité de fichier stable, un LRU, une durée glissante de 10 minutes et une purge en cas de mémoire faible
- `Nouveauté` Annulation coopérative et délai: chaque appel AIDL est limité à 30 secondes; le dépassement arrête l'analyse native ou la copie de repli, libère les ressources temporaires et renvoie MEDIAINFO_TIMEOUT
- `Correction` Suppression de la copie systématique du fichier multimédia complet et fermeture fiable des descripteurs, analyseurs natifs, flux et fichiers temporaires sur tous les chemins d'erreur
- `Correction` L'identité du cache conserve les horodatages à la nanoseconde et le cache est désactivé sur API 24 a 26, ou ces informations ne peuvent pas être validées en toute sécurité
- `Amélioration` L'inventaire dynamique des ABI vérifie les bibliothèques MediaInfoLib réellement empaquetées et maintient cohérents le rapport d'exécution, les métadonnées et les cinq variantes APK
- `Amélioration` L'analyseur d'instantané tolère mieux les libellés localisés, les groupes répétés, les champs inconnus et les sorties partielles de MediaInfoLib
- `Amélioration` Ajout d'outils de benchmark reproductibles pour les appels à froid et à chaud, la concurrence, les délais et la validation sur de vrais médias, avec manifeste de sources et résumé SHA-256
- `Amélioration` La génération documentaire validée couvre désormais 10 langues et produit de façon déterministe README, instructions intégrées et journaux des modifications

##### Pour plus d'historique des versions

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

******

### Compilation

******

Cette section s'adresse aux développeurs souhaitant compiler le plugin depuis les sources.

Clonez le dépôt avec les deux sous-modules officiels épinglés avant la compilation:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

Compiler les APK debug:

```powershell
.\gradlew.bat :app:assembleDebug
```

Compiler les APK release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Pour l'archivage des publications, exécutez la tache `:app:appendDigestToReleasedFiles`, qui copie les APK de `app/release` vers `app/releases` et les renomme selon le motif `autojs6-plugin-mediainfo-v2.1.0-<abi>-<crc32>.apk`.

Les paramètres de compilation sont centralisés dans `version.properties`: SDK minimal 24 (Android 7.0), SDK cible 36, version actuelle 2.1.0.

******

### Localisation Et Génération De Docs

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

`strings.xml` contient la description localisée du plugin et les messages d'erreur, et `plugin_instruction.md` contient les instructions affichées dans le centre de plugins de l'hôte. README, journal des modifications et instructions sont tous générés depuis des sources JSON: modifiez les sources sous `.readme/` et `.changelog/`, puis exécutez `py .python/generate_markdown.py` pour régénérer chaque artefact; les artefacts générés ne sont jamais édités a la main. Exécutez `py .python/generate_markdown.py --check` pour vérifier que sources et artefacts sont synchronisés (la CI le vérifie aussi automatiquement).

******

### Licence

******

Le code du projet est sous licence [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). Pour la branche v2 construite depuis les sources, `libmediainfo.so` est produit depuis les sources officielles [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) et [ZenLib](https://github.com/MediaArea/ZenLib) (licence zlib), tandis que le pont JNI compatible est maintenu dans ce dépôt. La provenance du binaire v1.1.0 gelé reste documentée séparément.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### Liens

******

- Documentation AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Site officiel MediaInfo: https://mediaarea.net/en/MediaInfo
- Projet MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Enveloppe MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
