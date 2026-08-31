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
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Idiomas (Languages)

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

El plugin MediaInfo (MediaInfo Plugin) aporta a AutoJs6 la lectura de información de archivos multimedia. Una vez instalado, una sola línea de script obtiene cientos de parámetros técnicos de archivos de video, audio e imagen, como formato del contenedor, códec, duración, resolución, tasa de bits y canales; el diálogo de información multimedia de la lista de archivos de AutoJs6 también muestra el informe completo proporcionado por este plugin. El análisis proviene de MediaInfoLib, la misma biblioteca de código abierto que impulsa la aplicación MediaInfo de escritorio.

El plugin se ejecuta en su propio proceso y AutoJs6 lo descubre automáticamente sin configuración alguna. Para leer un archivo, el host entrega el contenido multimedia como un descriptor de solo lectura. MediaInfoLib lee directamente los descriptores de archivos normales con acceso aleatorio mediante `/proc/self/fd`; los descriptores sin acceso aleatorio, como las tuberías, o un análisis directo fallido recurren a una copia temporal en la caché privada, eliminada al terminar la llamada. No interviene ningún acceso a la red y no se solicita ningún permiso sensible del sistema.

******

### Funciones Destacadas

******

- Listo para usar: sin configuración; AutoJs6 descubre el plugin automáticamente, y tanto los scripts como el diálogo de información multimedia de la lista de archivos pueden usarlo de inmediato.
- Información completa: formato del contenedor, códec, duración, resolución, velocidad de fotogramas, tasa de bits, canales, frecuencia de muestreo y más, todo en una sola llamada.
- Tres modos de lectura: informe de texto completo (`inform`), consulta de parámetro único (`get`) e instantánea JSON estructurada (`read`/`snapshot`), según convenga.
- Dos motores de script: el entorno Node lo llama de forma asíncrona mediante `require("mediainfo")`; el entorno Rhino usa el módulo global `mediainfo(path)` que devuelve de forma síncrona un objeto analizado accesible por propiedades.
- Amplia cobertura de formatos: el análisis lo realiza MediaInfoLib, la biblioteca del MediaInfo de escritorio, con soporte para gran cantidad de formatos de video, audio e imagen, comunes y poco frecuentes.
- Cinco paquetes APK: cuatro paquetes de una sola arquitectura (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) más un paquete `universal` todo en uno, para instalar solo lo necesario.
- Respetuoso con la privacidad: el análisis ocurre en un proceso aislado que solo recibe descriptores de archivo de solo lectura del host, sin permisos de red ni permisos sensibles del sistema.
- Multilingüe: metadatos del plugin, instrucciones, README y registro de cambios disponibles en 10 idiomas.

******

### Uso

******

1. Descargue el APK del plugin que coincida con el dispositivo desde la página [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) e instálelo en el dispositivo que ejecuta AutoJs6; en caso de duda, elija el paquete `universal` o consulte `Cómo Elegir Un APK` más abajo.
2. Abra el centro de plugins de AutoJs6 y confirme que el plugin `MediaInfo` está reconocido y habilitado.
3. Llame al módulo `mediainfo` en los scripts como se muestra en `API De Script` más abajo, o abra el diálogo de información multimedia de cualquier archivo en la lista de archivos de AutoJs6 para ver el informe completo directamente.

> Si el plugin no aparece en el centro de plugins, actualice primero AutoJs6 a una versión reciente (compilación interna 3923 o superior). El plugin en sí admite dispositivos con Android 7.0 (API 24) o superior.

******

### Cómo Elegir Un APK

******

Cada versión publica 5 APK que solo difieren en las arquitecturas de biblioteca nativa incluidas:

| Paquete | Ideal para |
|---|---|
| `arm64-v8a` | La gran mayoría de teléfonos y tabletas Android modernos (ARM de 64 bits); la primera opción |
| `armeabi-v7a` | Dispositivos ARM de 32 bits más antiguos |
| `x86_64` | Emuladores x86 de 64 bits y algunos dispositivos x86 |
| `x86` | Emuladores x86 de 32 bits y algunos dispositivos x86 |
| `universal` | Incluye las 4 arquitecturas y es el más grande; funciona en cualquier dispositivo y es la opción segura en caso de duda |

