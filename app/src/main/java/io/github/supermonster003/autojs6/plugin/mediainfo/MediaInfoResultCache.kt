package io.github.supermonster003.autojs6.plugin.mediainfo

import java.util.LinkedHashMap

internal data class MediaFileIdentity(
    val deviceId: Long,
    val inode: Long,
    val sizeBytes: Long,
    val modifiedSeconds: Long,
    val modifiedNanoseconds: Long,
    val changedSeconds: Long,
    val changedNanoseconds: Long,
    val displayName: String,
)

internal data class MediaGetRequest(
    val streamKind: String,
    val streamNumber: Int,
    val parameter: String,
)

internal data class MediaSnapshotRequest(
    val includeInform: Boolean,
    val includeSections: Boolean,
)

internal data class MediaCacheHit<out T>(val value: T)

internal data class MediaCacheStats(
    val fileCount: Int,
    val queryCount: Int,
    val snapshotCount: Int,
    val estimatedCharacters: Int,
)

/**
 * Process-local metadata cache. It never stores media bytes and is intentionally
 * bounded by file count, query count, age and an estimated UTF-16 character budget.
 */
internal class MediaInfoResultCache(
    private val maxFiles: Int = DEFAULT_MAX_FILES,
    private val ttlMillis: Long = DEFAULT_TTL_MILLIS,
    private val maxQueriesPerFile: Int = DEFAULT_MAX_QUERIES_PER_FILE,
    private val maxCharacters: Int = DEFAULT_MAX_CHARACTERS,
    private val nowMillis: () -> Long = { System.nanoTime() / NANOS_PER_MILLISECOND },
) {

    private inner class Entry(var lastAccessMillis: Long) {
        var inform: String? = null
        val snapshots = mutableMapOf<MediaSnapshotRequest, String>()
        val queries = object : LinkedHashMap<MediaGetRequest, String>(16, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<MediaGetRequest, String>?): Boolean =
                size > maxQueriesPerFile
        }

        fun estimatedCharacters(identity: MediaFileIdentity): Int {
            var result = identity.displayName.length + (inform?.length ?: 0)
            snapshots.forEach { (_, value) ->
                result += value.length + SNAPSHOT_KEY_ESTIMATE
            }
            queries.forEach { (request, value) ->
                result += request.streamKind.length + request.parameter.length + value.length + QUERY_KEY_ESTIMATE
            }
            return result
        }
    }

    private val entries = object : LinkedHashMap<MediaFileIdentity, Entry>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<MediaFileIdentity, Entry>?): Boolean =
            size > maxFiles
    }

    init {
        require(maxFiles > 0) { "maxFiles must be positive" }
        require(ttlMillis > 0L) { "ttlMillis must be positive" }
        require(maxQueriesPerFile > 0) { "maxQueriesPerFile must be positive" }
        require(maxCharacters > 0) { "maxCharacters must be positive" }
    }

    @Synchronized
    fun getInform(identity: MediaFileIdentity?): String? =
        identity?.let { getEntry(it)?.inform }

    @Synchronized
    fun putInform(identity: MediaFileIdentity?, inform: String) {
        if (identity == null || inform.isBlank()) return
        val entry = getOrCreateEntry(identity)
        if (entry.inform != null && entry.inform != inform) {
            entry.queries.clear()
            entry.snapshots.clear()
        }
        entry.inform = inform
        trimToCharacterBudget()
    }

    @Synchronized
    fun getQuery(identity: MediaFileIdentity?, request: MediaGetRequest): MediaCacheHit<String>? =
        identity?.let { key -> getEntry(key)?.queries?.get(request)?.let(::MediaCacheHit) }

    @Synchronized
    fun putQuery(identity: MediaFileIdentity?, request: MediaGetRequest, value: String) {
        if (identity == null) return
        getOrCreateEntry(identity).queries[request] = value
        trimToCharacterBudget()
    }

    @Synchronized
    fun getSnapshot(identity: MediaFileIdentity?, request: MediaSnapshotRequest): String? =
        identity?.let { key -> getEntry(key)?.snapshots?.get(request) }

    @Synchronized
    fun putSnapshot(identity: MediaFileIdentity?, request: MediaSnapshotRequest, value: String) {
        if (identity == null || value.isBlank()) return
        getOrCreateEntry(identity).snapshots[request] = value
        trimToCharacterBudget()
    }

    @Synchronized
    fun clear() {
        entries.clear()
    }

    @Synchronized
    fun stats(): MediaCacheStats {
        pruneExpired(nowMillis())
        return MediaCacheStats(
            fileCount = entries.size,
            queryCount = entries.values.sumOf { it.queries.size },
            snapshotCount = entries.values.sumOf { it.snapshots.size },
            estimatedCharacters = estimatedCharacters(),
        )
    }

    private fun getEntry(identity: MediaFileIdentity): Entry? {
        val now = nowMillis()
        pruneExpired(now)
        return entries[identity]?.also { it.lastAccessMillis = now }
    }

    private fun getOrCreateEntry(identity: MediaFileIdentity): Entry {
        val now = nowMillis()
        pruneExpired(now)
        return entries[identity]?.also { it.lastAccessMillis = now }
            ?: Entry(now).also { entries[identity] = it }
    }

    private fun pruneExpired(now: Long) {
        val iterator = entries.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next().value
            if (now - entry.lastAccessMillis >= ttlMillis) iterator.remove()
        }
    }

    private fun trimToCharacterBudget() {
        while (entries.isNotEmpty() && estimatedCharacters() > maxCharacters) {
            val eldest = entries.entries.iterator().next()
            entries.remove(eldest.key)
        }
    }

    private fun estimatedCharacters(): Int = entries.entries.sumOf { (identity, entry) ->
        entry.estimatedCharacters(identity)
    }

    companion object {
        const val DEFAULT_MAX_FILES = 32
        const val DEFAULT_TTL_MILLIS = 10L * 60L * 1_000L
        const val DEFAULT_MAX_QUERIES_PER_FILE = 64
        const val DEFAULT_MAX_CHARACTERS = 2 * 1_024 * 1_024

        private const val NANOS_PER_MILLISECOND = 1_000_000L
        private const val SNAPSHOT_KEY_ESTIMATE = 2
        private const val QUERY_KEY_ESTIMATE = 8
    }
}
