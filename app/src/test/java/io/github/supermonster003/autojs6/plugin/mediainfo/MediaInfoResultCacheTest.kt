package io.github.supermonster003.autojs6.plugin.mediainfo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaInfoResultCacheTest {

    @Test
    fun storesAllMetadataKindsIncludingEmptyQueryValues() {
        val cache = cache()
        val identity = identity(1)
        val query = MediaGetRequest("AUDIO", 0, "MissingField")
        val snapshot = MediaSnapshotRequest(includeInform = false, includeSections = true)

        cache.putInform(identity, "General\nFormat : Wave")
        cache.putQuery(identity, query, "")
        cache.putSnapshot(identity, snapshot, "{\"schema\":\"snapshot\"}")

        assertEquals("General\nFormat : Wave", cache.getInform(identity))
        assertEquals("", cache.getQuery(identity, query)?.value)
        assertEquals("{\"schema\":\"snapshot\"}", cache.getSnapshot(identity, snapshot))
        assertEquals(MediaCacheStats(1, 1, 1, cache.stats().estimatedCharacters), cache.stats())
    }

    @Test
    fun fileLruEvictsTheLeastRecentlyUsedIdentity() {
        val cache = cache(maxFiles = 2)
        val first = identity(1)
        val second = identity(2)
        val third = identity(3)

        cache.putInform(first, "first")
        cache.putInform(second, "second")
        assertEquals("first", cache.getInform(first))
        cache.putInform(third, "third")

        assertEquals("first", cache.getInform(first))
        assertNull(cache.getInform(second))
        assertEquals("third", cache.getInform(third))
    }

    @Test
    fun ttlIsSlidingAndExpiresIdleEntries() {
        var now = 0L
        val cache = cache(ttlMillis = 100L, nowMillis = { now })
        val identity = identity(1)
        cache.putInform(identity, "report")

        now = 99L
        assertEquals("report", cache.getInform(identity))
        now = 198L
        assertEquals("report", cache.getInform(identity))
        now = 298L
        assertNull(cache.getInform(identity))
        assertEquals(0, cache.stats().fileCount)
    }

    @Test
    fun differentFileMetadataDoesNotCollide() {
        val cache = cache()
        val original = identity(1)
        val changed = original.copy(modifiedNanoseconds = original.modifiedNanoseconds + 1)
        cache.putInform(original, "old")
        cache.putInform(changed, "new")

        assertEquals("old", cache.getInform(original))
        assertEquals("new", cache.getInform(changed))
        assertEquals(2, cache.stats().fileCount)
    }

    @Test
    fun queryLruIsBoundedPerFile() {
        val cache = cache(maxQueriesPerFile = 2)
        val identity = identity(1)
        val first = MediaGetRequest("AUDIO", 0, "First")
        val second = MediaGetRequest("AUDIO", 0, "Second")
        val third = MediaGetRequest("AUDIO", 0, "Third")

        cache.putQuery(identity, first, "1")
        cache.putQuery(identity, second, "2")
        assertEquals("1", cache.getQuery(identity, first)?.value)
        cache.putQuery(identity, third, "3")

        assertEquals("1", cache.getQuery(identity, first)?.value)
        assertNull(cache.getQuery(identity, second))
        assertEquals("3", cache.getQuery(identity, third)?.value)
    }

    @Test
    fun replacingAReportClearsDependentResults() {
        val cache = cache()
        val identity = identity(1)
        val query = MediaGetRequest("GENERAL", 0, "Format")
        val snapshot = MediaSnapshotRequest(true, true)
        cache.putInform(identity, "old")
        cache.putQuery(identity, query, "old query")
        cache.putSnapshot(identity, snapshot, "old snapshot")

        cache.putInform(identity, "new")

        assertEquals("new", cache.getInform(identity))
        assertNull(cache.getQuery(identity, query))
        assertNull(cache.getSnapshot(identity, snapshot))
    }

    @Test
    fun characterBudgetEvictsWholeEldestEntries() {
        val cache = cache(maxCharacters = 12)
        cache.putInform(identity(1), "123456")
        cache.putInform(identity(2), "abcdef")

        assertNull(cache.getInform(identity(1)))
        assertEquals("abcdef", cache.getInform(identity(2)))
        assertEquals(1, cache.stats().fileCount)
    }

    @Test
    fun nullIdentityAndBlankReportsAreNeverCached() {
        val cache = cache()
        val query = MediaGetRequest("GENERAL", 0, "Format")
        val snapshot = MediaSnapshotRequest(true, true)
        cache.putInform(null, "report")
        cache.putInform(identity(1), "")
        cache.putQuery(null, query, "value")
        cache.putSnapshot(null, snapshot, "snapshot")

        assertEquals(MediaCacheStats(0, 0, 0, 0), cache.stats())
    }

    private fun cache(
        maxFiles: Int = 32,
        ttlMillis: Long = 10_000L,
        maxQueriesPerFile: Int = 64,
        maxCharacters: Int = 10_000,
        nowMillis: () -> Long = { 0L },
    ) = MediaInfoResultCache(
        maxFiles = maxFiles,
        ttlMillis = ttlMillis,
        maxQueriesPerFile = maxQueriesPerFile,
        maxCharacters = maxCharacters,
        nowMillis = nowMillis,
    )

    private fun identity(index: Long) = MediaFileIdentity(
        deviceId = 7,
        inode = index,
        sizeBytes = 1_024,
        modifiedSeconds = 100,
        modifiedNanoseconds = 123,
        changedSeconds = 100,
        changedNanoseconds = 456,
        displayName = "f$index",
    )
}
