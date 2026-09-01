package io.github.supermonster003.autojs6.plugin.mediainfo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaInfoSnapshotV2ContractTest {

    @Test
    fun groupsRepeatedTracksAndPreservesUpstreamValueTypes() {
        val snapshot = JSONObject(
            MediaInfoSnapshotV2.build(
                fileName = "two-tracks.mka",
                sizeBytes = 4_096,
                inform = "General\nFormat : Matroska",
                includeInform = true,
                includeTracks = true,
                nativeJson = nativeFixture,
            ),
        )

        assertEquals(MediainfoSnapshotContract.SCHEMA_V2, snapshot.getString("schema"))
        assertEquals("two-tracks.mka", snapshot.getJSONObject("file").getString("name"))
        assertEquals(4_096, snapshot.getJSONObject("file").getLong("sizeBytes"))
        assertEquals("General\nFormat : Matroska", snapshot.getString("inform"))

        val engine = snapshot.getJSONObject("engine")
        assertEquals("MediaInfoLib", engine.getString("name"))
        assertEquals("26.05", engine.getString("version"))
        assertEquals("https://mediaarea.net/MediaInfo", engine.getString("url"))
        assertFalse("Unspecified native library keys leaked into the stable engine", engine.has("build"))

        val tracks = snapshot.getJSONObject("tracks")
        assertEquals(1, tracks.getJSONArray("general").length())
        val audio = tracks.getJSONArray("audio")
        assertEquals(2, audio.length())

        val firstAudio = audio.getJSONObject(0)
        val fields = firstAudio.getJSONObject("fields")
        assertEquals("PCM", fields.getString("Format"))
        assertEquals(8_000, fields.getInt("SamplingRate"))
        assertTrue(fields.getBoolean("Default"))
        assertFalse("The grouping discriminator leaked into fields", fields.has("@type"))
        assertEquals(1, firstAudio.getJSONObject("attributes").getInt("@typeorder"))
        assertEquals("Yes", firstAudio.getJSONObject("extra").getString("IsTruncated"))

        assertEquals("AAC", audio.getJSONObject(1).getJSONObject("fields").getString("Format"))
    }

    @Test
    fun payloadFlagsKeepTheEnvelopeAndOmitOptionalContent() {
        val snapshot = JSONObject(
            MediaInfoSnapshotV2.build(
                fileName = "minimal.mka",
                sizeBytes = 10,
                inform = "must not leak",
                includeInform = false,
                includeTracks = false,
                nativeJson = nativeFixture,
            ),
        )

        assertEquals("", snapshot.getString("inform"))
        assertEquals(0, snapshot.getJSONObject("tracks").length())
        assertEquals("MediaInfoLib", snapshot.getJSONObject("engine").getString("name"))
    }

    @Test
    fun rejectsNativeShapesThatCannotSatisfyTheContract() {
        val malformedFixtures = listOf(
            """
                {
                  "creatingLibrary": { "name": "MediaInfoLib", "version": "26.05" },
                  "media": {}
                }
            """.trimIndent(),
            """
                {
                  "creatingLibrary": { "name": "MediaInfoLib", "version": "26.05" },
                  "media": {
                    "track": [
                      { "@type": "Audio Track", "Format": "PCM" }
                    ]
                  }
                }
            """.trimIndent(),
        )

        malformedFixtures.forEach { nativeJson ->
            val failure = runCatching {
                MediaInfoSnapshotV2.build(
                    fileName = "invalid.media",
                    sizeBytes = 0,
                    inform = "",
                    includeInform = false,
                    includeTracks = false,
                    nativeJson = nativeJson,
                )
            }.exceptionOrNull()
            assertTrue("Malformed native JSON was accepted: $nativeJson", failure != null)
        }
    }

    private val nativeFixture = """
        {
          "creatingLibrary": {
            "name": "MediaInfoLib",
            "version": "26.05",
            "url": "https://mediaarea.net/MediaInfo",
            "build": "fixture-only"
          },
          "media": {
            "@ref": "two-tracks.mka",
            "track": [
              {
                "@type": "General",
                "Format": "Matroska",
                "FileSize": 4096
              },
              {
                "@type": "Audio",
                "@typeorder": 1,
                "Format": "PCM",
                "SamplingRate": 8000,
                "Default": true,
                "extra": {
                  "IsTruncated": "Yes"
                }
              },
              {
                "@type": "Audio",
                "@typeorder": 2,
                "Format": "AAC"
              }
            ]
          }
        }
    """.trimIndent()
}
