# Recorded benchmark results

The following full-profile results were recorded on 2026-08-31. Values are medians of three end-to-end iterations. The source tree was not yet committed, so the raw artifacts record both `source.dirty: true` and a deterministic `source.benchmarkInputsSha256` for exact identification.

| Android target | Format | Direct 1 MiB | Direct 1 GiB | Direct growth for 1024x size | Cache 1 GiB | Pipe 1 MiB | Pipe 256 MiB |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| API 36 x86_64 | PCM/WAVE | 14.288 ms | 22.213 ms | 1.55x | 0.088 ms | 16.768 ms | 2424.682 ms |
| API 36 x86_64 | RGB/BMP | 50.210 ms | 68.687 ms | 1.37x | 0.181 ms | 72.594 ms | 1140.365 ms |
| API 29 x86 | PCM/WAVE | 8.932 ms | 31.925 ms | 3.57x | 0.116 ms | 13.726 ms | 1225.032 ms |
| API 29 x86 | RGB/BMP | 67.888 ms | 75.120 ms | 1.11x | 0.239 ms | 69.782 ms | 980.627 ms |
| API 31 ARM64 | PCM/WAVE | 8.513 ms | 32.653 ms | 3.84x | 0.221 ms | 12.568 ms | 818.077 ms |
| API 31 ARM64 | RGB/BMP | 67.480 ms | 87.900 ms | 1.30x | 0.137 ms | 72.037 ms | 1236.030 ms |

Raw artifacts:

- [API 36 x86_64 full profile](2026-08-31-api36-x86_64-full.json)
- [API 29 x86 full profile](2026-08-31-api29-x86-full.json)
- [API 31 ARM64 full profile](2026-08-31-api31-arm64-v8a-full.json)
- [API 31 ARM64 real-media validation](2026-08-31-api31-arm64-v8a-real-media.json)
- [API 33 ARM64 19.37 GiB MP4 validation](2026-08-31-api33-arm64-v8a-real-media-huge-mp4.json)
- [API 33 ARM64 77.97 GiB MKV validation](2026-08-31-api33-arm64-v8a-real-media-huge-mkv.json)
- [API 31 ARM64 v1.1.0 / v2.0.0 parsing-difference review](2026-08-31-api31-arm64-v8a-v1.1.0-v2.0.0-diff.json)
- [v2.0.0 final Release build and physical-device validation](2026-08-31-v2.0.0-release.json)
- [API 31 ARM64 MediaInfoLib 26.05 native-JSON evaluation](2026-09-01-api31-arm64-v8a-native-json-evaluation.json)

All three synthetic artifacts contain 24 measurements. Those three synthetic records and the three real-media validation records use the same `benchmarkInputsSha256`. The targets were measured serially to avoid host and storage contention. The direct path stays broadly flat while apparent input size grows from 1 MiB to 1 GiB: a 1024x size increase produces only 1.11x to 3.84x median latency growth. A primed 1 GiB cache hit takes 0.088 ms to 0.239 ms, while the pipe path grows substantially because it intentionally includes materializing a private copy before MediaInfoLib parsing.

The real-media artifacts are metadata-only. The API 31 matrix covers MP4, WebM, FLAC, and a 561 MiB problematic MP4 from the separately supplied sample set. The explicitly approved API 33 run additionally covers a 19.37 GiB MPEG-4 file and a 77.97 GiB Matroska file using `--allow-large-transfer`. Their cold full-report parses took 137.417 ms and 177.211 ms; cache hits took 0.258 ms and 0.480 ms. Cold and cached results match for every `inform`, `get`, and `snapshot` call, and every staged device copy was removed immediately after its run.

The sanitized version-difference artifact compares the frozen 0.7.83 engine with the official-source 26.05 candidate on the same four API 31 ARM64 files. All container formats and valid core stream types remain stable. The 17 direct-query and 63 parsed-field changes were reviewed as compatible upstream parser evolution: unit/date normalization, stable field renames, more specific AAC identification, and additional H.264, VP9 HDR/color, FLAC checksum/compression, and cover-image metadata. The malformed MP4 remains General-only in both engines, while 26.05 omits the old `IsTruncated=Yes` diagnostic. Full reports remain local and are bound to the public artifact by `rawReviewSha256`.

The comparison uses a verified installed v1.1.0 baseline because the already-frozen minified v1.1.0 Release wrapper cannot load its native engine; its `libmediainfo.so` is nevertheless byte-identical to the working installed baseline. v1.1.0 is not rebuilt or replaced. The v2 branch adds an explicit R8 keep rule and a public-AIDL smoke test that installs and exercises the actual minified Release variant, preventing the old packaging defect from recurring.

The final Release artifact records bind version 2.0.0 code 10 to source revision `c2e8c42c4ff020df9d6d12aa00095d29482e9298`, the production signer, all five CRC32 filenames, sizes, SHA-256 digests, and the four audited native-library digests. The exact ARM64 and ARM32 minified APK bytes were installed on API 31, their installed hashes and versions matched, both public-AIDL/JNI smoke tests passed, and both application packages were absent after every run.

The 2026-09-01 API 31 ARM64 artifact captures MediaInfoLib 26.05 native JSON alongside the unchanged `snapshot-v1` output for MP4, WebM, FLAC with cover art, and a malformed MP4. It confirms the native envelope and tracks are usable, while documenting why machine field names, raw units, nested diagnostics, and the missing synthetic File section prevent a transparent v1 replacement. JSON and text calls also passed the dedicated concurrent output-isolation test; all staged files and development packages were removed afterward. The design decision and upstream source review are recorded in [the native JSON evaluation](../../MEDIAINFO_NATIVE_JSON.md).

These numbers establish regression baselines for the tested devices and formats; they are not, by themselves, a timeout recommendation and must not be generalized to encrypted, network-backed, or other untested provider behavior. The 30-second service deadline is validated separately with an explicitly enabled stalled-pipe test on API 36 x86_64 and API 31 ARM64.
