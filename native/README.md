# MediaInfoLib 官方源码构建

本目录是 v2.0.0 及以后版本的唯一原生构建入口. v1.1.0 Release 中的旧二进制已经冻结, 不会由这里重建或替换. 完整的版本边界和发布门禁见 [`MEDIAINFO_UPSTREAM.md`](../MEDIAINFO_UPSTREAM.md).

## 干净检出

两个上游仓库都是固定提交的 Git 子模块. 推荐递归检出:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
```

若已经普通检出, 补执行:

```powershell
git submodule update --init --recursive
```

不要在 `native/upstream/MediaInfoLib` 或 `native/upstream/ZenLib` 内提交本地补丁. 项目维护的兼容代码只放在 `native/bridge`.

## 固定输入

[`upstream.lock.json`](upstream.lock.json) 是机器可读的来源清单, 同时记录:

- MediaInfoLib / ZenLib 的官方仓库, 正式标签和完整提交;
- 上游许可证位置及 APK 内的许可副本;
- Android NDK, CMake, minSdk, STL, ABI 和 ELF 最大页大小;
- Unicode, 64 位大文件和精简 feature profile.

Gradle 固定使用 NDK `29.0.14206865` 与 CMake `3.22.1`. Android SDK 未安装它们时可执行:

```powershell
sdkmanager "ndk;29.0.14206865" "cmake;3.22.1"
```

## 构建与校验

一次 debug 构建会从相同源码生成四个 ABI 和五个 APK:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

随后校验子模块, 标签, 许可, ELF 架构, 16 KB LOAD 对齐, SONAME, 动态依赖, 导出面和 APK 内容:

```powershell
python scripts/update_mediainfo_upstream.py --fetch-locked
python scripts/verify_native_build.py `
  --library-root app/build/intermediates/stripped_native_libs/debug/stripDebugDebugSymbols/out/lib `
  --apk-dir app/build/outputs/apk/debug
```

校验器要求动态依赖严格为 Android 系统的 `libz.so`, `libm.so`, `libdl.so`, `libc.so`, 动态导出严格为 `JNI_OnLoad`; `libzen.so` 与 `libc++_shared.so` 均不得出现在 APK 中.

## 上游更新

[`update-mediainfo-upstream.yml`](../.github/workflows/update-mediainfo-upstream.yml) 每周检查两个官方仓库的最新稳定 Release, 也支持手动触发. [`update_mediainfo_upstream.py`](../scripts/update_mediainfo_upstream.py) 只在版本号递增时移动固定引用并刷新来源锁和许可副本; 同名标签若解析到不同提交会直接失败.

自动化只创建或刷新 Draft PR. 它不会合并 PR, 修改插件版本, 创建 Release 或跳过 ARM 实机, 真实媒体, 超时和超大文件门禁.
