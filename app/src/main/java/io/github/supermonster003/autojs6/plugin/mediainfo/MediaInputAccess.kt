package io.github.supermonster003.autojs6.plugin.mediainfo

import android.os.Build
import android.os.ParcelFileDescriptor
import android.system.Os
import android.system.OsConstants
import java.io.File

internal enum class MediaInputKind {
    DIRECT_DESCRIPTOR,
    PRIVATE_COPY,
}

internal data class MediaInputSource(
    val path: String,
    val sizeBytes: Long,
    val kind: MediaInputKind,
    val cacheIdentity: MediaFileIdentity? = null,
)

internal object MediaInputAccess {

    const val TEMP_FILE_PREFIX = "autojs6-mediainfo-"

    fun directSource(descriptor: ParcelFileDescriptor, displayName: String? = null): MediaInputSource? {
        val stat = runCatching { Os.fstat(descriptor.fileDescriptor) }.getOrNull() ?: return null
        if (!OsConstants.S_ISREG(stat.st_mode)) return null

        val path = "/proc/self/fd/${descriptor.fd}"
        if (!File(path).canRead()) return null
        return MediaInputSource(
            path = path,
            sizeBytes = stat.st_size.coerceAtLeast(0L),
            kind = MediaInputKind.DIRECT_DESCRIPTOR,
            cacheIdentity = stat.run {
                val resolvedDisplayName = displayName.orEmpty()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1 ||
                    st_dev == 0L || st_ino == 0L || st_size < 0L || st_mtime <= 0L || st_ctime <= 0L ||
                    resolvedDisplayName.length > MAX_CACHE_DISPLAY_NAME_CHARS
                ) {
                    null
                } else {
                    MediaFileIdentity(
                        deviceId = st_dev,
                        inode = st_ino,
                        sizeBytes = st_size,
                        modifiedSeconds = st_mtime,
                        modifiedNanoseconds = st_mtim.tv_nsec,
                        changedSeconds = st_ctime,
                        changedNanoseconds = st_ctim.tv_nsec,
                        displayName = resolvedDisplayName,
                    )
                }
            },
        )
    }

    fun rewindForCopy(descriptor: ParcelFileDescriptor) {
        runCatching {
            Os.lseek(descriptor.fileDescriptor, 0L, OsConstants.SEEK_SET)
        }
    }

    private const val MAX_CACHE_DISPLAY_NAME_CHARS = 512
}
