package io.github.supermonster003.autojs6.plugin.mediainfo

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import org.autojs.plugin.common.api.PluginInfo
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.autojs.plugin.mediainfo.api.MediainfoOptionKeys
import org.json.JSONArray
import org.json.JSONObject
import org.mediainfo.android.MediaInfo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

class MediainfoPluginService : Service() {

    override fun onBind(intent: Intent?): IBinder = binder

    private val binder = object : IMediainfoPlugin.Stub() {
        override fun getInfo(): PluginInfo {
            return pluginInfo(
                name = getString(R.string.app_name),
                description = getString(R.string.plugin_description),
            )
        }

        override fun inform(mediaFd: ParcelFileDescriptor?, displayName: String?): String {
            return withMediaTempFile(mediaFd, displayName) { file ->
                MediaInfo().getMI(file.absolutePath)
            }
        }

        override fun get(
            mediaFd: ParcelFileDescriptor?,
            displayName: String?,
            streamKind: String?,
            streamNumber: Int,
            parameter: String?,
        ): String {
            return withMediaTempFile(mediaFd, displayName) { file ->
                MediaInfo().get(file.absolutePath, streamKind.toMediaInfoStreamKind(), streamNumber, parameter.orEmpty())
            }
        }

        override fun snapshot(mediaFd: ParcelFileDescriptor?, displayName: String?, options: Bundle?): String {
            return withMediaTempFile(mediaFd, displayName) { file ->
                val includeInform = options?.getBoolean(MediainfoOptionKeys.INCLUDE_INFORM, true) ?: true
                val includeSections = options?.getBoolean(MediainfoOptionKeys.INCLUDE_SECTIONS, true) ?: true
                val inform = MediaInfo().getMI(file.absolutePath).orEmpty()
                JSONObject()
                    .put("schema", "autojs6-plugin-mediainfo-snapshot-v1")
                    .put("fileName", displayName.orEmpty())
                    .put("sizeBytes", file.length())
                    .put("inform", if (includeInform) inform else "")
                    .put("sections", if (includeSections) mediaInfoSections(inform) else JSONObject())
                    .toString()
            }
        }
    }

    private fun <T> withMediaTempFile(mediaFd: ParcelFileDescriptor?, displayName: String?, block: (File) -> T): T {
        require(mediaFd != null) { getString(R.string.error_media_file_descriptor_null) }
        val suffix = displayName
            ?.substringAfterLast('.', "")
            ?.takeIf { it.isNotBlank() && it.length <= 12 }
            ?.let { ".$it" }
            ?: ".media"
        val file = File.createTempFile("autojs6-mediainfo-", suffix, cacheDir)
        try {
            FileInputStream(mediaFd.fileDescriptor).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            return block(file)
        } finally {
            file.delete()
        }
    }

    private fun String?.toMediaInfoStreamKind(): MediaInfo.StreamKind {
        val normalized = this.orEmpty().trim().uppercase(Locale.US)
        return MediaInfo.StreamKind.entries.firstOrNull { it.name == normalized }
            ?: throw IllegalArgumentException(getString(R.string.error_unsupported_stream_kind, this))
    }

    private fun mediaInfoSections(inform: String): JSONObject {
        val sections = JSONObject()
        var current: JSONObject? = null
        inform.lineSequence().forEach { raw ->
            val line = raw.trimEnd()
            if (line.isBlank()) return@forEach
            if (!line.contains(':')) {
                val sectionName = line.trim().lowercase(Locale.US)
                val array = sections.optJSONArray(sectionName) ?: JSONArray().also { sections.put(sectionName, it) }
                current = JSONObject().also { array.put(it) }
            } else {
                val target = current ?: return@forEach
                val idx = line.indexOf(':')
                val key = line.substring(0, idx).trim().toMediainfoCamel()
                val value = line.substring(idx + 1).trim()
                if (key.isNotBlank()) {
                    target.put(key, value)
                }
            }
        }
        return sections
    }

    private fun String.toMediainfoCamel(): String = this
        .replace(Regex("\\((s|es|ies)\\)"), "$1")
        .split(Regex("[^A-Za-z0-9]+"))
        .filter { it.isNotEmpty() }
        .joinToString("") { part ->
            part.lowercase(Locale.US).replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }.replaceFirstChar { it.lowercase() }
}