El plugin carga la biblioteca nativa dentro de su propio proceso. Si por error se instaló un paquete de una sola arquitectura que no coincide con el dispositivo, las llamadas fallan con `MediaInfo library is not available`; cambiar al paquete `universal` lo resuelve.

******

### API De Script

******

En el entorno Node (scripts que comienzan con la directiva `"nodejs"`), obtenga el módulo mediante `require("mediainfo")`; todos los métodos devuelven una Promise:

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

`read(path, options?)` devuelve un objeto instantánea estructurado (vea `Estructura De La Instantánea Y Opciones` más abajo); `get(path, streamKind?, parameter)` devuelve el texto sin procesar del parámetro, con `streamKind` por defecto `general`. Por seguridad, los scripts Node solo pueden acceder a archivos dentro del directorio del proyecto, y las rutas relativas se resuelven desde la raíz del proyecto.

En el entorno Rhino (el motor de script predeterminado de AutoJs6), `mediainfo` es un módulo global; `mediainfo(path)` y `mediainfo.read(path)` son equivalentes y devuelven de forma síncrona un objeto analizado:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

En el objeto devuelto, `path` e `inform` contienen la ruta resuelta y el informe de texto completo; cada tipo de flujo (como `general`, `video`, `audio`) funciona tanto como propiedad que expone los campos analizados (como `mi.video.width`, nombres de campo en camelCase) como función para consultar en vivo parámetros sin procesar (como `mi.audio("BitRate")`). Los scripts Rhino pueden acceder a cualquier ruta que el host tenga permiso de leer.

******

### Estructura De La Instantánea Y Opciones

******

La instantánea devuelta por `read()` en el entorno Node tiene este aspecto (el JSON devuelto por el método AIDL `snapshot` del plugin es idéntico salvo que su `schema` es `autojs6-plugin-mediainfo-snapshot-v1` y carece del campo `path`):

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

- `includeInform`: incluir o no el informe de texto `inform`, por defecto `true`; ponga `false` para obtener una cadena vacía y una carga menor.
- `includeSections`: analizar o no el informe en `sections`, por defecto `true`; ponga `false` para obtener un objeto vacío.

`sections` usa como claves los nombres de sección del informe en minúsculas (con varios flujos de un mismo tipo, los nombres de sección pueden llevar un índice como `audio #1`), y cada valor es un arreglo de objetos; los nombres de campo se convierten a camelCase mientras los valores conservan el texto original de MediaInfo, incluidas unidades y espacios separadores de miles como `1 920 pixels`.

******

### Tipos De Flujo

******

El parámetro `streamKind` de `get()` admite los siguientes tipos de flujo:

```text
general, video, audio, text, other, image, menu
```

`streamKind` no distingue mayúsculas de minúsculas y se asigna a los tipos de flujo nativos de MediaInfo; consultar un flujo inexistente o un parámetro sin valor devuelve una cadena vacía.

******

### Preguntas Frecuentes

******

#### ¿Cómo confirmo que el plugin funciona?

Abra el centro de plugins de AutoJs6; ver el plugin `MediaInfo` allí significa que el host lo ha reconocido. Luego ejecute cualquiera de los ejemplos de script de `mediainfo`; si los resultados vuelven con normalidad, el plugin funciona.

#### ¿Por qué no hay icono del plugin en la lista de aplicaciones?

Es lo esperado. El plugin no tiene interfaz propia ni crea icono de lanzador; tras la instalación, AutoJs6 lo descubre y lo controla por completo en segundo plano, y toda interacción ocurre dentro de AutoJs6.

#### ¿Un script Node falla con `path must stay inside the scoped working directory`?

Por seguridad, el motor Node solo permite acceder a archivos dentro del directorio del proyecto. Mueva o copie el archivo multimedia al directorio del proyecto antes de leerlo; para acceder a otras ubicaciones (como la galería o descargas), use en su lugar un script del motor Rhino.

#### ¿`get()` devolvió una cadena vacía?

