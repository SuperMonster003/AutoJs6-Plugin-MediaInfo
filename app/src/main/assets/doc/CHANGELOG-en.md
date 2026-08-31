******

### Release History

******

# v2.0.0

###### 2026/08/31

* `Feature` Official-source build: all four ABIs are generated directly from pinned MediaArea MediaInfoLib 26.05 and ZenLib 0.4.41 sources, replacing prebuilt libraries from the dormant personal repository
* `Feature` Reproducible provenance: upstream tags, full commits, NDK / CMake settings, and license texts are recorded in the lock file and every APK, with automated ELF and five-APK audits
* `Feature` Stable upstream tracking: weekly or manual checks propose pinned official Release updates through Draft PRs, detect moved tags, and never merge or publish automatically
* `Fix` Preserved the exact JNI wrapper class and methods through R8 and added a public-AIDL smoke test that installs the actual minified Release APK, preventing native loading failures in release builds
* `Improvement` MediaInfoLib 26.05 exposes richer codec, HDR / color, checksum, and cover-image metadata while preserving the public AIDL and `autojs6-plugin-mediainfo-snapshot-v1` contracts
* `Improvement` Every ABI supports 16 KB page sizes and passes gates across API 24-37, x86 / x86_64, ARM32 / ARM64, timeout, cache, real-media, and huge-file scenarios
* `Improvement` Full reports, field queries, and sections from 0.7.83 and 26.05 were reviewed on the same real samples; containers and core streams remain compatible while field text continues to follow upstream parsing
* `Dependency` Upgraded the frozen native parser from MediaInfoLib 0.7.83 to 26.05 and pinned ZenLib 0.4.41 with Android NDK 29.0.14206865

# v1.1.0

###### 2026/08/31

* `Feature` Copy-free parsing: seekable regular file descriptors are read directly by MediaInfoLib through `/proc/self/fd`; only pipes or failed direct parses use a private temporary copy
* `Feature` Process-local result cache: on API 27+, full reports, field queries, and snapshots use stable file identity, LRU eviction, a sliding 10-minute lifetime, and low-memory cleanup to avoid repeated parsing
* `Feature` Cooperative cancellation and timeout: every AIDL call has a 30-second limit; timeout stops native parsing or fallback copying, releases temporary resources, and returns `MEDIAINFO_TIMEOUT`
* `Fix` Fixed avoidable whole-file copying for large regular descriptors and ensured descriptors and fallback files close on success, failure, validation errors, cancellation, and timeout
* `Fix` Fixed cache identity precision for rapid file changes by using nanosecond mtime / ctime and conservatively disabling cache reuse on API 24-26
* `Improvement` Supported ABI reporting is generated dynamically from the `libmediainfo.so` files actually present in installed base or split APKs, with a safe process-ABI fallback
* `Improvement` Hardened snapshot section parsing for repeated and numbered streams, malformed lines, embedded colons, duplicate fields, and independent output options
* `Improvement` Added reproducible synthetic benchmark and real-media validation tooling, with complete x86, x86_64, and ARM64 performance baselines
* `Improvement` Rebuilt the 10-language README, plugin instructions, and changelog generation pipeline with drift validation and GitHub Actions gates

# v1.0.0

###### 2026/07/15

* `Feature` First stable release: brings MediaInfoLib-powered media file inspection to AutoJs6, fetching container format, codec, duration, resolution, bit rate, channels, and more in a single call
* `Feature` Script API: the Node environment gets async `read`/`get` via `require("mediainfo")`; the Rhino environment gets the global `mediainfo(path)` module returning a property-accessible parsed object synchronously
* `Feature` Three reading capabilities: full text report (`inform`), single parameter lookup (`get`), and structured JSON snapshot (`snapshot`, schema `autojs6-plugin-mediainfo-snapshot-v1`)
* `Feature` Discovered automatically by AutoJs6 through `org.autojs.plugin.MEDIAINFO`; the plugin receives and parses media content in its own process via read-only file descriptors, requesting no network or sensitive system permissions
* `Feature` Ships four single-ABI packages (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) plus an all-architecture `universal` package, with release filenames carrying version, ABI, and CRC32 digest
* `Feature` Plugin metadata, instructions, README, and changelog cover 10 languages: Simplified Chinese, Hong Kong Traditional Chinese, Taiwan Traditional Chinese, English, French, Spanish, Japanese, Korean, Russian, and Arabic
