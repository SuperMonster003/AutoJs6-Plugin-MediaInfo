package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/** Explicit, host-staged validation for real media files. */
@RunWith(AndroidJUnit4::class)
class MediainfoRealMediaValidationTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val arguments = InstrumentationRegistry.getArguments()

    @Test
    fun validateStagedRealMedia() {
        assumeTrue(
            "Manual real-media validation; run it through .python/run_real_media_validation.py",
            arguments.getString(ARG_ENABLED).toBoolean(),
        )

        val directory = requireNotNull(context.getExternalFilesDir(STAGED_DIRECTORY)) {
            "App-specific external files directory is unavailable"
        }
        val captureDetails = arguments.getString(ARG_CAPTURE_DETAILS).toBoolean()
        val files = directory.listFiles { file -> file.isFile }.orEmpty().sortedBy { it.name }
        check(files.isNotEmpty()) { "No real media files were staged in ${directory.absolutePath}" }

        val temporaryFilesBefore = mediaTempFileNames()
        val results = JSONArray()
        val failures = mutableListOf<String>()
        withBoundPlugin { plugin ->
            files.forEach { file ->
                val item = runCatching { validateFile(plugin, file, captureDetails) }.fold(
                    onSuccess = { it },
                    onFailure = { error ->
                        failures += "${file.name}: ${error.javaClass.simpleName}: ${error.message}"
                        JSONObject()
                            .put("stagedName", file.name)
                            .put("sizeBytes", file.length())
                            .put("errorType", error.javaClass.name)
                            .put("errorMessage", error.message.orEmpty())
                    },
                )
                results.put(item)
            }
        }

        check(temporaryFilesBefore == mediaTempFileNames()) {
            "MediaInfo fallback temporary files leaked during real-media validation"
        }

        val result = JSONObject()
            .put("schema", RESULT_SCHEMA)
            .put("generatedAtUtc", utcTimestamp())
            .put("device", deviceJson())
            .put("files", results)
            .put("failureCount", failures.size)
            .put("capturedDetails", captureDetails)
        File(context.filesDir, RESULT_FILE_NAME).writeText(result.toString(2), Charsets.UTF_8)

        check(failures.isEmpty()) { failures.joinToString(separator = "\n") }
    }

    private fun validateFile(
        plugin: IMediainfoPlugin,
        file: File,
        captureDetails: Boolean,
    ): JSONObject {
        if (!captureDetails) {
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
                val source = requireNotNull(MediaInputAccess.directSource(descriptor, file.name)) {
                    "Staged media is not available as a regular direct descriptor"
                }
                check(source.cacheIdentity != null) { "Staged media does not have a stable cache identity" }
            }
        }

        val coldInform = timed { withDescriptor(file) { plugin.inform(it, file.name) } }
        val cachedInform = timed { withDescriptor(file) { plugin.inform(it, file.name) } }
        check(coldInform.value == cachedInform.value) { "Cached inform differs from the cold report" }

        val coldFormat = timed {
            withDescriptor(file) { plugin.get(it, file.name, "general", 0, "Format") }
        }
        val cachedFormat = timed {
            withDescriptor(file) { plugin.get(it, file.name, "general", 0, "Format") }
        }
        check(coldFormat.value == cachedFormat.value) { "Cached Format differs from the cold query" }

        val coldSnapshot = timed { withDescriptor(file) { plugin.snapshot(it, file.name, null) } }
        val cachedSnapshot = timed { withDescriptor(file) { plugin.snapshot(it, file.name, null) } }
        check(coldSnapshot.value == cachedSnapshot.value) { "Cached snapshot differs from the cold snapshot" }
        val snapshot = JSONObject(coldSnapshot.value)

        return JSONObject()
            .put("stagedName", file.name)
            .put("sizeBytes", file.length())
            .put("informLength", coldInform.value.length)
            .put("informHasGeneral", coldInform.value.contains("General"))
            .put("informHasVideo", coldInform.value.contains("Video"))
            .put("informHasAudio", coldInform.value.contains("Audio"))
            .put("informHasImage", coldInform.value.contains("Image"))
            .put("generalFormat", coldFormat.value)
            .put("snapshotSectionCount", snapshot.getJSONObject("sections").length())
            .put("coldInformNanos", coldInform.elapsedNanos)
            .put("cachedInformNanos", cachedInform.elapsedNanos)
            .put("coldGetNanos", coldFormat.elapsedNanos)
            .put("cachedGetNanos", cachedFormat.elapsedNanos)
            .put("coldSnapshotNanos", coldSnapshot.elapsedNanos)
            .put("cachedSnapshotNanos", cachedSnapshot.elapsedNanos)
            .apply {
                if (captureDetails) {
                    put("snapshot", snapshot)
                    put("queries", captureQueries(plugin, file, snapshot))
                }
            }
    }

    private fun captureQueries(
        plugin: IMediainfoPlugin,
        file: File,
        snapshot: JSONObject,
    ): JSONObject {
        val sections = snapshot.getJSONObject("sections")
        return JSONObject().apply {
            QUERY_SPECS.forEach { spec ->
                val streamCount = sections.optJSONArray(spec.sectionName)?.length().orZero()
                repeat(streamCount) { streamIndex ->
                    spec.parameters.forEach { parameter ->
                        val value = withDescriptor(file) { descriptor ->
                            plugin.get(descriptor, file.name, spec.streamKind, streamIndex, parameter)
                        }.orEmpty()
                        if (value.isNotEmpty()) {
                            put("${spec.streamKind}[$streamIndex].$parameter", value)
                        }
                    }
                }
            }
        }
    }

    private fun Int?.orZero(): Int = this ?: 0

    private fun <T> timed(block: () -> T): TimedValue<T> {
        val startedAt = SystemClock.elapsedRealtimeNanos()
        val value = block()
        return TimedValue(value, SystemClock.elapsedRealtimeNanos() - startedAt)
    }

    private fun <T> withDescriptor(file: File, block: (ParcelFileDescriptor) -> T): T =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use(block)

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

    private fun mediaTempFileNames(): Set<String> = context.cacheDir
        .listFiles { _, name -> name.startsWith(MediaInputAccess.TEMP_FILE_PREFIX) }
        .orEmpty()
        .mapTo(mutableSetOf()) { it.name }

    private fun deviceJson(): JSONObject = JSONObject()
        .put("manufacturer", Build.MANUFACTURER)
        .put("model", Build.MODEL)
        .put("sdk", Build.VERSION.SDK_INT)
        .put("abis", JSONArray(Build.SUPPORTED_ABIS.toList()))

    private fun utcTimestamp(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date())

    private data class TimedValue<T>(val value: T, val elapsedNanos: Long)

    private data class QuerySpec(
        val streamKind: String,
        val sectionName: String,
        val parameters: List<String>,
    )

    companion object {
        const val RESULT_FILE_NAME = "mediainfo-real-media-result.json"

        private const val RESULT_SCHEMA = "autojs6-plugin-mediainfo-real-media-v1"
        private const val ARG_ENABLED = "realMediaValidation"
        private const val ARG_CAPTURE_DETAILS = "realMediaCaptureDetails"
        private const val STAGED_DIRECTORY = "media-validation"
        private const val ACTION_MEDIAINFO = "org.autojs.plugin.MEDIAINFO"
        private const val SERVICE_TIMEOUT_SECONDS = 10L

        private val QUERY_SPECS = listOf(
            QuerySpec(
                "general",
                "general",
                listOf(
                    "Format",
                    "Format_Version",
                    "Format_Profile",
                    "CodecID",
                    "FileSize",
                    "Duration",
                    "OverallBitRate_Mode",
                    "OverallBitRate",
                    "FrameRate",
                    "FrameCount",
                    "StreamSize",
                    "Encoded_Date",
                    "Tagged_Date",
                    "Encoded_Application",
                    "Encoded_Library",
                ),
            ),
            QuerySpec(
                "video",
                "video",
                listOf(
                    "Format",
                    "Format_Version",
                    "Format_Profile",
                    "Format_Level",
                    "Format_Tier",
                    "CodecID",
                    "Duration",
                    "BitRate_Mode",
                    "BitRate",
                    "Width",
                    "Height",
                    "DisplayAspectRatio",
                    "FrameRate_Mode",
                    "FrameRate",
                    "FrameCount",
                    "ColorSpace",
                    "ChromaSubsampling",
                    "BitDepth",
                    "ScanType",
                    "HDR_Format",
                    "StreamSize",
                    "Language",
                ),
            ),
            QuerySpec(
                "audio",
                "audio",
                listOf(
                    "Format",
                    "Format_Version",
                    "Format_Profile",
                    "CodecID",
                    "Duration",
                    "BitRate_Mode",
                    "BitRate",
                    "Channels",
                    "ChannelLayout",
                    "SamplingRate",
                    "FrameRate",
                    "FrameCount",
                    "BitDepth",
                    "Compression_Mode",
                    "StreamSize",
                    "Language",
                    "Default",
                    "Forced",
                ),
            ),
            QuerySpec(
                "text",
                "text",
                listOf(
                    "Format",
                    "Format_Profile",
                    "CodecID",
                    "Duration",
                    "FrameRate",
                    "FrameCount",
                    "StreamSize",
                    "Language",
                    "Default",
                    "Forced",
                ),
            ),
            QuerySpec(
                "other",
                "other",
                listOf("Type", "Format", "CodecID", "Duration", "FrameRate", "FrameCount"),
            ),
            QuerySpec(
                "image",
                "image",
                listOf("Format", "CodecID", "Width", "Height", "ColorSpace", "BitDepth", "StreamSize"),
            ),
            QuerySpec(
                "menu",
                "menu",
                listOf("Format", "Duration", "FrameRate", "FrameCount", "Language"),
            ),
        )
    }
}
