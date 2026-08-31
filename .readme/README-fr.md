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
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
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

`read(path, options?)` renvoie un objet instantané structuré (voir `Structure De L'instantané Et Options` ci-dessous); `get(path, streamKind?, parameter)` renvoie le texte brut du paramètre, `streamKind` valant `general` par défaut. Par sécurité, les scripts Node ne peuvent accéder qu'aux fichiers du répertoire du projet, et les chemins relatifs sont résolus depuis la racine du projet.

Dans l'environnement Rhino (moteur de script par défaut d'AutoJs6), `mediainfo` est un module global; `mediainfo(path)` et `mediainfo.read(path)` sont équivalents et renvoient de manière synchrone un objet analysé:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

Sur l'objet renvoyé, `path` et `inform` contiennent le chemin résolu et le rapport texte complet; chaque type de flux (comme `general`, `video`, `audio`) fonctionne a la fois comme propriété exposant les champs analysés (comme `mi.video.width`, noms de champs en camelCase) et comme fonction pour interroger en direct les paramètres bruts (comme `mi.audio("BitRate")`). Les scripts Rhino peuvent accéder a tout chemin que l'hôte est autorisé a lire.

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

#### Un script Node échoue avec `path must stay inside the scoped working directory`?

Par sécurité, le moteur Node n'autorise l'accès qu'aux fichiers du répertoire du projet. Déplacez ou copiez le fichier multimédia dans le répertoire du projet avant de le lire; pour accéder a d'autres emplacements (galerie ou téléchargements), utilisez plutôt un script du moteur Rhino.

#### `get()` a renvoyé une chaine vide?

