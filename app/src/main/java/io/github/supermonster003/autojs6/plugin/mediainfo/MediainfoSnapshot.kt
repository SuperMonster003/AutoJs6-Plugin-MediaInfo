package io.github.supermonster003.autojs6.plugin.mediainfo

import org.autojs.plugin.mediainfo.api.MediainfoOptionKeys
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

internal object MediainfoSnapshotContract {
    const val OPTION_SCHEMA = "schema"

    const val SCHEMA_V1 = "autojs6-plugin-mediainfo-snapshot-v1"
    const val SCHEMA_V2 = "autojs6-plugin-mediainfo-snapshot-v2"

    const val CAPABILITY_SUPPORTED_SCHEMAS = "snapshotSchemas"
    const val CAPABILITY_DEFAULT_SCHEMA = "defaultSnapshotSchema"
    const val CAPABILITY_ENGINE_VERSION = "engineVersion"

    val SUPPORTED_SCHEMAS = arrayOf(SCHEMA_V1, SCHEMA_V2)
}

internal enum class SnapshotSchema(val id: String) {
    V1(MediainfoSnapshotContract.SCHEMA_V1),
    V2(MediainfoSnapshotContract.SCHEMA_V2),
    ;

    companion object {
        fun resolve(requestedSchema: String?): SnapshotSchema {
            if (requestedSchema.isNullOrBlank()) return V1
            return entries.firstOrNull { it.id == requestedSchema }
                ?: throw IllegalArgumentException("Unsupported MediaInfo snapshot schema: $requestedSchema")
        }
    }
}

internal data class SnapshotOptions(
    val includeInform: Boolean = true,
    val includeSections: Boolean = true,
    val schema: SnapshotSchema = SnapshotSchema.V1,
)

internal fun parseSnapshotOptions(readBoolean: (key: String, defaultValue: Boolean) -> Boolean): SnapshotOptions =
    parseSnapshotOptions(readBoolean = readBoolean, readString = { null })

internal fun parseSnapshotOptions(
    readBoolean: (key: String, defaultValue: Boolean) -> Boolean,
    readString: (key: String) -> String?,
): SnapshotOptions =
    SnapshotOptions(
        includeInform = readBoolean(MediainfoOptionKeys.INCLUDE_INFORM, true),
        includeSections = readBoolean(MediainfoOptionKeys.INCLUDE_SECTIONS, true),
        schema = SnapshotSchema.resolve(readString(MediainfoSnapshotContract.OPTION_SCHEMA)),
    )

/**
 * Converts MediaInfoLib's versioned native JSON envelope into the plugin-owned
 * snapshot-v2 envelope. Track field names and values intentionally remain
 * upstream extensions; only the surrounding grouping and metadata are stable.
 */
internal object MediaInfoSnapshotV2 {

    fun build(
        fileName: String,
        sizeBytes: Long,
        inform: String,
        includeInform: Boolean,
        includeTracks: Boolean,
        nativeJson: String,
    ): String {
        val nativeRoot = JSONObject(nativeJson)
        val nativeLibrary = nativeRoot.requiredObject("creatingLibrary")
        val nativeTracks = nativeRoot
            .requiredObject("media")
            .requiredArray("track")
        require(nativeTracks.length() > 0) { "MediaInfo native JSON contains no tracks" }

        val engine = JSONObject()
            .put("name", nativeLibrary.requiredString("name"))
            .put("version", nativeLibrary.requiredString("version"))
        nativeLibrary.optionalString("url")?.let { engine.put("url", it) }

        val adaptedTracks = adaptTracks(nativeTracks)
        val tracks = if (includeTracks) adaptedTracks else JSONObject()
        return JSONObject()
            .put("schema", MediainfoSnapshotContract.SCHEMA_V2)
            .put(
                "file",
                JSONObject()
                    .put("name", fileName)
                    .put("sizeBytes", sizeBytes),
            )
            .put("engine", engine)
            .put("inform", if (includeInform) inform else "")
            .put("tracks", tracks)
            .toString()
    }

    private fun adaptTracks(nativeTracks: JSONArray): JSONObject {
        val groupedTracks = linkedMapOf<String, JSONArray>()
        for (index in 0 until nativeTracks.length()) {
            val nativeTrack = nativeTracks.optJSONObject(index)
                ?: throw IllegalArgumentException("MediaInfo native track $index is not an object")
            val type = nativeTrack.requiredString("@type").lowercase(Locale.US)
            require(TRACK_TYPE.matches(type)) { "Unsupported MediaInfo native track type: $type" }
            groupedTracks.getOrPut(type) { JSONArray() }.put(adaptTrack(nativeTrack))
        }

        return JSONObject().also { result ->
            groupedTracks.forEach { (type, values) -> result.put(type, values) }
        }
    }

    private fun adaptTrack(nativeTrack: JSONObject): JSONObject {
        val fields = JSONObject()
        val attributes = JSONObject()
        var extra: JSONObject? = null

        nativeTrack.keys().forEach { key ->
            when {
                key == "@type" -> Unit
                key == "extra" -> {
                    extra = nativeTrack.optJSONObject(key)
                        ?: throw IllegalArgumentException("MediaInfo native track extra is not an object")
                }
                key.startsWith("@") -> attributes.put(key, nativeTrack.get(key))
                else -> fields.put(key, nativeTrack.get(key))
            }
        }

        return JSONObject()
            .put("fields", fields)
            .also { result ->
                if (attributes.length() > 0) result.put("attributes", attributes)
                extra?.let { result.put("extra", it) }
            }
    }

    private fun JSONObject.requiredObject(key: String): JSONObject =
        optJSONObject(key) ?: throw IllegalArgumentException("MediaInfo native JSON is missing object: $key")

    private fun JSONObject.requiredArray(key: String): JSONArray =
        optJSONArray(key) ?: throw IllegalArgumentException("MediaInfo native JSON is missing array: $key")

    private fun JSONObject.requiredString(key: String): String =
        optionalString(key)
            ?: throw IllegalArgumentException("MediaInfo native JSON is missing string: $key")

    private fun JSONObject.optionalString(key: String): String? =
        (opt(key) as? String)?.takeIf { it.isNotBlank() }

    private val TRACK_TYPE = Regex("[a-z][a-z0-9_-]*")
}

internal object MediaInfoReportParser {

    fun parseSections(inform: String): Map<String, List<Map<String, String>>> {
        val sections = linkedMapOf<String, MutableList<Map<String, String>>>()
        var current: MutableMap<String, String>? = null

        inform.lineSequence().forEach { raw ->
            val line = raw.trimEnd()
            if (line.isBlank()) return@forEach

            if (!line.contains(':')) {
                val sectionName = line.trim().lowercase(Locale.US)
                if (sectionName.isEmpty()) return@forEach
                val fields = linkedMapOf<String, String>()
                sections.getOrPut(sectionName) { mutableListOf() }.add(fields)
                current = fields
                return@forEach
            }

            val target = current ?: return@forEach
            val separator = line.indexOf(':')
            val key = toCamelCase(line.substring(0, separator).trim())
            if (key.isEmpty()) return@forEach
            target[key] = line.substring(separator + 1).trim()
        }

        return sections
    }

    fun toCamelCase(fieldName: String): String = fieldName
        .replace(Regex("\\((s|es|ies)\\)"), "$1")
        .split(Regex("[^A-Za-z0-9]+"))
        .filter { it.isNotEmpty() }
        .joinToString("") { part ->
            part.lowercase(Locale.US).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }
        .replaceFirstChar { it.lowercase() }
}
