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
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Языки (Languages)

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

Плагин MediaInfo (MediaInfo Plugin) добавляет в AutoJs6 чтение информации о медиафайлах. После установки одна строка скрипта получает сотни технических параметров видео, аудио и изображений: формат контейнера, кодек, длительность, разрешение, битрейт, каналы и многое другое; диалог медиаинформации в списке файлов AutoJs6 также показывает полный отчет, подготовленный этим плагином. Разбор выполняет MediaInfoLib, та же библиотека с открытым исходным кодом, что лежит в основе настольного MediaInfo.

Плагин работает в собственном процессе и обнаруживается AutoJs6 автоматически, без какой-либо настройки. Для чтения файла хост передает медиаконтент как файловый дескриптор только для чтения. Обычные дескрипторы с произвольным доступом MediaInfoLib читает напрямую через `/proc/self/fd`; для непозиционируемых дескрипторов, например каналов, или при неудачном прямом разборе создается временная копия в приватном кеше, которая удаляется сразу после вызова. Сеть не используется ни на одном этапе, и никакие чувствительные системные разрешения не запрашиваются.

******

### Основные Возможности

******

- Работает из коробки: настройка не требуется; AutoJs6 обнаруживает плагин автоматически, и скрипты вместе с диалогом медиаинформации в списке файлов могут использовать его сразу.
- Полная информация: формат контейнера, кодек, длительность, разрешение, частота кадров, битрейт, каналы, частота дискретизации и другое, все за один вызов.
- Три режима чтения: полный текстовый отчет (`inform`), запрос отдельного параметра (`get`) и структурированный JSON снимок (`read`/`snapshot`), на выбор.
- Два скриптовых движка: среда Node вызывает асинхронно через `require("mediainfo")`; среда Rhino использует глобальный модуль `mediainfo(path)`, синхронно возвращающий разобранный объект с доступом по свойствам.
- Широкий охват форматов: разбор выполняет MediaInfoLib, библиотека настольного MediaInfo, поддерживающая множество распространенных и редких видео, аудио и графических форматов.
- Пять вариантов APK: четыре пакета под одну архитектуру (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) плюс пакет `universal` со всеми архитектурами, чтобы устанавливать только нужное.
- Бережно к приватности: разбор происходит в изолированном процессе, который получает от хоста лишь дескрипторы файлов только для чтения, без сетевых и чувствительных системных разрешений.
- Многоязычность: метаданные плагина, инструкции, README и журнал изменений доступны на 10 языках.

******

### Использование

******

1. Скачайте APK плагина, подходящий устройству, со страницы [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) и установите его на устройство с AutoJs6; если не уверены, возьмите пакет `universal` или смотрите `Выбор APK` ниже.
2. Откройте центр плагинов AutoJs6 и убедитесь, что плагин `MediaInfo` распознан и включен.
3. Вызывайте модуль `mediainfo` в скриптах, как показано в разделе `API Скриптов` ниже, или откройте диалог медиаинформации любого медиафайла в списке файлов AutoJs6, чтобы сразу увидеть полный отчет.

> Если плагин не появляется в центре плагинов, сначала обновите AutoJs6 до свежей версии (внутренняя сборка 3923 или выше). Сам плагин поддерживает устройства с Android 7.0 (API 24) и выше.

******

### Выбор APK

******

Каждый выпуск содержит 5 APK, различающихся только набором архитектур нативной библиотеки:

| Пакет | Подходит для |
|---|---|
| `arm64-v8a` | Подавляющее большинство современных Android телефонов и планшетов (64-битный ARM); первый выбор |
| `armeabi-v7a` | Более старые 32-битные ARM устройства |
| `x86_64` | 64-битные x86 эмуляторы и немногие x86 устройства |
| `x86` | 32-битные x86 эмуляторы и немногие x86 устройства |
| `universal` | Содержит все 4 архитектуры и является самым большим; работает на любом устройстве, надежный выбор при сомнениях |

Плагин загружает нативную библиотеку в собственном процессе. Если по ошибке установлен пакет под одну архитектуру, не совпадающую с устройством, вызовы завершаются ошибкой `MediaInfo library is not available`; переход на пакет `universal` решает проблему.

******

### API Скриптов

******

