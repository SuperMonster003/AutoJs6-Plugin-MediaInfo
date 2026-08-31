enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "autojs6-plugin-mediainfo"

// Keep the included build reproducible on clean CI workers. Its settings script reads this
// value while it is being configured, before the root project plugins are resolved.
System.setProperty("org.gradle.toolchains.foojay-resolver-convention", "1.0.0")
System.setProperty("gradle.java.version.overridden.by.user", "")
System.setProperty("gradle.java.version.coerced.by.gradle", "")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
}

plugins {
    // @Hint by SuperMonster003 on Sep 14, 2025.
    //  ! Enable JDK auto-resolution/download capability for build modules.
    //  ! zh-CN: 让构建模块具备 JDK 自动解析/下载能力.
    id("org.gradle.toolchains.foojay-resolver-convention")
}

includeBuild("build-logic")

private val libs = emptyList<String>()

include(
    ":app",
    *libs.map { ":libs:$it" }.toTypedArray(),
)
