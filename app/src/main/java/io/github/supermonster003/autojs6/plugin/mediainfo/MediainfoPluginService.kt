package io.github.supermonster003.autojs6.plugin.mediainfo

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import org.autojs.plugin.common.api.PluginInfo
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.json.JSONArray
import org.json.JSONObject
import org.mediainfo.android.MediaInfo
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MediainfoPluginService : Service() {

    private val resultCache = MediaInfoResultCache()
    private val activeCalls = ConcurrentHashMap.newKeySet<MediaInfoCallGuard>()
    private val timeoutScheduler = Executors.newSingleThreadScheduledExecutor { runnable ->
        Thread(runnable, "mediainfo-call-timeout").apply { isDaemon = true }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onLowMemory() {
        resultCache.clear()
        super.onLowMemory()
    }

    override fun onDestroy() {
        activeCalls.forEach { it.cancel() }
        timeoutScheduler.shutdownNow()
        resultCache.clear()
        super.onDestroy()
    }

    private val binder = object : IMediainfoPlugin.Stub() {
        override fun getInfo(): PluginInfo {
            return pluginInfo(
                name = getString(R.string.app_name),
                description = getString(R.string.plugin_description),
            )
        }

        override fun inform(mediaFd: ParcelFileDescriptor?, displayName: String?): String {
            return withCallTimeout { call ->
                withMediaSource(mediaFd, displayName, call) { source ->
                    resultCache.getInform(source.cacheIdentity)?.let { cached ->
                        return@withMediaSource MediaParseAttempt(cached, true)
                    }
                    val inform = call.withMediaInfo { it.getMI(source.path).orEmpty() }
                    if (inform.isNotBlank()) resultCache.putInform(source.cacheIdentity, inform)
                    MediaParseAttempt(inform, inform.isNotBlank())
                }
            }
        }

        override fun get(
            mediaFd: ParcelFileDescriptor?,
            displayName: String?,
            streamKind: String?,
            streamNumber: Int,
            parameter: String?,
        ): String {
            return withCallTimeout { call ->
                val resolvedStreamKind = try {
                    streamKind.toMediaInfoStreamKind()
                } catch (error: Throwable) {
                    runCatching { mediaFd?.close() }
                    throw error
                }
                val resolvedParameter = parameter.orEmpty()
                val request = MediaGetRequest(
                    streamKind = resolvedStreamKind.name,
                    streamNumber = streamNumber,
                    parameter = resolvedParameter,
                )
                withMediaSource(mediaFd, displayName, call) { source ->
                    resultCache.getQuery(source.cacheIdentity, request)?.let { cached ->
                        return@withMediaSource MediaParseAttempt(cached.value, true)
                    }
                    val cachedInform = resultCache.getInform(source.cacheIdentity)
                    val nativeResult = call.withMediaInfo { mediaInfo ->
                        val value = mediaInfo.get(
                            source.path,
                            resolvedStreamKind,
                            streamNumber,
                            resolvedParameter,
                        )
                        val validationReport = if (
                            source.kind == MediaInputKind.DIRECT_DESCRIPTOR && value.isEmpty() && cachedInform == null
                        ) {
                            mediaInfo.getMI(source.path).orEmpty()
                        } else {
                            null
                        }
                        NativeGetResult(value, validationReport)
                    }
                    nativeResult.validationReport?.takeIf { it.isNotBlank() }?.let { report ->
                        resultCache.putInform(source.cacheIdentity, report)
                    }
                    val parsed = source.kind != MediaInputKind.DIRECT_DESCRIPTOR ||
                        nativeResult.value.isNotEmpty() ||
                        cachedInform != null ||
                        nativeResult.validationReport?.isNotBlank() == true
                    if (!parsed) {
                        MediaParseAttempt("", false)
                    } else {
                        resultCache.putQuery(source.cacheIdentity, request, nativeResult.value)
                        MediaParseAttempt(nativeResult.value, true)
                    }
                }
            }
        }

        override fun snapshot(mediaFd: ParcelFileDescriptor?, displayName: String?, options: Bundle?): String {
            return withCallTimeout { call ->
                withMediaSource(mediaFd, displayName, call) { source ->
                    val snapshotOptions = parseSnapshotOptions { key, defaultValue ->
                        options?.getBoolean(key, defaultValue) ?: defaultValue
                    }
                    val request = MediaSnapshotRequest(
                        includeInform = snapshotOptions.includeInform,
                        includeSections = snapshotOptions.includeSections,
                    )
                    resultCache.getSnapshot(source.cacheIdentity, request)?.let { cached ->
                        return@withMediaSource MediaParseAttempt(cached, true)
                    }
                    val inform = resultCache.getInform(source.cacheIdentity)
                        ?: call.withMediaInfo { it.getMI(source.path).orEmpty() }.also { report ->
                            if (report.isNotBlank()) resultCache.putInform(source.cacheIdentity, report)
                        }
                    val snapshot = JSONObject()
                        .put("schema", "autojs6-plugin-mediainfo-snapshot-v1")
                        .put("fileName", displayName.orEmpty())
                        .put("sizeBytes", source.sizeBytes)
                        .put("inform", if (snapshotOptions.includeInform) inform else "")
                        .put(
                            "sections",
                            if (snapshotOptions.includeSections) mediaInfoSections(inform) else JSONObject(),
                        )
                        .toString()
                    if (inform.isNotBlank()) resultCache.putSnapshot(source.cacheIdentity, request, snapshot)
                    MediaParseAttempt(snapshot, inform.isNotBlank())
                }
            }
        }
    }

    private data class MediaParseAttempt<T>(
        val value: T,
        val parsed: Boolean,
    )

    private data class NativeGetResult(
        val value: String,
        val validationReport: String?,
    )

    private fun <T> withCallTimeout(block: (MediaInfoCallGuard) -> T): T {
        val call = MediaInfoCallGuard()
        activeCalls += call
        val timeout = timeoutScheduler.schedule(call::cancel, CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        try {
            val result = block(call)
            timeout.cancel(false)
            call.throwIfCanceled()
            return result
        } catch (error: Throwable) {
            if (call.isCanceled) {
                throw IllegalStateException(
                    getString(R.string.error_media_parse_timeout, CALL_TIMEOUT_SECONDS),
                    error,
                )
            }
            throw error
        } finally {
            timeout.cancel(false)
            activeCalls -= call
            call.release()
        }
    }

    private fun <T> withMediaSource(
        mediaFd: ParcelFileDescriptor?,
        displayName: String?,
        call: MediaInfoCallGuard,
        parse: (MediaInputSource) -> MediaParseAttempt<T>,
    ): T {
        val descriptor = requireNotNull(mediaFd) { getString(R.string.error_media_file_descriptor_null) }
        var descriptorOwnedByStream = false
        try {
            call.throwIfCanceled()
            MediaInputAccess.directSource(descriptor, displayName)?.let { directSource ->
                val attempt = parse(directSource)
                if (attempt.parsed) return attempt.value
            }

            val file = createMediaTempFile(displayName)
            try {
                MediaInputAccess.rewindForCopy(descriptor)
                val input = ParcelFileDescriptor.AutoCloseInputStream(descriptor)
                descriptorOwnedByStream = true
                input.use { stream ->
                    call.register(stream)
                    try {
                        FileOutputStream(file).use { output ->
                            copyWithCancellation(stream, output, call)
                        }
                    } finally {
                        call.unregister(stream)
                    }
                }
                return parse(
                    MediaInputSource(
                        path = file.absolutePath,
                        sizeBytes = file.length(),
                        kind = MediaInputKind.PRIVATE_COPY,
                    ),
                ).value
            } finally {
                file.delete()
            }
        } finally {
            if (!descriptorOwnedByStream) descriptor.close()
        }
    }

    private fun copyWithCancellation(input: InputStream, output: OutputStream, call: MediaInfoCallGuard) {
        val buffer = ByteArray(COPY_BUFFER_BYTES)
        while (true) {
            call.throwIfCanceled()
            val read = input.read(buffer)
            if (read < 0) return
            call.throwIfCanceled()
            output.write(buffer, 0, read)
        }
    }

    private fun createMediaTempFile(displayName: String?): File {
        val suffix = displayName
            ?.substringAfterLast('.', "")
            ?.takeIf { SAFE_FILE_EXTENSION.matches(it) }
            ?.let { ".$it" }
            ?: ".media"
        return File.createTempFile(MediaInputAccess.TEMP_FILE_PREFIX, suffix, cacheDir)
    }

    private fun String?.toMediaInfoStreamKind(): MediaInfo.StreamKind {
        val normalized = this.orEmpty().trim().uppercase(Locale.US)
        return MediaInfo.StreamKind.entries.firstOrNull { it.name == normalized }
            ?: throw IllegalArgumentException(getString(R.string.error_unsupported_stream_kind, this))
    }

    private fun mediaInfoSections(inform: String): JSONObject {
        val sections = JSONObject()
        MediaInfoReportParser.parseSections(inform).forEach { (sectionName, occurrences) ->
            val array = JSONArray()
            occurrences.forEach { fields ->
                val section = JSONObject()
                fields.forEach { (key, value) ->
                    section.put(key, value)
                }
                array.put(section)
            }
            sections.put(sectionName, array)
        }
        return sections
    }

    private companion object {
        const val CALL_TIMEOUT_SECONDS = 30L
        const val COPY_BUFFER_BYTES = 256 * 1_024
        val SAFE_FILE_EXTENSION = Regex("[A-Za-z0-9]{1,12}")
    }
}
