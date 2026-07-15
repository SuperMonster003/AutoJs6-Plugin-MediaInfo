<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Plugin MediaInfo para leer información de archivos multimedia</p>

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

### Idiomas

******

El README.md actual admite los siguientes idiomas:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- Español [es] # actual
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### Introducción

******

El plugin AutoJs6 MediaInfo proporciona lectura de metadatos multimedia basada en MediaInfo para AutoJs6. Puede producir informes completos, consultar parámetros individuales y generar instantáneas JSON estructuradas.

******

### Funciones

******

- Proporciona el servicio de plugin `mediainfo` con ID de plugin `mediainfo`.
- Admite `mediainfo.read(path, options)` y `mediainfo.get(path, streamKind, parameter, options)` en el entorno AutoJs6 Node.
- Admite descubrimiento e invocación desde el host mediante `org.autojs.plugin.MEDIAINFO`, con `inform`, `get` y `snapshot` expuestos por la interfaz AIDL subyacente.
- Incluye `libmediainfo.so` para `arm64-v8a`, `armeabi-v7a`, `x86_64` y `x86`.
- Los metadatos del plugin, las instrucciones de uso, README y changelog están localizados en español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwan.

******

### Uso

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

La ruta debe apuntar a un archivo multimedia al que el host pueda acceder. En el entorno Node, se admiten rutas relativas dentro del directorio de trabajo.

******

### Tipos De Flujo

******

Los tipos de flujo MediaInfo admitidos incluyen:

```text
general, video, audio, text, other,
image, menu
```

`streamKind` en `mediainfo.get()` no distingue mayúsculas y minúsculas y se asigna a los tipos de flujo nativos de MediaInfo.

******

### Opciones De Instantánea

******

- `includeInform`: incluye el informe de texto MediaInfo en la instantánea, valor predeterminado `true`.
- `includeSections`: analiza el informe en `sections`, valor predeterminado `true`.

******

### Historial De Versiones

******

# v1.0.0

###### 2026/07/15

* `Función` Se agregó el servicio de plugin MediaInfo con ID de plugin `mediainfo` y motor `mediainfo`
* `Función` Se agregó descubrimiento e invocación desde el host mediante `org.autojs.plugin.MEDIAINFO`
* `Función` Se agregaron las capacidades `inform`, `get` y `snapshot` para informes multimedia completos, consulta de parámetros individuales e instantáneas JSON estructuradas
* `Función` Se incluyó `libmediainfo.so` para `arm64-v8a`, `armeabi-v7a`, `x86_64` y `x86`, con una variante APK `universal`
* `Función` Se agregaron metadatos ABI admitidos a la información de ejecución del plugin y nombres de APK de publicación con versión, variante ABI y resumen CRC32
* `Función` Se agregaron metadatos del plugin, instrucciones de uso, README y changelog localizados en español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwan

##### Para más historial de versiones

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-es.md)

******

### Compilación

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilación Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Los parámetros de compilación provienen de `version.properties`; el SDK mínimo actual es 24 y el SDK objetivo es 36.

******

### Estructura De Recursos

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` contiene descripciones localizadas del plugin y mensajes de error; `plugin_instruction.md` contiene instrucciones de uso mostradas por el host. Los archivos README y CHANGELOG se generan desde fuentes JSON mediante `.python/generate_markdown.py`.

******

### Enlaces

******

- Documentación de AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Proyecto oficial MediaInfo: https://mediaarea.net/en/MediaInfo
- Proyecto oficial MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Proyecto MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
