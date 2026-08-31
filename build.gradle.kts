// @Hint: pin build plugins in the repository so clean CI workers do not depend on artifacts
//  ! installed only in a developer's local Maven repository.
//  ! zh-CN: 在仓库内固定构建插件版本, 避免干净的 CI 环境依赖开发机 Maven 本地仓库中的产物.
plugins {
    id("com.android.application") version "9.2.1" apply false
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/public")
    }
}

tasks {
    register<Delete>("clean").configure {
        delete(rootProject.layout.buildDirectory)
    }
}
