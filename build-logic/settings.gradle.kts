enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "logic"

include(":convention")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("io.github.supermonster003.autojs6-platform-versions") version "1.6.0"
        id("org.gradle.toolchains.foojay-resolver-convention") version System.getProperty("org.gradle.toolchains.foojay-resolver-convention")
    }
}

plugins {
    id("io.github.supermonster003.autojs6-platform-versions")
    // @Hint by SuperMonster003 on Oct 6, 2025.
    //  ! Enable JDK auto-resolution/download capability for build modules.
    //  ! zh-CN: 让构建模块具备 JDK 自动解析/下载能力.
    id("org.gradle.toolchains.foojay-resolver-convention")
}