Le nom du paramètre doit être un paramètre MediaInfo natif (comme `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), et le flux cible doit réellement exister. Utilisez d'abord `read()` pour inspecter les champs réellement disponibles dans `sections`, ou consultez le rapport `inform` complet.

#### La lecture de gros fichiers est lente?

Les fichiers ordinaires sont désormais analysés directement sans copie intégrale dans le cache, ce qui évite aux gros fichiers un temps de copie linéaire avec leur taille. Les descripteurs non positionnables comme les tubes, ou les formats dont l'analyse directe échoue, utilisent encore une copie temporaire dont le cout dépend des octets reçus; le temps d'analyse propre à MediaInfoLib reste lié au format et au contenu.

#### Les résultats d'analyse sont-ils mis en cache et que se passe-t-il en cas de délai dépassé?

Oui. Sous Android 8.1 (API 27) et versions ultérieures, pour un fichier ordinaire dont l'identité reste stable et le contenu inchangé, le plugin met en cache dans le processus courant le rapport, les requêtes et les instantanés: au plus 32 fichiers, 64 requêtes par fichier, une durée de vie glissante de 10 minutes et environ 2 MiB de texte au total. Le cache est désactivé par prudence sur API 24-26, où les horodatages de fichiers à la nanoseconde sont indisponibles; il est vidé en cas de mémoire faible ou à l'arrêt du processus. Chaque appel AIDL est limité à 30 secondes; au-delà, l'analyse native ou la copie de repli est annulée de façon coopérative, le fichier temporaire est supprimé et l'exception contient `MEDIAINFO_TIMEOUT`.

#### Le fichier a plusieurs pistes audio ou sous-titres; comment lire la deuxième piste et les suivantes?

L'instantané `sections` conserve toutes les sections du rapport (avec plusieurs flux, les noms de sections portent un indice comme `audio #2`), lisez-les donc directement; `get()` interroge actuellement toujours le premier flux d'un type, et la sélection par indice de flux est prévue dans [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### Le plugin accède-t-il au réseau ou demande-t-il des permissions sensibles?

Non. Son manifeste ne contient aucune permission réseau, stockage, caméra ni autre permission système sensible; il ne déclare que la permission de plugin utilisée pour communiquer avec AutoJs6. Le contenu multimédia arrive de l'hôte sous forme de descripteur en lecture seule, et la copie temporaire créée pour l'analyse est supprimée aussitôt.

******

### Permissions Et Sécurité

******

Les fichiers multimédias peuvent provenir de sources non fiables, la conception place donc plusieurs lignes de défense autour de l'analyse:

- Isolation de processus: l'analyse se déroule dans le propre processus du plugin et la bibliothèque native n'est jamais injectée dans le processus hôte, si bien qu'un échec d'analyse laisse AutoJs6 fonctionner normalement.
- Surface de données minimale: le plugin ne peut pas lire le stockage de l'appareil par lui-même; il ne reçoit qu'un descripteur de fichier en lecture seule ouvert par l'hôte et un nom d'affichage.
- Lecture directe si possible, purge en cas de repli: les descripteurs ordinaires positionnables ne créent aucune copie du média; seul le repli de compatibilité écrit dans le cache privé, puis supprime le fichier temporaire dès la fin de l'appel.
- Permissions minimales: aucune permission réseau, stockage, caméra ni autre permission système sensible; le service et le point d'éveil sont protégés par la permission de plugin AutoJs6 (`org.autojs.permission.PLUGIN`), les applications tierces ne peuvent donc pas les appeler directement.
- Ouvert et auditable: le code du plugin, les scripts de build et la chaine de génération de documentation sont entièrement open source, et l'origine de la bibliothèque native et de l'enveloppe JNI est indiquée dans la section licence.

N'installez le plugin que depuis la page officielle [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) ou d'autres canaux de confiance; des paquets d'origine inconnue peuvent être altérés même si le nom et le numéro de version semblent identiques.

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

`MediainfoPluginService` expose quatre méthodes, `getInfo`/`inform`/`get`/`snapshot`, via l'interface AIDL `IMediainfoPlugin`; le contenu multimédia est transmis sous forme de `ParcelFileDescriptor` en lecture seule plus un nom d'affichage, et `snapshot` accepte en outre un `Bundle` d'options portant `includeInform`/`includeSections`. Le service et `WakeActivity` sont tous deux protégés par la permission `org.autojs.permission.PLUGIN`.

Le plugin analyse les APK base / split installés et rapporte dynamiquement les ABI qui contiennent réellement `libmediainfo.so`; un paquet mono-architecture ne rapporte que son ABI, tandis que `universal` rapporte les 4. Si les chemins APK sont illisibles, il se replie en toute sécurité sur l'architecture du processus courant lorsqu'une bibliothèque native extraite est présente.

******

### Feuille De Route

******

Les capacités prévues du plugin et leur état d'avancement sont maintenus sous forme de liste cochable dans ROADMAP.md, organisée par jalons avec critères d'acceptation, couvrant les requêtes par indice de flux, l'analyse sans copie, le rapport ABI dynamique, l'évolution de la bibliothèque native et l'intégration continue. Les éléments non cochés sont des intentions et non des capacités livrées; les discussions via Issues sont bienvenues.

- [Voir ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### Historique Des Versions

******

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

#### v1.0.0

_2026/07/15_

- `Nouveauté` Première version stable: apporte a AutoJs6 la lecture des informations de fichiers multimédias via MediaInfoLib, obtenant format du conteneur, codec, durée, résolution, débit, canaux et plus en un seul appel
- `Nouveauté` API de script: l'environnement Node reçoit `read`/`get` asynchrones via `require("mediainfo")`; l'environnement Rhino reçoit le module global `mediainfo(path)` renvoyant de manière synchrone un objet analysé accessible par propriétés
- `Nouveauté` Trois capacités de lecture: rapport texte complet (`inform`), requête de paramètre unique (`get`) et instantané JSON structuré (`snapshot`, schéma `autojs6-plugin-mediainfo-snapshot-v1`)
- `Nouveauté` Découvert automatiquement par AutoJs6 via `org.autojs.plugin.MEDIAINFO`; le plugin reçoit et analyse le contenu multimédia dans son propre processus via des descripteurs de fichier en lecture seule, sans permission réseau ni permission système sensible
- `Nouveauté` Fournit quatre paquets mono-architecture (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) plus un paquet `universal` toutes architectures, avec des noms de fichiers de publication portant version, ABI et résumé CRC32
- `Nouveauté` Métadonnées du plugin, instructions, README et journal des modifications couvrent 10 langues: chinois simplifié, chinois traditionnel de Hong Kong, chinois traditionnel de Taiwan, anglais, français, espagnol, japonais, coréen, russe et arabe

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

Compiler les APK release (les splits ABI sont activés, produisant 4 paquets mono-architecture plus 1 paquet `universal` en une fois; configurez le fichier non suivi `sign.properties` pour la signature automatique):

```powershell
.\gradlew.bat :app:assembleRelease
```

Pour l'archivage des publications, exécutez la tache `:app:appendDigestToReleasedFiles`, qui copie les APK de `app/release` vers `app/releases` et les renomme selon le motif `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk`.

Les paramètres de compilation sont centralisés dans `version.properties`: SDK minimal 24 (Android 7.0), SDK cible 36, version actuelle 1.1.0.

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
