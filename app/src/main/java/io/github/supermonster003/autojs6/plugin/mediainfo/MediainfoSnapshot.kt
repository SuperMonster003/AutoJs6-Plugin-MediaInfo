package io.github.supermonster003.autojs6.plugin.mediainfo

import org.autojs.plugin.mediainfo.api.MediainfoOptionKeys
import java.util.Locale

internal data class SnapshotOptions(
    val includeInform: Boolean = true,
    val includeSections: Boolean = true,
)

internal fun parseSnapshotOptions(readBoolean: (key: String, defaultValue: Boolean) -> Boolean): SnapshotOptions =
    SnapshotOptions(
        includeInform = readBoolean(MediainfoOptionKeys.INCLUDE_INFORM, true),
        includeSections = readBoolean(MediainfoOptionKeys.INCLUDE_SECTIONS, true),
    )

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
