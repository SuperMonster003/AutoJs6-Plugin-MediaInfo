package io.github.supermonster003.autojs6.plugin.mediainfo

import android.content.Context
import android.os.Build
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.common.api.PluginInfo
import org.autojs.plugin.mediainfo.api.MediainfoPluginCapabilityKeys
import org.autojs.plugin.mediainfo.api.MediainfoPluginIds
import org.autojs.plugin.mediainfo.api.MediainfoSnapshotSchemas

internal fun Context.pluginInfo(name: String, description: String, engineVersion: String?): PluginInfo {
    val appContext = applicationContext
    val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
    return PluginInfo().apply {
        this.name = name
        this.description = description
        author = appContext.stringResource("plugin_author", "SuperMonster003")
        id = appContext.stringResource("plugin_id", MediainfoPluginIds.ID)
        engine = appContext.stringResource("plugin_engine", MediainfoPluginIds.ENGINE)
        variant = appContext.stringResource("plugin_variant", MediainfoPluginIds.VARIANT_DEFAULT)
        versionName = packageInfo.versionName ?: ""
        versionCode = packageInfo.versionCodeCompat()
        versionDate = appContext.stringResource("plugin_version_date", "")
        supportedAbis = NativeLibraryInventory.supportedAbis(appContext)
        capabilities = android.os.Bundle().apply {
            putInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION, 3923)
            putStringArray(
                MediainfoPluginCapabilityKeys.SNAPSHOT_SCHEMAS,
                MediainfoSnapshotSchemas.VALUES.toTypedArray(),
            )
            putString(
                MediainfoPluginCapabilityKeys.DEFAULT_SNAPSHOT_SCHEMA,
                MediainfoSnapshotSchemas.DEFAULT,
            )
            engineVersion?.takeIf { it.isNotBlank() }?.let { version ->
                putString(MediainfoPluginCapabilityKeys.ENGINE_VERSION, version)
            }
        }
    }
}

private fun Context.stringResource(name: String, fallback: String): String {
    val id = resources.getIdentifier(name, "string", packageName)
    return if (id != 0) resources.getString(id) else fallback
}

private fun android.content.pm.PackageInfo.versionCodeCompat(): Long {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) return longVersionCode
    @Suppress("DEPRECATION")
    return versionCode.toLong()
}
