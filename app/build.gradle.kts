import java.util.zip.CRC32
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.Properties
import javax.inject.Inject

plugins {
    id("io.github.supermonster003.autojs6-native-alignment")
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.mediainfo"

abstract class GenerateMediaInfoMetadataTask : DefaultTask() {

    @get:InputFile
    abstract val sourceFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @TaskAction
    fun generate() {
        fileSystemOperations.sync {
            from(sourceFile)
            into(outputDirectory)
            rename { "mediainfo-upstream.lock.json" }
        }
    }
}

var isSignsValid = false
val useDebugSigningForReleaseSmoke = providers
    .gradleProperty("mediainfo.releaseSmokeDebugSigning")
    .map(String::toBoolean)
    .getOrElse(false)

android {
    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile
    ndkVersion = "29.0.14206865"

    defaultConfig {
        applicationId = globalApplicationId

        minSdk = versions.sdkVersionMin
        targetSdk = versions.sdkVersionTarget
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        resValue("string", "app_name", "MediaInfo")
        resValue("string", "plugin_author", "SuperMonster003")
        resValue("string", "plugin_id", "mediainfo")
        resValue("string", "plugin_engine", "mediainfo")
        resValue("string", "plugin_variant", "default")
        resValue("string", "plugin_version_date", utils.getDateString("MMM d, yyyy", "GMT+08:00"))

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }

        externalNativeBuild {
            cmake {
                arguments += listOf("-DANDROID_STL=c++_static")
                targets += listOf("mediainfo")
            }
        }
    }

    signingConfigs {
        val props = Properties().also { props ->
            File("${project.rootDir}/sign.properties").takeIf { it.exists() }?.let { file ->
                file.inputStream().use { props.load(it) }
                isSignsValid = props.isNotEmpty()
            }
        }
        if (isSignsValid) {
            create("release") {
                storeFile = props["storeFile"]?.let { file(it as String) }
                keyPassword = props["keyPassword"] as String
                keyAlias = props["keyAlias"] as String
                storePassword = props["storePassword"] as String
            }
        }
    }

    buildTypes {
        val niceSigningConfig = takeIf { isSignsValid }?.let {
            signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            niceSigningConfig?.let { signingConfig = it }
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            when {
                niceSigningConfig != null -> signingConfig = niceSigningConfig
                useDebugSigningForReleaseSmoke -> signingConfig = signingConfigs.getByName("debug")
            }
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a", "x86_64", "x86")
            isUniversalApk = true
        }
    }

    buildFeatures {
        aidl = true
        resValues = true
    }

    packaging {
        jniLibs.useLegacyPackaging = true
    }

    externalNativeBuild {
        cmake {
            path = file("../native/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        val variantTaskSuffix = variant.name.replaceFirstChar { character ->
            if (character.isLowerCase()) character.titlecase() else character.toString()
        }
        val metadataTask = tasks.register<GenerateMediaInfoMetadataTask>(
            "generate${variantTaskSuffix}MediaInfoMetadata",
        ) {
            description = "Copies the pinned MediaInfo source manifest into the ${variant.name} APK"
            sourceFile.set(rootProject.layout.projectDirectory.file("native/upstream.lock.json"))
            outputDirectory.set(layout.buildDirectory.dir("generated/mediainfoMetadataAssets/${variant.name}"))
        }
        requireNotNull(variant.sources.assets).addGeneratedSourceDirectory(metadataTask) { task ->
            task.outputDirectory
        }
    }
}

val standaloneAndroidTestKotlinRuntime by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = true
}

dependencies {
    implementation(files("$rootDir/libs/common-plugin-api.aar"))
    implementation(files("$rootDir/libs/mediainfo-api.aar"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.runner)

    // The version-difference test deliberately instruments a minified published
    // release instead of this module's debug APK. Treat the Kotlin runtime as a
    // file collection so AGP packages it in the test APK rather than assuming
    // that every tested APK contains the complete unminified runtime.
    standaloneAndroidTestKotlinRuntime(
        "org.jetbrains.kotlin:kotlin-stdlib:${KotlinCompilerVersion.VERSION}",
    )
    androidTestImplementation(files(standaloneAndroidTestKotlinRuntime))
}

tasks {
    withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
    }

}

tasks.register<Sync>("appendDigestToReleasedFiles") {
    group = "distribution"
    description = "Collects the current signed release APKs with CRC32 filenames."
    dependsOn("assembleRelease")
    val sourceDirectory = layout.buildDirectory.dir("outputs/apk/release")
    val expectedNames = setOf("app-arm64-v8a-release.apk", "app-armeabi-v7a-release.apk", "app-x86_64-release.apk", "app-x86-release.apk", "app-universal-release.apk")
    val destinationDirectory = layout.projectDirectory.dir("releases/${versions.appVersionName}")
    inputs.property("versionName", versions.appVersionName)
    inputs.property("versionCode", versions.appVersionCode)
    doFirst {
        check(isSignsValid) { "Release signing configuration is missing or incomplete" }
        val source = sourceDirectory.get().asFile
        val actualNames = source.listFiles { f -> f.isFile && f.extension == "apk" }
            .orEmpty().mapTo(mutableSetOf()) { it.name }
        check(actualNames == expectedNames) { "Release APK set differs: expected $expectedNames, found $actualNames" }
        @Suppress("UNCHECKED_CAST")
        val metadata = groovy.json.JsonSlurper().parse(source.resolve("output-metadata.json")) as Map<String, Any?>
        val elements = metadata["elements"] as List<*>
        check(elements.size == expectedNames.size)
        elements.forEach { entry ->
            val item = entry as Map<*, *>
            check(item["outputFile"] in expectedNames)
            check(item["versionName"] == versions.appVersionName)
            check((item["versionCode"] as Number).toInt() == versions.appVersionCode)
        }
        val javaExecutable = File(System.getProperty("java.home"), "bin/java" + if (System.getProperty("os.name").startsWith("Windows")) ".exe" else "")
        val verifier = File(androidComponents.sdkComponents.sdkDirectory.get().asFile, "build-tools/${android.buildToolsVersion}/lib/apksigner.jar")
        check(verifier.isFile) { "Android SDK APK signature verifier is unavailable" }
        expectedNames.forEach { name ->
            val process = ProcessBuilder(javaExecutable.path, "-jar", verifier.path, "verify", source.resolve(name).path)
                .redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            check(process.waitFor() == 0) { "Invalid release APK signature: $name: $output" }
        }
    }
    from(sourceDirectory)
    into(destinationDirectory)
    include("*.apk")
    rename { name ->
        val crc = CRC32()
        sourceDirectory.get().file(name).asFile.inputStream().use { input ->
            val buffer = ByteArray(65536)
            while (true) { val size = input.read(buffer); if (size < 0) break; crc.update(buffer, 0, size) }
        }
        val suffix = if (name == "app-release.apk") "" else "-" + name.removePrefix("app-").removeSuffix("-release.apk")
        "${rootProject.name}-v${versions.appVersionName}$suffix-${crc.value.toString(16).uppercase().padStart(8, '0')}.apk"
    }
}
