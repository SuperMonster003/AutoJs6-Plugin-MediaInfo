import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.util.Properties
import javax.inject.Inject

plugins {
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
            niceSigningConfig?.let { signingConfig = it }
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

dependencies {
    implementation(files("$rootDir/libs/common-plugin-api.aar"))
    implementation(files("$rootDir/libs/mediainfo-api.aar"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.runner)
}

tasks {
    withType(JavaCompile::class.java) {
        options.encoding = "UTF-8"
    }

    register<Copy>("appendDigestToReleasedFiles") {
        description = "Appends CRC32 digest to released APK files"
        
        val src = "release"
        val dst = "${src}s"
        val ext = utils.FILE_EXTENSION_APK

        if (!file(src).isDirectory) {
            return@register
        }

        from(src); into(dst); include("*.$ext")

        rename { name ->
            val abi = name.replace(Regex("^app-(.+?)-$src(\\.$ext)$"), "$1")
            val releasedFileNamePrefix = "${rootProject.name}-v${versions.appVersionName}-$abi"
            utils.digestCRC32(file("${src}/$name")).let { digest ->
                "$releasedFileNamePrefix-$digest.$ext"
            }
        }

        doLast { println("Destination: ${file(dst)}") }
    }
}
