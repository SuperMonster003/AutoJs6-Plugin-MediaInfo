package io.github.supermonster003.autojs6.plugin.mediainfo

import org.mediainfo.android.MediaInfo
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

internal class MediaInfoCallGuard {

    private val canceled = AtomicBoolean(false)
    private val activeMediaInfo = AtomicReference<MediaInfo?>()
    private val activeCloseable = AtomicReference<Closeable?>()

    val isCanceled: Boolean
        get() = canceled.get()

    fun cancel() {
        if (!canceled.compareAndSet(false, true)) return
        activeMediaInfo.get()?.cancel()
        activeCloseable.get()?.let { closeable -> runCatching { closeable.close() } }
    }

    fun throwIfCanceled() {
        if (isCanceled) throw MediaInfoCallCanceledException()
    }

    fun <T> withMediaInfo(block: (MediaInfo) -> T): T {
        throwIfCanceled()
        val mediaInfo = MediaInfo()
        activeMediaInfo.set(mediaInfo)
        if (isCanceled) mediaInfo.cancel()
        return try {
            block(mediaInfo).also { throwIfCanceled() }
        } finally {
            activeMediaInfo.compareAndSet(mediaInfo, null)
        }
    }

    fun register(closeable: Closeable) {
        activeCloseable.set(closeable)
        if (isCanceled) {
            runCatching { closeable.close() }
            activeCloseable.compareAndSet(closeable, null)
            throwIfCanceled()
        }
    }

    fun unregister(closeable: Closeable) {
        activeCloseable.compareAndSet(closeable, null)
    }

    fun release() {
        activeMediaInfo.set(null)
        activeCloseable.set(null)
    }
}

internal class MediaInfoCallCanceledException : RuntimeException()