В среде Node (скрипты, начинающиеся с директивы `"nodejs"`) модуль получают через `require("mediainfo")`; все методы возвращают Promise:

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

`read(path, options?)` возвращает структурированный снимок (см. ниже); `get(path, streamKind, parameter, options?)` возвращает исходный текст параметра. Относительные пути разрешаются от рабочего каталога; абсолютные пути и родительские каталоги также поддерживаются, если Android разрешает приложению их читать. Передавайте путь к файлу, а не URI content.

В среде Rhino (скриптовый движок AutoJs6 по умолчанию) `mediainfo` является глобальным модулем; `mediainfo(path)` и `mediainfo.read(path)` эквивалентны и синхронно возвращают разобранный объект:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

У возвращаемого объекта `path` и `inform` содержат разрешенный путь и полный текстовый отчет; каждый тип потока (например `general`, `video`, `audio`) работает и как свойство с разобранными полями (например `mi.video.width`, имена полей в camelCase), и как функция для живого запроса исходных параметров (например `mi.audio("BitRate")`). Скрипты Rhino могут обращаться к любому пути, доступному хосту для чтения.

Запросы MediaInfo поддерживают streamNumber с нуля, подсчет потоков countGet и infoKind для единиц, описаний и читаемых имен; Rhino и Node сохраняют TEXT первого потока по умолчанию и согласуют возможности плагина:

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

### Демонстрация

******

Реальные экраны AutoJs6 в светлой и тёмной темах. Пример Rhino читает две дорожки PCM с частотой 8 kHz и 16 kHz; кнопка MediaInfo открывает полный отчёт с исходным путём к файлу.

<table>
  <tr>
    <th>Вывод скрипта Rhino</th>
    <th>Информация о медиафайле</th>
    <th>Подробности MediaInfo</th>
  </tr>
  <tr>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-script.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-script.png?raw=true" alt="Вывод скрипта Rhino" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-dialog.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-dialog.png?raw=true" alt="Информация о медиафайле" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-details.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-details.png?raw=true" alt="Подробности MediaInfo" width="260" /></picture></td>
  </tr>
</table>

Запустите [демонстрационный скрипт](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/demo/mediainfo.js) с [образцом из двух дорожек](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/androidTest/assets/mediainfo-two-audio.mka) или укажите свой медиафайл. Образец содержит синтезированную тишину. Исходный путь и расширенные запросы требуют согласованных обновлений приложения и плагинов.

******

### Структура Снимка И Параметры

******

Снимок, возвращаемый `read()` в среде Node, выглядит так (JSON, возвращаемый AIDL методом `snapshot` плагина, идентичен, но его `schema` равна `autojs6-plugin-mediainfo-snapshot-v1` и поле `path` отсутствует):

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

- `includeInform`: включать ли текстовый отчет `inform`, по умолчанию `true`; установите `false`, чтобы получить пустую строку и уменьшить объем ответа.
- `includeSections`: разбирать ли отчет в `sections`, по умолчанию `true`; установите `false`, чтобы получить пустой объект.

Ключами `sections` служат имена разделов отчета в нижнем регистре (при нескольких потоках одного типа имена разделов могут содержать номер, например `audio #1`), а каждое значение является массивом объектов; имена полей преобразуются в camelCase, а значения сохраняют исходный текст MediaInfo, включая единицы измерения и пробелы-разделители тысяч, например `1 920 pixels`.

******

### Типы Потоков

******

Параметр `streamKind` метода `get()` поддерживает следующие типы потоков:

```text
general, video, audio, text, other, image, menu
```

`streamKind` не зависит от регистра и сопоставляется с нативными типами потоков MediaInfo; запрос несуществующего потока или параметра без значения возвращает пустую строку.

******

### Частые Вопросы

******

#### Как убедиться, что плагин работает?

Откройте центр плагинов AutoJs6; если там виден плагин `MediaInfo`, хост его распознал. Затем запустите любой пример скрипта `mediainfo`; нормально возвращаемые результаты означают, что плагин работает.

#### Почему в списке приложений нет значка плагина?

Так и задумано. У плагина нет собственного интерфейса, и он не создает значок запуска; после установки его полностью обнаруживает и использует AutoJs6 в фоне, а все взаимодействие происходит внутри AutoJs6.

