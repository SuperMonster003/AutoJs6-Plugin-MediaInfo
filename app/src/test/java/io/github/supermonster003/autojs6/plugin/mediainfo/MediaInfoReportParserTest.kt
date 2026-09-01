package io.github.supermonster003.autojs6.plugin.mediainfo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaInfoReportParserTest {

    @Test
    fun `parses numbered streams without collapsing v1 section names`() {
        val inform = """
            General
            Format                                   : Matroska

            Audio #1
            Format                                   : AAC LC
            Channel(s)                               : 2 channels

            Audio #2
            Format                                   : AC-3
            Language                                 : English
        """.trimIndent()

        val sections = MediaInfoReportParser.parseSections(inform)

        assertEquals(listOf("general", "audio #1", "audio #2"), sections.keys.toList())
        assertEquals("Matroska", sections.getValue("general").single().getValue("format"))
        assertEquals("2 channels", sections.getValue("audio #1").single().getValue("channels"))
        assertEquals("AC-3", sections.getValue("audio #2").single().getValue("format"))
        assertEquals("English", sections.getValue("audio #2").single().getValue("language"))
    }

    @Test
    fun `preserves repeated same-name sections as separate objects`() {
        val sections = MediaInfoReportParser.parseSections(
            """
                Menu
                ID : 1
                Menu
                ID : 2
            """.trimIndent(),
        )

        assertEquals(
            listOf(mapOf("id" to "1"), mapOf("id" to "2")),
            sections.getValue("menu"),
        )
    }

    @Test
    fun `ignores malformed fields and preserves colons inside values`() {
        val sections = MediaInfoReportParser.parseSections(
            """
                Orphan field : ignored
                General
                : ignored
                Encoded date : UTC 2026-08-31 12:34:56
                Service URL : https://example.test:8443/media
                Format : first
                Format : last
            """.trimIndent(),
        )

        assertEquals(
            mapOf(
                "encodedDate" to "UTC 2026-08-31 12:34:56",
                "serviceUrl" to "https://example.test:8443/media",
                "format" to "last",
            ),
            sections.getValue("general").single(),
        )
    }

    @Test
    fun `converts MediaInfo labels to stable camel case`() {
        assertEquals("commercialNames", MediaInfoReportParser.toCamelCase("Commercial name(s)"))
        assertEquals("durationString3", MediaInfoReportParser.toCamelCase("Duration/String3"))
        assertEquals("formatInfo", MediaInfoReportParser.toCamelCase("Format/Info"))
        assertEquals("bitRate", MediaInfoReportParser.toCamelCase("Bit rate"))
        assertEquals("", MediaInfoReportParser.toCamelCase("---"))
    }

    @Test
    fun `snapshot options default to including inform and sections`() {
        val requestedDefaults = mutableListOf<Pair<String, Boolean>>()

        val options = parseSnapshotOptions { key, defaultValue ->
            requestedDefaults += key to defaultValue
            defaultValue
        }

        assertTrue(options.includeInform)
        assertTrue(options.includeSections)
        assertEquals(SnapshotSchema.V1, options.schema)
        assertEquals(2, requestedDefaults.size)
        assertTrue(requestedDefaults.all { it.second })
    }

    @Test
    fun `snapshot options honor independent explicit values`() {
        val options = parseSnapshotOptions { key, _ -> key.endsWith("includeSections") }

        assertFalse(options.includeInform)
        assertTrue(options.includeSections)
        assertEquals(SnapshotSchema.V1, options.schema)
    }

    @Test
    fun `snapshot schema requires an explicit supported identifier`() {
        val v1 = parseSnapshotOptions(
            readBoolean = { _, defaultValue -> defaultValue },
            readString = { MediainfoSnapshotContract.SCHEMA_V1 },
        )
        val v2 = parseSnapshotOptions(
            readBoolean = { _, defaultValue -> defaultValue },
            readString = { MediainfoSnapshotContract.SCHEMA_V2 },
        )
        val blank = parseSnapshotOptions(
            readBoolean = { _, defaultValue -> defaultValue },
            readString = { "  " },
        )

        assertEquals(SnapshotSchema.V1, v1.schema)
        assertEquals(SnapshotSchema.V2, v2.schema)
        assertEquals(SnapshotSchema.V1, blank.schema)

        val failure = runCatching {
            parseSnapshotOptions(
                readBoolean = { _, defaultValue -> defaultValue },
                readString = { "snapshot-latest" },
            )
        }.exceptionOrNull()
        assertTrue(
            "Unknown schema did not produce a useful validation error: $failure",
            failure is IllegalArgumentException && failure.message.orEmpty().contains("snapshot-latest"),
        )

        val paddedFailure = runCatching {
            parseSnapshotOptions(
                readBoolean = { _, defaultValue -> defaultValue },
                readString = { " ${MediainfoSnapshotContract.SCHEMA_V2} " },
            )
        }.exceptionOrNull()
        assertTrue("A padded schema identifier must not be accepted", paddedFailure is IllegalArgumentException)
    }
}
