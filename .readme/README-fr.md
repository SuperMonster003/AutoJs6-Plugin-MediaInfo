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
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues

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

Le plugin AutoJs6 MediaInfo fournit a AutoJs6 une lecture des métadonnées multimédias basée sur MediaInfo. Il peut produire des rapports complets, interroger des paramètres individuels et sortir des instantanés JSON structurés.

******

### Fonctions

******

- Fournit le service de plugin `mediainfo` avec l'ID de plugin `mediainfo`.
- Prend en charge `mediainfo.read(path, options)` et `mediainfo.get(path, streamKind, parameter, options)` dans l'environnement AutoJs6 Node.
- Prend en charge la découverte et l'appel par l'hôte via `org.autojs.plugin.MEDIAINFO`, avec `inform`, `get` et `snapshot` exposés par l'interface AIDL sous-jacente.
- Inclut `libmediainfo.so` pour `arm64-v8a`, `armeabi-v7a`, `x86_64` et `x86`.
- Les métadonnées du plugin, les instructions d'utilisation, le README et le changelog sont localisés en espagnol, français, russe, arabe, japonais, coréen, anglais, chinois simplifié, chinois traditionnel de Hong Kong et chinois traditionnel de Taiwan.

******

### Utilisation

******

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);

  const format = await mediainfo.get("sample.mp4", "general", "Format");
  console.log(format);
})();
```

Le chemin doit pointer vers un fichier multimédia accessible par l'hôte. Dans l'environnement Node, les chemins relatifs du répertoire de travail sont pris en charge.

******

### Types De Flux

******

Les types de flux MediaInfo pris en charge incluent:

```text
general, video, audio, text, other,
image, menu
```

`streamKind` dans `mediainfo.get()` n'est pas sensible a la casse et correspond aux types de flux natifs de MediaInfo.

******

### Options D'instantané

******

- `includeInform`: inclut le rapport texte MediaInfo dans l'instantané, valeur par défaut `true`.
- `includeSections`: analyse le rapport dans `sections`, valeur par défaut `true`.

******

### Historique Des Versions

******

# v1.0.0

###### 2026/07/15

* `Nouveauté` Ajout du service de plugin MediaInfo avec l'ID de plugin `mediainfo` et le moteur `mediainfo`
* `Nouveauté` Ajout de la découverte et de l'appel par l'hôte via `org.autojs.plugin.MEDIAINFO`
* `Nouveauté` Ajout des capacités `inform`, `get` et `snapshot` pour les rapports multimédias complets, la recherche de paramètre unique et les instantanés JSON structurés
* `Nouveauté` Ajout de `libmediainfo.so` pour `arm64-v8a`, `armeabi-v7a`, `x86_64` et `x86`, avec une variante APK `universal`
* `Nouveauté` Ajout des métadonnées ABI prises en charge dans les informations d'exécution du plugin et de noms APK de publication avec version, variante ABI et résumé CRC32
* `Nouveauté` Ajout des métadonnées du plugin, instructions d'utilisation, README et changelog localisés en espagnol, français, russe, arabe, japonais, coréen, anglais, chinois simplifié, chinois traditionnel de Hong Kong et chinois traditionnel de Taiwan

##### Pour plus d'historique des versions

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

******

### Compilation

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilation Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Les paramètres de compilation proviennent de `version.properties`; le SDK minimal actuel est 24 et le SDK cible est 36.

******

### Structure Des Ressources

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contient les descriptions localisées du plugin et les messages d'erreur; `plugin_instruction.md` contient les instructions d'utilisation affichées par l'hôte. Les fichiers README et CHANGELOG sont générés depuis des sources JSON par `.python/generate_markdown.py`.

******

### Liens

******

- Documentation AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Projet officiel MediaInfo: https://mediaarea.net/en/MediaInfo
- Projet officiel MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Projet MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
