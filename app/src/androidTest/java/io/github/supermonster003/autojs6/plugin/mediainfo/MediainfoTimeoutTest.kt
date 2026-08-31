package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/** Explicit end-to-end validation for the service timeout and fallback cleanup path. */
@RunWith(AndroidJUnit4::class)
class MediainfoTimeoutTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private val arguments = InstrumentationRegistry.getArguments()

    @Test
    fun stalledPipeIsCanceledAtTheDeadlineWithoutLeakingItsTemporaryFile() {
        assumeTrue(
            "Manual MediaInfo timeout validation; pass -e $ARG_ENABLED true",
            arguments.getString(ARG_ENABLED).toBoolean(),
        )

        val temporaryFilesBefore = mediaTempFileNames()
        withBoundPlugin { plugin ->
            val (readSide, writeSide) = ParcelFileDescriptor.createPipe()
            val watchdogFinished = CountDownLatch(1)
            val watchdog = Thread {
                if (!watchdogFinished.await(WATCHDOG_SECONDS, TimeUnit.SECONDS)) {
                    runCatching { writeSide.close() }
                }
            }.apply {
                name = "mediainfo-timeout-test-watchdog"
                isDaemon = true
                start()
            }

            val startedAt = SystemClock.elapsedRealtime()
            val failure = try {
                plugin.inform(readSide, "stalled-pipe.media")
                fail("A stalled pipe must not outlive the MediaInfo deadline")
                null
            } catch (error: Throwable) {
                error
            } finally {
                watchdogFinished.countDown()
                runCatching { readSide.close() }
                runCatching { writeSide.close() }
                watchdog.join(TimeUnit.SECONDS.toMillis(5))
            }
            val elapsedMillis = SystemClock.elapsedRealtime() - startedAt

            assertTrue(
                "Timeout error did not expose the stable MEDIAINFO_TIMEOUT code: $failure",
                generateSequence(failure) { it.cause }
                    .any { it.message.orEmpty().contains(ERROR_CODE) },
            )
            assertTrue(
                "MediaInfo call ended before its configured deadline: ${elapsedMillis}ms",
                elapsedMillis >= MIN_EXPECTED_TIMEOUT_MILLIS,
            )
            assertTrue(
                "MediaInfo cancellation did not release the stalled pipe promptly: ${elapsedMillis}ms",
                elapsedMillis < MAX_EXPECTED_TIMEOUT_MILLIS,
            )
        }
        assertEquals(temporaryFilesBefore, mediaTempFileNames())
    }

    private fun <T> withBoundPlugin(block: (IMediainfoPlugin) -> T): T {
        val connected = CountDownLatch(1)
        val binder = AtomicReference<IBinder?>()
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binder.set(service)
                connected.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit
        }
        val intent = Intent(ACTION_MEDIAINFO).setPackage(context.packageName)
        check(context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
            "MediaInfo plugin service could not be bound"
        }
        try {
            check(connected.await(SERVICE_BIND_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                "Timed out while binding the MediaInfo plugin service"
            }
            return block(IMediainfoPlugin.Stub.asInterface(requireNotNull(binder.get())))
        } finally {
            context.unbindService(connection)
        }
    }

    private fun mediaTempFileNames(): Set<String> = context.cacheDir
        .listFiles { _, name -> name.startsWith(MediaInputAccess.TEMP_FILE_PREFIX) }
        .orEmpty()
        .mapTo(mutableSetOf()) { it.name }

    private companion object {
        const val ARG_ENABLED = "mediainfoTimeoutTest"
        const val ACTION_MEDIAINFO = "org.autojs.plugin.MEDIAINFO"
        const val ERROR_CODE = "MEDIAINFO_TIMEOUT"
        const val SERVICE_BIND_TIMEOUT_SECONDS = 10L
        const val WATCHDOG_SECONDS = 45L
        const val MIN_EXPECTED_TIMEOUT_MILLIS = 28_000L
        const val MAX_EXPECTED_TIMEOUT_MILLIS = 40_000L
    }
}
