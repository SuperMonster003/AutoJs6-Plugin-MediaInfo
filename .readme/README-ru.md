<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Плагин MediaInfo для чтения информации о медиафайлах</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
  </p>
</div>

******

### Языки

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### Введение

******

Плагин AutoJs6 MediaInfo предоставляет AutoJs6 чтение медиа метаданных на базе MediaInfo. Он может создавать полные отчеты, запрашивать отдельные параметры и выводить структурированные JSON снимки.

******

### Возможности

******

- Предоставляет сервис плагина `mediainfo` с ID плагина `mediainfo`.
- Поддерживает `mediainfo.read(path, options)` и `mediainfo.get(path, streamKind, parameter, options)` в среде AutoJs6 Node.
- Поддерживает обнаружение и вызов хостом через `org.autojs.plugin.MEDIAINFO`, а нижний интерфейс AIDL предоставляет `inform`, `get` и `snapshot`.
- Включает `libmediainfo.so` для `arm64-v8a`, `armeabi-v7a`, `x86_64` и `x86`.
- Метаданные плагина, инструкции по использованию, README и changelog локализованы на испанский, французский, русский, арабский, японский, корейский, английский, упрощенный китайский, традиционный китайский Гонконга и традиционный китайский Тайваня.

******

### Использование

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

Путь должен указывать на медиафайл, доступный хосту. В среде Node поддерживаются относительные пути внутри рабочего каталога.

******

### Типы Потоков

******

Поддерживаемые типы потоков MediaInfo включают:

```text
general, video, audio, text, other,
image, menu
```

`streamKind` в `mediainfo.get()` не зависит от регистра и сопоставляется с нативными типами потоков MediaInfo.

******

### Параметры Снимка

******

- `includeInform`: включает текстовый отчет MediaInfo в снимок, по умолчанию `true`.
- `includeSections`: разбирает отчет в `sections`, по умолчанию `true`.

******

### История Выпусков

******

# v1.0.0

###### 2026/07/15

* `Функция` Добавлен сервис плагина MediaInfo с ID плагина `mediainfo` и движком `mediainfo`
* `Функция` Добавлены обнаружение и вызов хостом через `org.autojs.plugin.MEDIAINFO`
* `Функция` Добавлены возможности `inform`, `get` и `snapshot` для полных медиа отчетов, поиска отдельного параметра и структурированных JSON снимков
* `Функция` Добавлен `libmediainfo.so` для `arm64-v8a`, `armeabi-v7a`, `x86_64` и `x86`, а также вариант APK `universal`
* `Функция` Добавлены метаданные поддерживаемых ABI в информацию выполнения плагина и имена APK выпусков с версией, вариантом ABI и дайджестом CRC32
* `Функция` Добавлены локализованные метаданные плагина, инструкции по использованию, README и changelog на испанский, французский, русский, арабский, японский, корейский, английский, упрощенный китайский, традиционный китайский Гонконга и традиционный китайский Тайваня

##### Больше истории выпусков

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-ru.md)

******

### Сборка

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release сборка:

```powershell
.\gradlew.bat :app:assembleRelease
```

Параметры сборки берутся из `version.properties`; текущий минимальный SDK равен 24, целевой SDK равен 36.

******

### Структура Ресурсов

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` содержит локализованные описания плагина и сообщения об ошибках; `plugin_instruction.md` содержит инструкции по использованию, отображаемые хостом. Файлы README и CHANGELOG генерируются из JSON источников с помощью `.python/generate_markdown.py`.

******

### Ссылки

******

- Документация AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Официальный проект MediaInfo: https://mediaarea.net/en/MediaInfo
- Официальный проект MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Проект MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