#### Скрипт Node сообщает `path must stay inside the scoped working directory`?

Обновите AutoJs6 и плагин Node Runtime. Текущие версии принимают обычные пути вне проекта с учётом разрешений Android. Старые версии приложения или среды выполнения могут сохранять прежнее ограничение каталогом проекта.

#### `get()` вернул пустую строку?

Имя параметра должно быть нативным параметром MediaInfo (например `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), а целевой поток должен реально существовать. Сначала используйте `read()`, чтобы посмотреть фактически доступные поля в `sections`, или изучите полный отчет `inform`.

#### Чтение больших файлов идет медленно?

Обычные файлы теперь разбираются напрямую без полной копии в кеш, поэтому большие файлы не требуют времени копирования, линейного по их размеру. Непозиционируемые дескрипторы, например каналы, или форматы с неудачным прямым разбором по-прежнему используют временную копию, стоимость которой зависит от числа полученных байтов; собственное время анализа MediaInfoLib зависит от формата и содержимого.

#### Кэшируются ли результаты и что происходит при превышении времени?

Да. На Android 8.1 (API 27) и новее для обычного неизменившегося файла со стабильным идентификатором плагин кэширует отчет, запросы и снимки в текущем процессе: не более 32 файлов, до 64 запросов на файл, скользящий срок 10 минут и около 2 MiB текста суммарно. На API 24-26 кэш консервативно отключен из-за отсутствия файловых меток времени с наносекундной точностью; он также очищается при нехватке памяти или завершении процесса. Каждый вызов AIDL ограничен 30 секундами; при превышении лимита нативный анализ или резервное копирование совместно отменяется, временный файл удаляется, а исключение содержит `MEDIAINFO_TIMEOUT`.

#### В файле несколько аудиодорожек или субтитров; как прочитать вторую и последующие?

После обновления хоста и Node запрос get(path, "audio", "Format", {streamNumber: 1}) читает второй аудиопоток. countGet(path, "audio") считает потоки, infoKind: "MEASURE" возвращает единицу. Проверьте capabilities() и [контракт](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_QUERY.md).

#### Обращается ли плагин к сети или запрашивает чувствительные разрешения?

Нет. Его манифест не содержит разрешений сети, хранилища, камеры и других чувствительных системных разрешений; объявлено только разрешение плагина для связи с AutoJs6. Медиаконтент приходит от хоста как дескриптор только для чтения, а временная копия для разбора сразу удаляется.

******

### Интерфейс Плагина

******

Следующая информация предназначена разработчикам хоста AutoJs6 и плагинов; хост использует эти идентификаторы для обнаружения плагина и согласования возможностей:

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

`MediainfoPluginService` предоставляет шесть методов, `getInfo`/`inform`/`get`/`snapshot`/`getDetail`/`countGet`, через AIDL интерфейс `IMediainfoPlugin`; медиаконтент передается как `ParcelFileDescriptor` только для чтения плюс отображаемое имя, а `snapshot` дополнительно принимает `Bundle` с параметрами `includeInform`/`includeSections`. Сервис и `WakeActivity` защищены разрешением `org.autojs.permission.PLUGIN`.

Для анализа медиафайлов используются встроенные нативные библиотеки MediaInfoLib.

******

### Дорожная Карта

******

Запланированные возможности плагина и их статус ведутся в ROADMAP.md как отмечаемый список, организованный по вехам с критериями приемки и охватывающий запросы по номеру потока, разбор без копирования, динамическое определение ABI, эволюцию нативной библиотеки и непрерывную интеграцию. Неотмеченные пункты являются намерениями, а не готовыми возможностями; обсуждение через Issues приветствуется.

- [Открыть ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### История Выпусков

******

#### v2.1.1

_2026/09/11_

- `Улучшение` Проверка выравнивания страниц 16 KB для 64-битных нативных библиотек при сборке, включая контракт manifest и отчеты JSON

#### v2.1.0

_2026/09/10_

- `Функция` Запросы MediaInfo поддерживают streamNumber с нуля, подсчет потоков countGet и infoKind для единиц, описаний и читаемых имен; Rhino и Node сохраняют TEXT первого потока по умолчанию и согласуют возможности плагина
- `Функция` Явно выбранная схема snapshot v2 группирует потоки из нативного JSON в массивы и предоставляет версию движка, сохраняя snapshot v1 по умолчанию
- `Исправление` Complete name в подробностях и снимках MediaInfo показывает исходный путь вместо приватного кеша или дескриптора, сохраняя отображаемое имя файла снимка
- `Улучшение` Обновлено описание путей Node и добавлены реальные снимки светлой/тёмной тем с запускаемым примером двух дорожек

#### v2.0.0

_2026/09/01_

- `Функция` Сборка из официальных исходников: все четыре ABI создаются непосредственно из закрепленных MediaArea MediaInfoLib 26.05 и ZenLib 0.4.41 без готовых библиотек из устаревшего личного репозитория
- `Функция` Воспроизводимое происхождение: теги, полные коммиты, настройки NDK / CMake и тексты лицензий записываются в файл блокировки и каждый APK, а ELF и пять APK проверяются автоматически
- `Функция` Отслеживание стабильных версий: еженедельная или ручная проверка предлагает закрепленные обновления официальных Release только через Draft PR, обнаруживает перенос тегов и никогда не выполняет автоматическое слияние или публикацию
- `Исправление` Точный класс оболочки JNI и его методы сохраняются после R8, а публичный AIDL smoke-тест устанавливает настоящий minified Release APK и предотвращает сбои загрузки нативной библиотеки в релизной сборке
- `Улучшение` MediaInfoLib 26.05 предоставляет больше метаданных о кодеках, HDR / цвете, контрольных суммах и обложках, сохраняя публичный AIDL и контракт `autojs6-plugin-mediainfo-snapshot-v1`
- `Улучшение` Все ABI поддерживают страницы 16 KB и проходят проверки на API 24-37, x86 / x86_64, ARM32 / ARM64, тайм-ауты, кеш, реальные медиа и огромные файлы
- `Улучшение` Полные отчеты, запросы полей и sections версий 0.7.83 и 26.05 проверены на одинаковых реальных образцах; контейнеры и основные потоки совместимы, а текст полей следует анализу upstream
- `Улучшение` Унифицировать оформление README и управление версиями платформы Gradle
- `Зависимость` Зафиксированный нативный анализатор обновлен с MediaInfoLib 0.7.83 до 26.05, закреплены ZenLib 0.4.41 и Android NDK 29.0.14206865

##### Больше истории выпусков

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ru.md)

******

### Сборка

******

Этот раздел предназначен разработчикам, желающим собрать плагин из исходников.

Перед сборкой клонируйте репозиторий вместе с двумя закрепленными официальными подмодулями:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

Собрать debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

Сборка release APK:

```powershell
.\gradlew.bat :app:assembleRelease
```

Для архивирования выпуска запустите задачу `:app:appendDigestToReleasedFiles`, которая копирует APK из `app/release` в `app/releases` и переименовывает их по шаблону `autojs6-plugin-mediainfo-v2.1.1-<abi>-<crc32>.apk`.

Параметры сборки собраны в `version.properties`: минимальный SDK 24 (Android 7.0), целевой SDK 36, текущая версия 2.1.1.

******

### Локализация И Генерация Документации

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

`strings.xml` содержит локализованное описание плагина и сообщения об ошибках, а `plugin_instruction.md` содержит инструкции, отображаемые в центре плагинов хоста. README, журнал изменений и инструкции генерируются из JSON источников: отредактируйте источники в `.readme/` и `.changelog/`, затем выполните `py .python/generate_markdown.py`, чтобы пересоздать все артефакты; сгенерированные артефакты никогда не правятся вручную. Команда `py .python/generate_markdown.py --check` проверяет синхронность источников и артефактов (CI также проверяет это автоматически).

******

### Лицензия

******

Код проекта распространяется под лицензией [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). В ветке v2 `libmediainfo.so` собирается из официальных исходников [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) и [ZenLib](https://github.com/MediaArea/ZenLib) (лицензия zlib), а совместимый JNI-мост поддерживается в этом репозитории. Происхождение замороженного бинарного файла v1.1.0 описано отдельно.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### Ссылки

******

- Документация AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Официальный сайт MediaInfo: https://mediaarea.net/en/MediaInfo
- Проект MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Обертка MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/docs/16kb.md)
