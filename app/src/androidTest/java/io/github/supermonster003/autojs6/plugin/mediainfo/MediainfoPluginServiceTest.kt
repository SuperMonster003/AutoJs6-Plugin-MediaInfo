package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.autojs.plugin.mediainfo.api.MediainfoOptionKeys
import org.autojs.plugin.mediainfo.api.MediainfoPluginIds
import org.json.JSONObject
import org.mediainfo.android.MediaInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class MediainfoPluginServiceTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun discoveryBindingAndAllBinderMethodsRoundTrip() {
        withBoundPlugin { plugin ->
            assertRuntimeInfo(plugin)

            val mediaFile = createWaveFile()
            try {
                assertRegularDescriptorCanBeParsedDirectly(mediaFile)

                val inform = withMediaDescriptor(mediaFile) { descriptor ->
                    plugin.inform(descriptor, mediaFile.name)
                }
                assertTrue("MediaInfo inform report is empty", inform.isNotBlank())
                assertTrue("MediaInfo inform report has no General section", inform.contains("General"))
                assertTrue("MediaInfo inform report has no Audio section", inform.contains("Audio"))
                val cachedInform = withMediaDescriptor(mediaFile) { descriptor ->
                    plugin.inform(descriptor, mediaFile.name)
                }
                assertEquals(inform, cachedInform)

                val format = withMediaDescriptor(mediaFile) { descriptor ->
                    plugin.get(descriptor, mediaFile.name, "audio", 0, "Format")
                }
                assertTrue("Unexpected audio format: $format", format.contains("PCM", ignoreCase = true))

                val options = Bundle().apply {
                    putBoolean(MediainfoOptionKeys.INCLUDE_INFORM, false)
                    putBoolean(MediainfoOptionKeys.INCLUDE_SECTIONS, true)
                }
                val snapshot = withMediaDescriptor(mediaFile) { descriptor ->
                    JSONObject(plugin.snapshot(descriptor, mediaFile.name, options))
                }
                assertEquals("autojs6-plugin-mediainfo-snapshot-v1", snapshot.getString("schema"))
                assertEquals(mediaFile.name, snapshot.getString("fileName"))
                assertEquals(mediaFile.length(), snapshot.getLong("sizeBytes"))
                assertEquals("", snapshot.getString("inform"))
                val sections = snapshot.getJSONObject("sections")
                assertTrue("Snapshot has no General section", sections.getJSONArray("general").length() > 0)
                assertTrue("Snapshot has no Audio section", sections.getJSONArray("audio").length() > 0)
                assertTrue(
                    "Snapshot audio format is not PCM",
                    sections.getJSONArray("audio")
                        .getJSONObject(0)
                        .getString("format")
                        .contains("PCM", ignoreCase = true),
                )
            } finally {
                mediaFile.delete()
            }
        }
    }

    @Test
    fun nonSeekablePipeFallsBackWithoutLeakingTemporaryFiles() {
        val temporaryFilesBefore = mediaTempFileNames()
        withBoundPlugin { plugin ->
            val (readSide, writeSide) = ParcelFileDescriptor.createPipe()
            assertNull("A pipe must not be treated as a direct media source", MediaInputAccess.directSource(readSide))

            val writerFailure = AtomicReference<Throwable?>()
            val writer = Thread {
                try {
                    ParcelFileDescriptor.AutoCloseOutputStream(writeSide).use { output ->
                        output.write(createWaveBytes())
                    }
                } catch (throwable: Throwable) {
                    writerFailure.set(throwable)
                }
            }.apply {
                name = "mediainfo-test-pipe-writer"
                start()
            }

            val inform = try {
                plugin.inform(readSide, "mediainfo-pipe.wav")
            } finally {
                readSide.close()
                writer.join(10_000)
            }
            assertFalse("Pipe writer did not finish", writer.isAlive)
            writerFailure.get()?.let { throw AssertionError("Pipe writer failed", it) }
            assertTrue("Fallback inform report is empty", inform.isNotBlank())
            assertTrue("Fallback report has no Audio section", inform.contains("Audio"))
        }
        assertEquals(temporaryFilesBefore, mediaTempFileNames())
    }

    @Test
    fun invalidStreamKindClosesTheServiceOwnedDescriptor() {
        withBoundPlugin { plugin ->
            val mediaFile = createWaveFile()
            try {
                val descriptor = ParcelFileDescriptor.open(mediaFile, ParcelFileDescriptor.MODE_READ_ONLY)
                try {
                    val failure = runCatching {
                        plugin.get(descriptor, mediaFile.name, "not-a-stream", 0, "Format")
                    }.exceptionOrNull()
                    assertTrue(
                        "Invalid stream kind did not produce the expected error: $failure",
                        failure is IllegalArgumentException &&
                            failure.message.orEmpty().contains("not-a-stream"),
                    )
                    assertFalse(
                        "The service retained its descriptor after request validation failed",
                        descriptor.fileDescriptor.valid(),
                    )
                } finally {
                    runCatching { descriptor.close() }
                }
            } finally {
                mediaFile.delete()
            }
        }
    }

    private fun assertRuntimeInfo(plugin: IMediainfoPlugin) {
        val info = plugin.info
        assertEquals(MediainfoPluginIds.ID, info.id)
        assertEquals(MediainfoPluginIds.ENGINE, info.engine)
        assertEquals(MediainfoPluginIds.VARIANT_DEFAULT, info.variant)
        val installedVersionName = context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
            .orEmpty()
        assertEquals(installedVersionName, info.versionName)
        assertTrue(info.name?.isNotBlank() == true)
        assertTrue(info.description?.isNotBlank() == true)

        val processAbis = if (android.os.Process.is64Bit()) {
            Build.SUPPORTED_64_BIT_ABIS
        } else {
            Build.SUPPORTED_32_BIT_ABIS
        }
        val knownAbis = setOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        val expectedAbi = processAbis.first { it in knownAbis }
        assertTrue(
            "Plugin APK does not advertise the current process ABI $expectedAbi: " +
                info.supportedAbis.orEmpty().contentToString(),
            expectedAbi in info.supportedAbis.orEmpty(),
        )

        val capabilities = requireNotNull(info.capabilities) { "Plugin capabilities are missing" }
        assertEquals(3923, capabilities.getInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION))
    }

    private fun withBoundPlugin(block: (IMediainfoPlugin) -> Unit) {
        val discoveryIntent = Intent(ACTION_MEDIAINFO)
            .addCategory(CATEGORY_MEDIAINFO)
            .setPackage(context.packageName)
        @Suppress("DEPRECATION")
        val matches = context.packageManager.queryIntentServices(discoveryIntent, 0)
        assertEquals("MediaInfo discovery must resolve exactly one service", 1, matches.size)

        val serviceInfo = matches.single().serviceInfo
        val explicitIntent = Intent(discoveryIntent).setComponent(
            ComponentName(serviceInfo.packageName, serviceInfo.name),
        )
        val latch = CountDownLatch(1)
        val binder = AtomicReference<IBinder?>()
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binder.set(service)
                latch.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit

            override fun onNullBinding(name: ComponentName?) {
                latch.countDown()
            }

            override fun onBindingDied(name: ComponentName?) {
                latch.countDown()
            }
        }

        assertTrue(
            "Unable to bind the discovered MediaInfo service",
            context.bindService(explicitIntent, connection, Context.BIND_AUTO_CREATE),
        )
        try {
            assertTrue("Timed out binding the MediaInfo service", latch.await(15, TimeUnit.SECONDS))
            val rawBinder = binder.get()
            assertNotNull("MediaInfo service returned a null Binder", rawBinder)
            block(IMediainfoPlugin.Stub.asInterface(rawBinder))
        } finally {
            context.unbindService(connection)
        }
    }

    private fun assertRegularDescriptorCanBeParsedDirectly(file: File) {
        withMediaDescriptor(file) { descriptor ->
            val source = requireNotNull(MediaInputAccess.directSource(descriptor, file.name)) {
                "A regular file descriptor should expose a direct source"
            }
            assertEquals(MediaInputKind.DIRECT_DESCRIPTOR, source.kind)
            assertEquals(file.length(), source.sizeBytes)
            assertTrue(source.path.startsWith("/proc/self/fd/"))
            assertEquals(file.name, requireNotNull(source.cacheIdentity).displayName)
            assertTrue(MediaInfo().getMI(source.path).contains("Audio"))
        }
    }

    private fun createWaveFile(): File =
        File(context.cacheDir, "mediainfo-service-roundtrip.wav").apply { writeBytes(createWaveBytes()) }

    private fun createWaveBytes(): ByteArray {
        val sampleRate = 8_000
        val sampleCount = 800
        val channelCount = 1
        val bitsPerSample = 16
        val blockAlign = channelCount * bitsPerSample / 8
        val dataSize = sampleCount * blockAlign
        val bytes = ByteBuffer.allocate(44 + dataSize).order(ByteOrder.LITTLE_ENDIAN).apply {
            put("RIFF".toByteArray(Charsets.US_ASCII))
            putInt(36 + dataSize)
            put("WAVE".toByteArray(Charsets.US_ASCII))
            put("fmt ".toByteArray(Charsets.US_ASCII))
            putInt(16)
            putShort(1)
            putShort(channelCount.toShort())
            putInt(sampleRate)
            putInt(sampleRate * blockAlign)
            putShort(blockAlign.toShort())
            putShort(bitsPerSample.toShort())
            put("data".toByteArray(Charsets.US_ASCII))
            putInt(dataSize)
            repeat(sampleCount) { putShort(0) }
        }.array()
        return bytes
    }

    private fun mediaTempFileNames(): Set<String> = context.cacheDir
        .listFiles { _, name -> name.startsWith(MediaInputAccess.TEMP_FILE_PREFIX) }
        .orEmpty()
        .mapTo(mutableSetOf()) { it.name }

    private fun <T> withMediaDescriptor(file: File, block: (ParcelFileDescriptor) -> T): T =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use(block)

    private companion object {
        const val ACTION_MEDIAINFO = "org.autojs.plugin.MEDIAINFO"
        const val CATEGORY_MEDIAINFO = "mediainfo"
    }
}
