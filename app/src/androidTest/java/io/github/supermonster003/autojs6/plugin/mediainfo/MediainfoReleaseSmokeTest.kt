package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.autojs.plugin.mediainfo.api.IMediainfoPlugin
import org.autojs.plugin.mediainfo.api.MediainfoOptionKeys
import org.autojs.plugin.mediainfo.api.MediainfoSnapshotSchemas
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/** Public-contract smoke test that can instrument an independently minified release APK. */
@RunWith(AndroidJUnit4::class)
class MediainfoReleaseSmokeTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun minifiedApkLoadsNativeEngineAndServesEveryAidlReadMethod() {
        val mediaFile = File(context.cacheDir, "mediainfo-release-smoke.wav")
            .apply { writeBytes(createWaveBytes()) }
        try {
            withBoundPlugin { plugin ->
                val info = plugin.info
                val installedVersion = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionName
                    .orEmpty()
                assertEquals(installedVersion, info.versionName)
                assertTrue(info.supportedAbis.orEmpty().isNotEmpty())

                val inform = withDescriptor(mediaFile) { descriptor ->
                    plugin.inform(descriptor, mediaFile.name)
                }
                assertTrue("Minified APK could not load or call MediaInfoLib", inform.isNotBlank())
                assertTrue("Release smoke report has no General section", inform.contains("General"))
                assertTrue("Release smoke report has no Audio section", inform.contains("Audio"))

                val format = withDescriptor(mediaFile) { descriptor ->
                    plugin.get(descriptor, mediaFile.name, "audio", 0, "Format")
                }
                assertTrue("Unexpected release audio format: $format", format.contains("PCM", ignoreCase = true))

                val options = Bundle().apply {
                    putBoolean(MediainfoOptionKeys.INCLUDE_INFORM, false)
                    putBoolean(MediainfoOptionKeys.INCLUDE_SECTIONS, true)
                }
                val snapshot = withDescriptor(mediaFile) { descriptor ->
                    JSONObject(plugin.snapshot(descriptor, mediaFile.name, options))
                }
                assertEquals("autojs6-plugin-mediainfo-snapshot-v1", snapshot.getString("schema"))
                assertEquals("", snapshot.getString("inform"))
                assertTrue(snapshot.getJSONObject("sections").getJSONArray("audio").length() > 0)

                val v2Options = Bundle(options).apply {
                    putString(
                        MediainfoOptionKeys.SCHEMA,
                        MediainfoSnapshotSchemas.V2,
                    )
                }
                val v2Snapshot = withDescriptor(mediaFile) { descriptor ->
                    JSONObject(plugin.snapshot(descriptor, mediaFile.name, v2Options))
                }
                assertEquals(MediainfoSnapshotSchemas.V2, v2Snapshot.getString("schema"))
                assertEquals("MediaInfoLib", v2Snapshot.getJSONObject("engine").getString("name"))
                assertTrue(
                    v2Snapshot.getJSONObject("tracks")
                        .getJSONArray("audio")
                        .getJSONObject(0)
                        .getJSONObject("fields")
                        .getString("Format")
                        .contains("PCM", ignoreCase = true),
                )
            }
        } finally {
            mediaFile.delete()
        }
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

        val connected = CountDownLatch(1)
        val binder = AtomicReference<IBinder?>()
        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                binder.set(service)
                connected.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit

            override fun onNullBinding(name: ComponentName?) = connected.countDown()

            override fun onBindingDied(name: ComponentName?) = connected.countDown()
        }
        assertTrue(
            "Unable to bind the minified MediaInfo service",
            context.bindService(explicitIntent, connection, Context.BIND_AUTO_CREATE),
        )
        try {
            assertTrue("Timed out binding the minified service", connected.await(15, TimeUnit.SECONDS))
            val rawBinder = binder.get()
            assertNotNull("Minified service returned a null Binder", rawBinder)
            block(IMediainfoPlugin.Stub.asInterface(rawBinder))
        } finally {
            context.unbindService(connection)
        }
    }

    private fun <T> withDescriptor(file: File, block: (ParcelFileDescriptor) -> T): T =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use(block)

    private fun createWaveBytes(): ByteArray {
        val sampleRate = 8_000
        val sampleCount = 800
        val channelCount = 1
        val bitsPerSample = 16
        val blockAlign = channelCount * bitsPerSample / 8
        val dataSize = sampleCount * blockAlign
        return ByteBuffer.allocate(44 + dataSize).order(ByteOrder.LITTLE_ENDIAN).apply {
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
    }

    private companion object {
        const val ACTION_MEDIAINFO = "org.autojs.plugin.MEDIAINFO"
        const val CATEGORY_MEDIAINFO = "mediainfo"
    }
}
