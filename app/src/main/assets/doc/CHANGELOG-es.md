******

### Historial De Versiones

******

# v2.0.0

###### 2026/08/31

* `Función` Compilación desde fuentes oficiales: las cuatro ABI se generan directamente desde MediaArea MediaInfoLib 26.05 y ZenLib 0.4.41 fijados, sin las bibliotecas precompiladas del antiguo repositorio personal
* `Función` Procedencia reproducible: las etiquetas, commits completos, ajustes de NDK / CMake y textos de licencia se registran en el archivo de bloqueo y cada APK, con auditorías automáticas de ELF y los cinco APK
* `Función` Seguimiento de versiones estables: las comprobaciones semanales o manuales proponen actualizaciones oficiales fijadas mediante Draft PR, detectan etiquetas movidas y nunca fusionan ni publican automáticamente
* `Corrección` Se conservaron la clase JNI exacta y sus métodos con R8 y se añadió una prueba AIDL pública que instala el APK Release minificado real, evitando fallos de carga nativa en compilaciones de publicación
* `Mejora` MediaInfoLib 26.05 ofrece más metadatos de códec, HDR / color, suma de comprobación y carátula, manteniendo los contratos AIDL públicos y `autojs6-plugin-mediainfo-snapshot-v1`
* `Mejora` Cada ABI admite páginas de 16 KB y supera controles en API 24-37, x86 / x86_64, ARM32 / ARM64, tiempo límite, caché, medios reales y archivos enormes
* `Mejora` Se revisaron informes completos, consultas de campos y sections de 0.7.83 y 26.05 con las mismas muestras reales; los contenedores y flujos principales siguen siendo compatibles y el texto de los campos sigue el análisis upstream
* `Dependencia` Se actualizó el analizador nativo congelado de MediaInfoLib 0.7.83 a 26.05 y se fijaron ZenLib 0.4.41 y Android NDK 29.0.14206865

# v1.1.0

###### 2026/08/31

* `Función` Análisis sin copiar el archivo completo: los descriptores de archivos normales con acceso aleatorio se leen directamente desde MediaInfoLib mediante /proc/self/fd; solo las tuberías o los fallos de lectura directa usan una copia temporal privada
* `Función` Caché de resultados dentro del proceso: en API 27+, los informes completos, las consultas de campos y las instantáneas usan una identidad de archivo estable, LRU, caducidad deslizante de 10 minutos y limpieza por baja memoria
* `Función` Cancelación cooperativa y tiempo límite: cada llamada AIDL tiene un límite de 30 segundos; al superarlo se detiene el análisis nativo o la copia alternativa, se liberan los recursos temporales y se devuelve MEDIAINFO_TIMEOUT
* `Corrección` Se eliminó la copia sistemática del archivo multimedia completo y se garantiza el cierre de descriptores, analizadores nativos, flujos y archivos temporales en todas las rutas de error
* `Corrección` La identidad de caché conserva marcas de tiempo con nanosegundos y la caché se desactiva en API 24 a 26, donde esa información no puede validarse de forma segura
* `Mejora` El inventario dinámico de ABI comprueba las bibliotecas MediaInfoLib realmente empaquetadas y mantiene coherentes el informe de ejecución, los metadatos y las cinco variantes APK
* `Mejora` El analizador de instantáneas tolera mejor etiquetas localizadas, grupos repetidos, campos desconocidos y salidas parciales de MediaInfoLib
* `Mejora` Se añadieron herramientas de benchmark reproducibles para llamadas en frío y en caliente, concurrencia, tiempos límite y validación con medios reales, con manifiesto de fuentes y resumen SHA-256
* `Mejora` La generación documental validada ahora cubre 10 idiomas y produce de forma determinista README, instrucciones integradas y registros de cambios

# v1.0.0

###### 2026/07/15

* `Función` Primera versión estable: aporta a AutoJs6 la lectura de información de archivos multimedia mediante MediaInfoLib, obteniendo formato del contenedor, códec, duración, resolución, tasa de bits, canales y más en una sola llamada
* `Función` API de script: el entorno Node recibe `read`/`get` asíncronos mediante `require("mediainfo")`; el entorno Rhino recibe el módulo global `mediainfo(path)` que devuelve de forma síncrona un objeto analizado accesible por propiedades
* `Función` Tres capacidades de lectura: informe de texto completo (`inform`), consulta de parámetro único (`get`) e instantánea JSON estructurada (`snapshot`, esquema `autojs6-plugin-mediainfo-snapshot-v1`)
* `Función` Descubierto automáticamente por AutoJs6 mediante `org.autojs.plugin.MEDIAINFO`; el plugin recibe y analiza el contenido multimedia en su propio proceso mediante descriptores de archivo de solo lectura, sin permisos de red ni permisos sensibles del sistema
* `Función` Incluye cuatro paquetes de una sola arquitectura (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) más un paquete `universal` con todas las arquitecturas, con nombres de archivo de publicación que llevan versión, ABI y resumen CRC32
* `Función` Los metadatos del plugin, las instrucciones, README y el registro de cambios cubren 10 idiomas: chino simplificado, chino tradicional de Hong Kong, chino tradicional de Taiwan, inglés, francés, español, japonés, coreano, ruso y árabe
