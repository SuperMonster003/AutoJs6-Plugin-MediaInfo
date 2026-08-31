# MediaInfo performance benchmark

This benchmark measures the plugin service's two end-to-end input paths and its process-local result cache:

- `direct_descriptor`: a seekable regular file is parsed through `/proc/self/fd/<fd>` without copying its payload; every iteration uses a unique display name so it remains a cache miss.
- `memory_cache`: the same regular file identity and display name are explicitly primed, then resolved from the in-memory LRU cache.
- `pipe_fallback`: the same bytes arrive through a non-seekable pipe and are copied to a private temporary file before parsing.

The generated inputs are deterministic sparse files in two formats: PCM/WAVE for an audio stream and RGB/BMP for an image stream. File creation is outside the timed region. Each recorded duration includes opening or streaming the descriptor, the plugin call, MediaInfoLib parsing, and cleanup. Pipe measurements therefore intentionally include the private-copy cost.

## Safety

The host runner requires an exact adb serial. By default it refuses physical devices and refuses to replace an existing plugin or test package. It installs only the matching ABI debug APK and its test APK, then uninstalls only those packages after collecting the result. Use `--allow-physical-device` only with explicit approval and a disposable test target.

The full profile creates sparse regular files with an apparent size up to 1 GiB and materializes private fallback copies up to 256 MiB. The Android target should have at least 384 MiB available in the app cache filesystem. Samples and fallback files are deleted after every run.

The real-media runner applies the same serial, physical-device and existing-package safety checks. It caps aggregate transfer at 2 GiB by default, stages samples one at a time under the plugin's app-specific external directory, disables redundant ADB compression for already compressed media, and removes each exact staged path in its cleanup block. Use `--allow-large-transfer` only after explicit approval. If the selected device already contains the plugin, `--update-existing-package` performs a same-signature `adb install -r`, preserves the plugin and its data after validation, and still removes the test package.

## Usage

From the repository root with JDK 21, Android SDK tools and `adb` on `PATH`:

```text
py .python/run_mediainfo_benchmark.py --serial emulator-5556 --profile smoke
```

To run the full matrix and retain a reviewable JSON artifact:

```text
py .python/run_mediainfo_benchmark.py --serial emulator-5556 --profile full --output benchmark/results/api36-x86_64.json
```

To validate selected real media and retain a metadata-only artifact:

```text
py .python/run_real_media_validation.py --serial DEVICE_SERIAL --sample PATH_TO_SAMPLE --output benchmark/results/api31-arm64-real-media.json --allow-physical-device
```

For an explicitly approved physical-device transfer larger than 2 GiB where the plugin is already installed, additionally pass `--allow-large-transfer --update-existing-package`.

Useful overrides are `--formats`, `--direct-sizes-mib`, `--fallback-sizes-mib`, `--warmups`, and `--iterations`. Use `--skip-build` only after assembling both `:app:assembleDebug` and `:app:assembleDebugAndroidTest` from the same source tree.

Profiles:

| Profile | Formats | Direct/cache sizes | Pipe fallback sizes | Measured iterations |
| --- | --- | --- | --- | --- |
| `smoke` | WAVE, BMP | 1, 16, 64 MiB | 1, 16 MiB | 2 |
| `full` | WAVE, BMP | 1, 64, 256, 1024 MiB | 1, 16, 64, 256 MiB | 3 |

One warm-up iteration of the smallest case is run for each format and input mode before measurements. Results use `SystemClock.elapsedRealtimeNanos()` and report every sample plus median, minimum, maximum, and effective MiB/s.

Recorded full-profile baselines and a compact comparison are available in [results/README.md](results/README.md). Keep the raw JSON artifact with any PR that changes input handling or MediaInfoLib, and rerun it on the same device class when comparing revisions.

## Interpretation and limits

Compare sizes within the same device, build, format and mode. A successful no-copy implementation should keep `direct_descriptor` latency broadly flat as the apparent file size grows, `memory_cache` should avoid native parsing, and `pipe_fallback` should grow with the number of copied bytes. Effective throughput for direct or cached parsing is only a scaling indicator because neither path reads the entire payload.

This is an end-to-end regression benchmark, not an AndroidX microbenchmark. Emulator scheduling, filesystem cache and host load can add noise, so medians matter more than individual samples. Synthetic WAVE/BMP inputs make the size axis deterministic but do not represent compressed, fragmented, encrypted, network-backed or malformed media. Real-world samples should be measured separately before setting production timeout thresholds.

The JSON schema is `autojs6-plugin-mediainfo-benchmark-v1`. Each artifact records the device API/ABI, profile configuration, raw nanosecond samples, summary metrics, source revision, whether the source tree was dirty, and a SHA-256 digest of every source, native library and AAR input that can affect the benchmark. The digest makes a result identifiable even before its working tree has been committed.
