El plugin MediaInfo (MediaInfo Plugin) aporta a AutoJs6 la lectura de información de archivos multimedia. Una vez instalado, una sola línea de script obtiene cientos de parámetros técnicos de archivos de video, audio e imagen, como formato del contenedor, códec, duración, resolución, tasa de bits y canales; el diálogo de información multimedia de la lista de archivos de AutoJs6 también muestra el informe completo proporcionado por este plugin. El análisis proviene de MediaInfoLib, la misma biblioteca de código abierto que impulsa la aplicación MediaInfo de escritorio.

### Uso

1. Descargue el APK del plugin que coincida con el dispositivo desde la página [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) e instálelo en el dispositivo que ejecuta AutoJs6; en caso de duda, elija el paquete `universal` o consulte `Cómo Elegir Un APK` más abajo.
2. Abra el centro de plugins de AutoJs6 y confirme que el plugin `MediaInfo` está reconocido y habilitado.
3. Llame al módulo `mediainfo` en los scripts como se muestra en `API De Script` más abajo, o abra el diálogo de información multimedia de cualquier archivo en la lista de archivos de AutoJs6 para ver el informe completo directamente.

Si el plugin no aparece en el centro de plugins, actualice primero AutoJs6 a una versión reciente (compilación interna 3923 o superior). El plugin en sí admite dispositivos con Android 7.0 (API 24) o superior.

### API De Script

En el entorno Node (scripts que comienzan con la directiva `"nodejs"`), obtenga el módulo mediante `require("mediainfo")`; todos los métodos devuelven una Promise:

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

`read(path, options?)` devuelve un objeto instantánea estructurado (vea `Estructura De La Instantánea Y Opciones` más abajo); `get(path, streamKind?, parameter)` devuelve el texto sin procesar del parámetro, con `streamKind` por defecto `general`. Por seguridad, los scripts Node solo pueden acceder a archivos dentro del directorio del proyecto, y las rutas relativas se resuelven desde la raíz del proyecto.

En el entorno Rhino (el motor de script predeterminado de AutoJs6), `mediainfo` es un módulo global; `mediainfo(path)` y `mediainfo.read(path)` son equivalentes y devuelven de forma síncrona un objeto analizado:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

En el objeto devuelto, `path` e `inform` contienen la ruta resuelta y el informe de texto completo; cada tipo de flujo (como `general`, `video`, `audio`) funciona tanto como propiedad que expone los campos analizados (como `mi.video.width`, nombres de campo en camelCase) como función para consultar en vivo parámetros sin procesar (como `mi.audio("BitRate")`). Los scripts Rhino pueden acceder a cualquier ruta que el host tenga permiso de leer.

### Tipos De Flujo

El parámetro `streamKind` de `get()` admite los siguientes tipos de flujo:

```text
general, video, audio, text, other, image, menu
```

`streamKind` no distingue mayúsculas de minúsculas y se asigna a los tipos de flujo nativos de MediaInfo; consultar un flujo inexistente o un parámetro sin valor devuelve una cadena vacía.

Para más detalles de uso y referencia de campos, consulte la [documentación de AutoJs6 MediaInfo](https://docs.autojs6.com/#/mediainfo) y la [página del proyecto](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
