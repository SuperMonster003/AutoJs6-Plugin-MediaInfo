package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * Explicitly enabled end-to-end benchmark for the regular-FD and pipe fallback paths.
 *
 * This class is skipped during ordinary connected tests. Use
 * `.python/run_mediainfo_benchmark.py` to install the APKs on one explicitly selected
 * emulator, enable the benchmark arguments and collect the JSON result.
 */
@RunWith(AndroidJUnit4::class)
class MediainfoPerformanceBenchmarkTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val arguments = InstrumentationRegistry.getArguments()

    @Test
    fun benchmarkSyntheticMediaMatrix() {
        assumeTrue(
            "Manual MediaInfo benchmark; run it through .python/run_mediainfo_benchmark.py",
            arguments.getString(ARG_ENABLED).toBoolean(),
        )

        val config = BenchmarkConfig.from(arguments)
        require(cacheAvailableBytes() >= config.minimumAvailableBytes) {
            "Benchmark needs at least ${config.minimumAvailableBytes} free bytes in the app cache filesystem"
        }

        val temporaryFilesBefore = mediaTempFileNames()
        val samplesDirectory = File(context.cacheDir, SAMPLE_DIRECTORY).apply { mkdirs() }
        val measurements = JSONArray()
        try {
            withBoundPlugin { plugin ->
                config.formats.forEach { format ->
                    runFormatMatrix(plugin, format, config, samplesDirectory, measurements)
                }
            }
        } finally {
            samplesDirectory.deleteRecursively()
        }

        check(temporaryFilesBefore == mediaTempFileNames()) {
            "MediaInfo fallback temporary files leaked during the benchmark"
        }

        val result = JSONObject()
            .put("schema", RESULT_SCHEMA)
            .put("generatedAtUtc", utcTimestamp())
            .put("profile", config.profile)
            .put("device", deviceJson())
            .put("configuration", config.toJson())
            .put("measurements", measurements)

        File(context.filesDir, RESULT_FILE_NAME).writeText(result.toString(2), Charsets.UTF_8)
    }

    private fun runFormatMatrix(
        plugin: IMediainfoPlugin,
        format: SyntheticFormat,
        config: BenchmarkConfig,
        directory: File,
        measurements: JSONArray,
    ) {
        val allSizes = (config.directSizesMiB + config.fallbackSizesMiB).distinct().sorted()
        var directWarmedUp = false
        var fallbackWarmedUp = false
        var uncachedInvocation = 0

        allSizes.forEach { sizeMiB ->
            val sample = format.createSparseSample(directory, sizeMiB)
            try {
                if (sizeMiB in config.directSizesMiB) {
                    if (!directWarmedUp) {
                        repeat(config.warmupIterations) {
                            validateReport(
                                format,
                                parseDirect(plugin, sample, "uncached-${uncachedInvocation++}-${sample.name}"),
                            )
                        }
                        directWarmedUp = true
                    }
                    measurements.put(
                        measure(format, InputMode.DIRECT_DESCRIPTOR, sample, config.measureIterations) {
                            parseDirect(plugin, sample, "uncached-${uncachedInvocation++}-${sample.name}")
                        },
                    )

                    validateReport(format, parseDirect(plugin, sample, sample.name))
                    measurements.put(
                        measure(format, InputMode.MEMORY_CACHE, sample, config.measureIterations) {
                            parseDirect(plugin, sample, sample.name)
                        },
                    )
                }

                if (sizeMiB in config.fallbackSizesMiB) {
                    if (!fallbackWarmedUp) {
                        repeat(config.warmupIterations) { validateReport(format, parsePipe(plugin, sample)) }
                        fallbackWarmedUp = true
                    }
                    measurements.put(
                        measure(format, InputMode.PIPE_FALLBACK, sample, config.measureIterations) {
                            parsePipe(plugin, sample)
                        },
                    )
                }
            } finally {
                sample.delete()
            }
        }
    }

    private fun measure(
        format: SyntheticFormat,
        mode: InputMode,
        sample: File,
        iterations: Int,
        parse: () -> String,
    ): JSONObject {
        val samplesNanos = LongArray(iterations) {
            val startedAt = SystemClock.elapsedRealtimeNanos()
            val report = parse()
            val elapsed = SystemClock.elapsedRealtimeNanos() - startedAt
            validateReport(format, report)
            elapsed
        }
        val sorted = samplesNanos.sorted()
        val medianNanos = if (sorted.size % 2 == 1) {
            sorted[sorted.size / 2]
        } else {
            val left = sorted[sorted.size / 2 - 1]
            val right = sorted[sorted.size / 2]
            left / 2 + right / 2 + (left % 2 + right % 2) / 2
        }
        val sizeBytes = sample.length()
        val throughputMiBPerSecond = sizeBytes.toDouble() / MEBIBYTE / (medianNanos.toDouble() / NANOS_PER_SECOND)

        return JSONObject()
            .put("format", format.wireName)
            .put("mode", mode.wireName)
            .put("sizeBytes", sizeBytes)
            .put("samplesNanos", JSONArray(samplesNanos.toList()))
            .put("medianNanos", medianNanos)
            .put("minNanos", sorted.first())
            .put("maxNanos", sorted.last())
            .put("throughputMiBPerSecond", throughputMiBPerSecond)
    }

    private fun parseDirect(plugin: IMediainfoPlugin, sample: File, displayName: String): String =
        ParcelFileDescriptor.open(sample, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
            plugin.inform(descriptor, displayName)
        }

    private fun parsePipe(plugin: IMediainfoPlugin, sample: File): String {
        val (readSide, writeSide) = ParcelFileDescriptor.createPipe()
        val writerFailure = AtomicReference<Throwable?>()
        val writer = Thread {
            try {
                FileInputStream(sample).use { input ->
                    ParcelFileDescriptor.AutoCloseOutputStream(writeSide).use { output ->
                        input.copyTo(output, PIPE_BUFFER_BYTES)
                    }
                }
            } catch (throwable: Throwable) {
                writerFailure.set(throwable)
            }
        }.apply {
            name = "mediainfo-benchmark-pipe-writer"
            isDaemon = true
            start()
        }

        val report = try {
            plugin.inform(readSide, sample.name)
        } finally {
            runCatching { readSide.close() }
            writer.join(PIPE_WRITER_TIMEOUT_MILLIS)
        }
        check(!writer.isAlive) { "Pipe writer timed out for ${sample.name}" }
        writerFailure.get()?.let { throw AssertionError("Pipe writer failed for ${sample.name}", it) }
        return report
    }

    private fun validateReport(format: SyntheticFormat, report: String) {
        check(report.isNotBlank()) { "MediaInfo returned an empty ${format.wireName} report" }
        check(report.contains(format.expectedSection)) {
            "MediaInfo ${format.wireName} report does not contain ${format.expectedSection}"
        }
    }

    private fun <T> withBoundPlugin(block: (IMediainfoPlugin) -> T): T {
        val connected = CountDownLatch(1)
        val binder = AtomicReference<IBinder?>()
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binder.set(service)
                connected.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit
        }
        val intent = Intent(ACTION_MEDIAINFO).setPackage(context.packageName)
        check(context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            "MediaInfo plugin service could not be bound"
        }
        try {
            check(connected.await(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                "Timed out while binding the MediaInfo plugin service"
            }
            return block(IMediainfoPlugin.Stub.asInterface(requireNotNull(binder.get())))
        } finally {
            context.unbindService(connection)
        }
    }

    private fun cacheAvailableBytes(): Long = android.os.StatFs(context.cacheDir.absolutePath).availableBytes

    private fun mediaTempFileNames(): Set<String> = context.cacheDir
        .listFiles { _, name -> name.startsWith(MediaInputAccess.TEMP_FILE_PREFIX) }
        .orEmpty()
        .mapTo(mutableSetOf()) { it.name }

    private fun deviceJson(): JSONObject = JSONObject()
        .put("manufacturer", Build.MANUFACTURER)
        .put("model", Build.MODEL)
        .put("sdk", Build.VERSION.SDK_INT)
        .put("abis", JSONArray(Build.SUPPORTED_ABIS.toList()))
        .put("availableProcessors", Runtime.getRuntime().availableProcessors())

    private fun utcTimestamp(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date())

    private data class BenchmarkConfig(
        val profile: String,
        val formats: List<SyntheticFormat>,
        val directSizesMiB: List<Int>,
        val fallbackSizesMiB: List<Int>,
        val warmupIterations: Int,
        val measureIterations: Int,
    ) {

        val minimumAvailableBytes: Long
            get() = ((fallbackSizesMiB.maxOrNull()?.toLong() ?: 0L) + FREE_SPACE_MARGIN_MIB) * MEBIBYTE

        fun toJson(): JSONObject = JSONObject()
            .put("formats", JSONArray(formats.map { it.wireName }))
            .put("directSizesMiB", JSONArray(directSizesMiB))
            .put("fallbackSizesMiB", JSONArray(fallbackSizesMiB))
            .put("warmupIterations", warmupIterations)
            .put("measureIterations", measureIterations)
            .put("pipeBufferBytes", PIPE_BUFFER_BYTES)

        companion object {

            fun from(arguments: Bundle): BenchmarkConfig {
                val profile = arguments.getString(ARG_PROFILE).orEmpty().ifBlank { PROFILE_SMOKE }
                val defaults = when (profile) {
                    PROFILE_SMOKE -> BenchmarkConfig(
                        profile = profile,
                        formats = SyntheticFormat.entries,
                        directSizesMiB = listOf(1, 16, 64),
                        fallbackSizesMiB = listOf(1, 16),
                        warmupIterations = 1,
                        measureIterations = 2,
                    )

                    PROFILE_FULL -> BenchmarkConfig(
                        profile = profile,
                        formats = SyntheticFormat.entries,
                        directSizesMiB = listOf(1, 64, 256, 1024),
                        fallbackSizesMiB = listOf(1, 16, 64, 256),
                        warmupIterations = 1,
                        measureIterations = 3,
                    )

                    else -> error("Unknown benchmark profile: $profile")
                }
                return defaults.copy(
                    formats = arguments.csv(ARG_FORMATS)?.map { SyntheticFormat.fromWireName(it) } ?: defaults.formats,
                    directSizesMiB = arguments.intCsv(ARG_DIRECT_SIZES_MIB) ?: defaults.directSizesMiB,
                    fallbackSizesMiB = arguments.intCsv(ARG_FALLBACK_SIZES_MIB) ?: defaults.fallbackSizesMiB,
                    warmupIterations = arguments.positiveInt(ARG_WARMUPS) ?: defaults.warmupIterations,
                    measureIterations = arguments.positiveInt(ARG_ITERATIONS) ?: defaults.measureIterations,
                ).also { config ->
                    require(config.formats.isNotEmpty()) { "At least one benchmark format is required" }
                    require(config.directSizesMiB.isNotEmpty()) { "At least one direct benchmark size is required" }
                    require(config.directSizesMiB.all { it in 1..MAX_SAMPLE_SIZE_MIB }) {
                        "Direct sample sizes must be between 1 and $MAX_SAMPLE_SIZE_MIB MiB"
                    }
                    require(config.fallbackSizesMiB.all { it in 1..MAX_FALLBACK_SIZE_MIB }) {
                        "Fallback sample sizes must be between 1 and $MAX_FALLBACK_SIZE_MIB MiB"
                    }
                }
            }

            private fun Bundle.csv(key: String): List<String>? = getString(key)
                ?.split(',')
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?.takeIf { it.isNotEmpty() }

            private fun Bundle.intCsv(key: String): List<Int>? = csv(key)
                ?.map { value -> value.toIntOrNull() ?: error("Invalid integer in $key: $value") }
                ?.distinct()
                ?.sorted()

            private fun Bundle.positiveInt(key: String): Int? = getString(key)?.let { value ->
                requireNotNull(value.toIntOrNull()?.takeIf { it > 0 }) { "$key must be a positive integer" }
            }
        }
    }

    private enum class InputMode(val wireName: String) {
        DIRECT_DESCRIPTOR("direct_descriptor"),
        MEMORY_CACHE("memory_cache"),
        PIPE_FALLBACK("pipe_fallback"),
    }

    private enum class SyntheticFormat(
        val wireName: String,
        private val extension: String,
        val expectedSection: String,
    ) {
        WAVE("wave_pcm", "wav", "Audio"),
        BITMAP("bitmap_rgb", "bmp", "Image"),
        ;

        fun createSparseSample(directory: File, sizeMiB: Int): File {
            val sizeBytes = sizeMiB.toLong() * MEBIBYTE
            val file = File(directory, "synthetic-$wireName-${sizeMiB}mib.$extension")
            RandomAccessFile(file, "rw").use { output ->
                output.setLength(sizeBytes)
                output.seek(0L)
                output.write(
                    when (this) {
                        WAVE -> waveHeader(sizeBytes)
                        BITMAP -> bitmapHeader(sizeBytes)
                    },
                )
            }
            return file
        }

        companion object {

            fun fromWireName(value: String): SyntheticFormat = entries.firstOrNull { it.wireName == value }
                ?: error("Unknown benchmark format: $value")

            private fun waveHeader(sizeBytes: Long): ByteArray {
                val dataSize = sizeBytes - WAVE_HEADER_BYTES
                return ByteBuffer.allocate(WAVE_HEADER_BYTES).order(ByteOrder.LITTLE_ENDIAN).apply {
                    put("RIFF".toByteArray(Charsets.US_ASCII))
                    putInt((sizeBytes - 8L).toInt())
                    put("WAVE".toByteArray(Charsets.US_ASCII))
                    put("fmt ".toByteArray(Charsets.US_ASCII))
                    putInt(16)
                    putShort(1)
                    putShort(2)
                    putInt(48_000)
                    putInt(48_000 * 4)
                    putShort(4)
                    putShort(16)
                    put("data".toByteArray(Charsets.US_ASCII))
                    putInt(dataSize.toInt())
                }.array()
            }

            private fun bitmapHeader(sizeBytes: Long): ByteArray {
                val pixelBytes = sizeBytes - BITMAP_HEADER_BYTES
                val rowBytes = 3_072
                val height = (pixelBytes / rowBytes).coerceAtLeast(1L).toInt()
                return ByteBuffer.allocate(BITMAP_HEADER_BYTES).order(ByteOrder.LITTLE_ENDIAN).apply {
                    put('B'.code.toByte())
                    put('M'.code.toByte())
                    putInt(sizeBytes.toInt())
                    putInt(0)
                    putInt(BITMAP_HEADER_BYTES)
                    putInt(40)
                    putInt(1_024)
                    putInt(height)
                    putShort(1)
                    putShort(24)
                    putInt(0)
                    putInt((height.toLong() * rowBytes).toInt())
                    putInt(2_835)
                    putInt(2_835)
                    putInt(0)
                    putInt(0)
                }.array()
            }
        }
    }

    companion object {

        const val RESULT_FILE_NAME = "mediainfo-benchmark-result.json"

        private const val RESULT_SCHEMA = "autojs6-plugin-mediainfo-benchmark-v1"
        private const val ACTION_MEDIAINFO = "org.autojs.plugin.MEDIAINFO"
        private const val ARG_ENABLED = "mediainfoBenchmark"
        private const val ARG_PROFILE = "benchmarkProfile"
        private const val ARG_FORMATS = "benchmarkFormats"
        private const val ARG_DIRECT_SIZES_MIB = "benchmarkDirectSizesMiB"
        private const val ARG_FALLBACK_SIZES_MIB = "benchmarkFallbackSizesMiB"
        private const val ARG_WARMUPS = "benchmarkWarmups"
        private const val ARG_ITERATIONS = "benchmarkIterations"
        private const val PROFILE_SMOKE = "smoke"
        private const val PROFILE_FULL = "full"
        private const val SAMPLE_DIRECTORY = "mediainfo-benchmark-samples"
        private const val SERVICE_TIMEOUT_SECONDS = 10L
        private const val PIPE_WRITER_TIMEOUT_MILLIS = 5 * 60 * 1_000L
        private const val PIPE_BUFFER_BYTES = 256 * 1_024
        private const val WAVE_HEADER_BYTES = 44
        private const val BITMAP_HEADER_BYTES = 54
        private const val MAX_SAMPLE_SIZE_MIB = 1_024
        private const val MAX_FALLBACK_SIZE_MIB = 256
        private const val FREE_SPACE_MARGIN_MIB = 128L
        private const val MEBIBYTE = 1_024L * 1_024L
        private const val NANOS_PER_SECOND = 1_000_000_000.0
    }
}
