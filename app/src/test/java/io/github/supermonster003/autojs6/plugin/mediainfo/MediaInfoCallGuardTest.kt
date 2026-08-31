package io.github.supermonster003.autojs6.plugin.mediainfo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.mediainfo.android.MediaInfo
import java.io.Closeable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class MediaInfoCallGuardTest {

    @Test
    fun cancellationClosesTheActiveResourceAndRejectsFurtherWork() {
        val guard = MediaInfoCallGuard()
        val closeCount = AtomicInteger()
        guard.register(Closeable { closeCount.incrementAndGet() })

        guard.cancel()
        guard.cancel()

        assertTrue(guard.isCanceled)
        assertEquals(1, closeCount.get())
        assertCanceled { guard.throwIfCanceled() }

        val lateCloseCount = AtomicInteger()
        assertCanceled { guard.register(Closeable { lateCloseCount.incrementAndGet() }) }
        assertEquals(1, lateCloseCount.get())
    }

    @Test
    fun cancellationSetsTheFlagOnAnActiveMediaInfoInstance() {
        val guard = MediaInfoCallGuard()
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        val nativeFlagWasSet = AtomicBoolean(false)
        val failure = AtomicReference<Throwable?>()
        val worker = Thread {
            try {
                guard.withMediaInfo { mediaInfo ->
                    entered.countDown()
                    check(release.await(5, TimeUnit.SECONDS)) { "Test did not release MediaInfo work" }
                    nativeFlagWasSet.set(mediaInfo.getIsCanceled() == 1)
                }
                fail("Canceled MediaInfo work must not return successfully")
            } catch (error: MediaInfoCallCanceledException) {
                // Expected after the guarded block observes the cooperative-cancel flag.
            } catch (error: Throwable) {
                failure.set(error)
            }
        }.apply {
            name = "mediainfo-call-guard-test"
            start()
        }

        assertTrue("MediaInfo work did not start", entered.await(5, TimeUnit.SECONDS))
        guard.cancel()
        release.countDown()
        worker.join(5_000)

        assertFalse("MediaInfo work did not finish", worker.isAlive)
        failure.get()?.let { throw AssertionError("MediaInfo work failed unexpectedly", it) }
        assertTrue("MediaInfo.cancel() did not set the JNI-polled flag", nativeFlagWasSet.get())
    }

    @Test
    fun unregisterPreventsACompletedResourceFromBeingClosedByLaterCancellation() {
        val guard = MediaInfoCallGuard()
        val closeCount = AtomicInteger()
        val closeable = Closeable { closeCount.incrementAndGet() }
        guard.register(closeable)
        guard.unregister(closeable)

        guard.cancel()

        assertEquals(0, closeCount.get())
    }

    private fun assertCanceled(block: () -> Unit) {
        try {
            block()
            fail("Expected MediaInfoCallCanceledException")
        } catch (_: MediaInfoCallCanceledException) {
            // Expected.
        }
    }
}