El nombre del parámetro debe ser un parámetro nativo de MediaInfo (como `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), y el flujo objetivo debe existir realmente. Use primero `read()` para inspeccionar los campos realmente disponibles en `sections`, o consulte el informe `inform` completo.

#### ¿Leer archivos grandes es lento?

Los archivos normales ahora se analizan directamente sin una copia completa en la caché, por lo que los archivos grandes evitan un tiempo de copia lineal con su tamaño. Los descriptores sin acceso aleatorio, como las tuberías, o los formatos cuyo análisis directo falla aún usan una copia temporal cuyo costo depende de los bytes recibidos; el tiempo de análisis propio de MediaInfoLib sigue dependiendo del formato y el contenido.

#### ¿Se almacenan en caché los resultados y qué ocurre si se agota el tiempo?

Sí. En Android 8.1 (API 27) y versiones posteriores, para un archivo normal con identidad estable y sin cambios, el plugin guarda en la caché del proceso actual el informe, las consultas y las instantáneas: como máximo 32 archivos, 64 consultas por archivo, una vigencia deslizante de 10 minutos y unos 2 MiB de texto en total. La caché se desactiva de forma conservadora en API 24-26 porque no hay marcas de tiempo de archivo con precisión de nanosegundos; también se vacía cuando hay poca memoria o termina el proceso. Cada llamada AIDL tiene un límite de 30 segundos; al superarlo, se cancela cooperativamente el análisis nativo o la copia de respaldo, se elimina el archivo temporal y la excepción contiene `MEDIAINFO_TIMEOUT`.

#### El archivo tiene varias pistas de audio o subtítulos; ¿cómo leo la segunda y siguientes?

La instantánea `sections` conserva todas las secciones del informe (con varios flujos, los nombres de sección llevan un índice como `audio #2`), así que léalas desde allí; `get()` actualmente consulta siempre el primer flujo de cada tipo, y la selección por índice de flujo está planificada en [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### ¿El plugin accede a la red o solicita permisos sensibles?

No. Su manifiesto no contiene permisos de red, almacenamiento, cámara ni otros permisos sensibles del sistema; solo declara el permiso de plugin usado para comunicarse con AutoJs6. El contenido multimedia llega del host como descriptor de solo lectura, y la copia temporal creada para el análisis se elimina de inmediato.

******

### Permisos Y Seguridad

******

Los archivos multimedia pueden venir de fuentes no confiables, por lo que el diseño coloca varias líneas de defensa alrededor del análisis:

- Aislamiento de procesos: el análisis ocurre en el propio proceso del plugin y la biblioteca nativa nunca se inyecta en el proceso del host, de modo que incluso un fallo de análisis deja a AutoJs6 funcionando con normalidad.
- Superficie de datos mínima: el plugin no puede leer el almacenamiento del dispositivo por sí mismo; solo recibe un descriptor de archivo de solo lectura abierto por el host más un nombre para mostrar.
- Lectura directa cuando sea posible y limpieza al recurrir a la copia: los descriptores normales con acceso aleatorio no crean una copia del medio; solo la ruta de compatibilidad escribe en la caché privada y elimina el archivo temporal al terminar la llamada.
- Permisos mínimos: sin permisos de red, almacenamiento, cámara ni otros permisos sensibles del sistema; el servicio y la entrada de activación están protegidos por el permiso de plugin de AutoJs6 (`org.autojs.permission.PLUGIN`), por lo que las aplicaciones de terceros no pueden llamarlos directamente.
- Abierto y auditable: el código del plugin, los scripts de compilación y la cadena de generación de documentación son totalmente de código abierto, y el origen de la biblioteca nativa y del envoltorio JNI se indica en la sección de licencia.

Instale el plugin solo desde la página oficial [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) u otros canales de confianza; los paquetes de origen desconocido pueden estar alterados aunque el nombre y el número de versión parezcan idénticos.

******

### Interfaz Del Plugin

******

La siguiente información está dirigida a los desarrolladores del host AutoJs6 y de plugins; el host usa estos identificadores para descubrir el plugin y negociar capacidades:

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

`MediainfoPluginService` expone cuatro métodos, `getInfo`/`inform`/`get`/`snapshot`, a través de la interfaz AIDL `IMediainfoPlugin`; el contenido multimedia se pasa como `ParcelFileDescriptor` de solo lectura más un nombre para mostrar, y `snapshot` acepta además un `Bundle` de opciones con `includeInform`/`includeSections`. Tanto el servicio como `WakeActivity` están protegidos por el permiso `org.autojs.permission.PLUGIN`.

El plugin examina los APK base / split instalados e informa dinámicamente las ABI que realmente contienen `libmediainfo.so`; un paquete de una sola ABI informa solo la suya, mientras que `universal` informa las 4. Si no se pueden leer las rutas de los APK, usa de forma segura la arquitectura del proceso actual cuando existe una biblioteca nativa extraída.

******

### Hoja De Ruta

******

Las capacidades planificadas del plugin y su estado de finalización se mantienen como una lista marcable en ROADMAP.md, organizada por hitos con criterios de aceptación, cubriendo consultas por índice de flujo, análisis sin copia, informe dinámico de ABI, evolución de la biblioteca nativa e integración continua. Los elementos sin marcar son intenciones y no capacidades entregadas; la discusión mediante Issues es bienvenida.

- [Ver ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### Historial De Versiones

******

#### v2.0.0

_2026/08/31_

- `Función` Compilación desde fuentes oficiales: las cuatro ABI se generan directamente desde MediaArea MediaInfoLib 26.05 y ZenLib 0.4.41 fijados, sin las bibliotecas precompiladas del antiguo repositorio personal
- `Función` Procedencia reproducible: las etiquetas, commits completos, ajustes de NDK / CMake y textos de licencia se registran en el archivo de bloqueo y cada APK, con auditorías automáticas de ELF y los cinco APK
- `Función` Seguimiento de versiones estables: las comprobaciones semanales o manuales proponen actualizaciones oficiales fijadas mediante Draft PR, detectan etiquetas movidas y nunca fusionan ni publican automáticamente
- `Corrección` Se conservaron la clase JNI exacta y sus métodos con R8 y se añadió una prueba AIDL pública que instala el APK Release minificado real, evitando fallos de carga nativa en compilaciones de publicación
- `Mejora` MediaInfoLib 26.05 ofrece más metadatos de códec, HDR / color, suma de comprobación y carátula, manteniendo los contratos AIDL públicos y `autojs6-plugin-mediainfo-snapshot-v1`
- `Mejora` Cada ABI admite páginas de 16 KB y supera controles en API 24-37, x86 / x86_64, ARM32 / ARM64, tiempo límite, caché, medios reales y archivos enormes
- `Mejora` Se revisaron informes completos, consultas de campos y sections de 0.7.83 y 26.05 con las mismas muestras reales; los contenedores y flujos principales siguen siendo compatibles y el texto de los campos sigue el análisis upstream
- `Dependencia` Se actualizó el analizador nativo congelado de MediaInfoLib 0.7.83 a 26.05 y se fijaron ZenLib 0.4.41 y Android NDK 29.0.14206865

#### v1.1.0

_2026/08/31_

- `Función` Análisis sin copiar el archivo completo: los descriptores de archivos normales con acceso aleatorio se leen directamente desde MediaInfoLib mediante /proc/self/fd; solo las tuberías o los fallos de lectura directa usan una copia temporal privada
- `Función` Caché de resultados dentro del proceso: en API 27+, los informes completos, las consultas de campos y las instantáneas usan una identidad de archivo estable, LRU, caducidad deslizante de 10 minutos y limpieza por baja memoria
- `Función` Cancelación cooperativa y tiempo límite: cada llamada AIDL tiene un límite de 30 segundos; al superarlo se detiene el análisis nativo o la copia alternativa, se liberan los recursos temporales y se devuelve MEDIAINFO_TIMEOUT
- `Corrección` Se eliminó la copia sistemática del archivo multimedia completo y se garantiza el cierre de descriptores, analizadores nativos, flujos y archivos temporales en todas las rutas de error
- `Corrección` La identidad de caché conserva marcas de tiempo con nanosegundos y la caché se desactiva en API 24 a 26, donde esa información no puede validarse de forma segura
- `Mejora` El inventario dinámico de ABI comprueba las bibliotecas MediaInfoLib realmente empaquetadas y mantiene coherentes el informe de ejecución, los metadatos y las cinco variantes APK
- `Mejora` El analizador de instantáneas tolera mejor etiquetas localizadas, grupos repetidos, campos desconocidos y salidas parciales de MediaInfoLib
- `Mejora` Se añadieron herramientas de benchmark reproducibles para llamadas en frío y en caliente, concurrencia, tiempos límite y validación con medios reales, con manifiesto de fuentes y resumen SHA-256
- `Mejora` La generación documental validada ahora cubre 10 idiomas y produce de forma determinista README, instrucciones integradas y registros de cambios

#### v1.0.0

_2026/07/15_

- `Función` Primera versión estable: aporta a AutoJs6 la lectura de información de archivos multimedia mediante MediaInfoLib, obteniendo formato del contenedor, códec, duración, resolución, tasa de bits, canales y más en una sola llamada
- `Función` API de script: el entorno Node recibe `read`/`get` asíncronos mediante `require("mediainfo")`; el entorno Rhino recibe el módulo global `mediainfo(path)` que devuelve de forma síncrona un objeto analizado accesible por propiedades
- `Función` Tres capacidades de lectura: informe de texto completo (`inform`), consulta de parámetro único (`get`) e instantánea JSON estructurada (`snapshot`, esquema `autojs6-plugin-mediainfo-snapshot-v1`)
- `Función` Descubierto automáticamente por AutoJs6 mediante `org.autojs.plugin.MEDIAINFO`; el plugin recibe y analiza el contenido multimedia en su propio proceso mediante descriptores de archivo de solo lectura, sin permisos de red ni permisos sensibles del sistema
- `Función` Incluye cuatro paquetes de una sola arquitectura (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) más un paquete `universal` con todas las arquitecturas, con nombres de archivo de publicación que llevan versión, ABI y resumen CRC32
- `Función` Los metadatos del plugin, las instrucciones, README y el registro de cambios cubren 10 idiomas: chino simplificado, chino tradicional de Hong Kong, chino tradicional de Taiwan, inglés, francés, español, japonés, coreano, ruso y árabe

##### Para más historial de versiones

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-es.md)

******

### Compilación

******

Esta sección está dirigida a desarrolladores que quieran compilar el plugin desde el código fuente.

Clone el repositorio junto con los dos submódulos oficiales fijados antes de compilar:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

Compilar los APK debug:

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilar los APK release (los splits por ABI están habilitados, produciendo 4 paquetes de una sola arquitectura más 1 paquete `universal` de una vez; configure el archivo no rastreado `sign.properties` para la firma automática):

```powershell
.\gradlew.bat :app:assembleRelease
```

Para archivar publicaciones, ejecute la tarea `:app:appendDigestToReleasedFiles`, que copia los APK de `app/release` a `app/releases` y los renombra con el patrón `autojs6-plugin-mediainfo-v2.0.0-<abi>-<crc32>.apk`.

Los parámetros de compilación están centralizados en `version.properties`: SDK mínimo 24 (Android 7.0), SDK objetivo 36, versión actual 2.0.0.

******

### Localización Y Generación De Docs

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

`strings.xml` contiene la descripción localizada del plugin y los mensajes de error, y `plugin_instruction.md` contiene las instrucciones mostradas en el centro de plugins del host. README, registro de cambios e instrucciones se generan desde fuentes JSON: edite las fuentes bajo `.readme/` y `.changelog/`, luego ejecute `py .python/generate_markdown.py` para regenerar todos los artefactos; los artefactos generados nunca se editan a mano. Ejecute `py .python/generate_markdown.py --check` para verificar que fuentes y artefactos están sincronizados (la CI también lo comprueba automáticamente).

******

### Licencia

******

El código del proyecto está licenciado bajo la [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). En la línea v2 compilada desde fuentes, `libmediainfo.so` se genera desde [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) y [ZenLib](https://github.com/MediaArea/ZenLib) (licencia zlib) oficiales, mientras el puente JNI compatible se mantiene en este repositorio. La procedencia del binario v1.1.0 congelado se documenta por separado.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### Enlaces

******

- Documentación de AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- Sitio web oficial de MediaInfo: https://mediaarea.net/en/MediaInfo
- Proyecto MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- Envoltorio MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
