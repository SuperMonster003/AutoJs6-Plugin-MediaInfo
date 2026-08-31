# MediaInfoLib 上游与版本边界策略

更新日期: 2026-08-31

本文档固定 M5 原生库演进的版本边界, 源码来源, 更新方式与验收门禁. 它只约束插件自 v1.1.0 起的原生引擎维护方式, 不改变已经发布的 v1.1.0 行为或资产.

******

## 已决定的版本边界

******

| 插件版本 | 原生引擎来源 | 维护策略 |
|---|---|---|
| v1.1.0 | `olegazyx/MediaInfoLib-android` 的旧构建体系; 该仓库内置 MediaInfoLib 0.7.83 源码 | v1 系列最终冻结点. `v1.1.0` 标签, 源码树, 五个 Release APK 及其 `libmediainfo.so` 永久保留, 不重建, 不替换, 不把新版 MediaInfoLib 回移到该版本 |
| v2.0.0 | MediaArea 官方 MediaInfoLib / ZenLib 固定正式版源码 | 首个官方源码构建版本. 初始目标为 MediaInfoLib v26.05 与 ZenLib v0.4.41; 全部 ABI 使用同一套可复现配置构建 |
| v2.x 及以后 | MediaArea 官方稳定 Release | 定时发现新版本, 以固定标签和完整提交创建更新 PR; 通过回归后人工合并与发布, 不直接跟随移动的 `master` |

插件版本与引擎版本彼此独立. 例如发布信息应写作 `AutoJs6-Plugin-MediaInfo v2.0.0 / MediaInfoLib 26.05`, 而不是为了与上游编号一致而跳改插件版本.

`v1.1.0` 的旧仓库明确携带 `libmediainfo_0.7.83` 源码, 但现有四种 ABI 预编译文件并没有一套可从现代环境完整复现的来源链. 因而冻结的权威对象是 v1.1.0 实际发布的源码和二进制本身; 不用重新编译来“修复”其来源记录.

******

## 官方仓库的职责

******

- [`MediaArea/MediaInfo`](https://github.com/MediaArea/MediaInfo) 是 GUI / CLI 应用仓库. 本地 `T:\backups\idea-projects\MediaInfo` 当前位于 `v26.05-30-g1d67fd0c8`, 即比 v26.05 标签多 30 个提交; 它可作为官方 Android 构建配置的参考, 但不作为插件的移动依赖.
- [`MediaArea/MediaInfoLib`](https://github.com/MediaArea/MediaInfoLib) 是插件真正需要编译和跟踪的解析引擎. 官方 CMake 目标名为 `mediainfo`, 源码中包含 Android JNI 与 `OpenFd` 支持.
- [`MediaArea/ZenLib`](https://github.com/MediaArea/ZenLib) 是 MediaInfoLib 源码构建所需的同组织基础库. 它必须与 MediaInfoLib 一起固定, 不能只记录主库版本.
- [`olegazyx/MediaInfoLib-android`](https://github.com/olegazyx/MediaInfoLib-android) 自 v2.0.0 起只保留历史归属与许可说明, 不再作为源码, JNI 或二进制依赖.

官方 MediaInfo Android 工程已经直接通过 CMake 引入同级 `MediaInfoLib/Project/CMake`, 并声明 `armeabi-v7a`, `arm64-v8a`, `x86` 与 `x86_64` 四种 ABI. 因此从官方源码构建 Android 动态库不是推测性路线, 而是官方自身持续使用的构建方式.

******

## v2.0.0 初始固定值

******

| 项目 | 固定值 | 说明 |
|---|---|---|
| MediaInfoLib | `v26.05` / `f23e69ce89581343f3b7a42e06828a5331e290d5` | 2026-05-12 发布的正式版; 标签直接指向该提交 |
| ZenLib | `v0.4.41` / `894980d3ecbc843d6ac685493f8f2ed5c2b6864c` | 官方正式版; 标签直接指向该提交 |
| Android NDK | `29.0.14206865` | v2 基线工具链; 默认提供 16 KB ELF 对齐能力 |
| CMake | `3.22.1` | 与当前 Android 工程和本机构建环境兼容的固定版本 |
| Android API | 最低 24; 目标 36 | 与插件当前 `minSdk` / `targetSdk` 保持一致 |
| ABI | `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` | 与当前五种 APK 交付方式一致 |

固定标签之外还必须记录完整提交, 因为标签名称本身不是可复现构建的充分证据. CI 应拒绝“标签解析出的提交”和来源清单不一致的情况.

******

## ARM64 源码构建验证

******

2026-08-31 已在不改动插件 APK 和 `app/src/main/jniLibs` 的前提下完成一次独立验证:

- 官方源码分别精确检出到 `T:\backups\idea-projects\MediaInfoLib` (`v26.05`) 和 `T:\backups\idea-projects\ZenLib` (`v0.4.41`), 两个工作树均为 detached HEAD 且无本地修改.
- 使用 NDK `29.0.14206865`, CMake `3.22.1`, Android API 24, `arm64-v8a`, Release 与 `c++_static` 配置, 并沿用官方 Android 工程的精简 feature flags; 大文件支持显式开启.
- ZenLib 的 CMake 最低版本策略会在未指定时把它意外构建成共享库. 验证通过 `CMAKE_POLICY_DEFAULT_CMP0077=NEW` 使 MediaInfoLib 保持共享库, ZenLib 静态并入, 最终 `libmediainfo.so` 不依赖额外的 `libzen.so`.
- 最终未剥离产物为 AArch64 ELF, SHA-256 为 `a91944ef787ac911e482002f1bc546596e9a8fdf6f46cbe4088fa2f62a8e3a32`, 三个 LOAD segment 对齐均为 `0x4000`; 动态依赖仅为 Android 系统的 `libz.so`, `libm.so`, `libdl.so` 与 `libc.so`.
- `JNI_OnLoad`, `MediaInfo_Open`, `MediaInfo_Inform`, `MediaInfo_Get`, `MediaInfo_Option` 等入口均已导出; 源码版本常量为 `MediaInfoLib - v26.05`. `llvm-strip --strip-unneeded` 后的验证副本为 10,048,424 字节.

验证产物保留在 `T:\backups\idea-projects\MediaInfo-v2-build-proof\arm64-v8a`. 它不是发布输入, 也没有复制进本仓库; 正式 v2 构建仍需把相同规则纳入 Gradle / CMake, CI 和四 ABI 回归.

******

## 集成结构

******

v2 实现时采用以下职责分离:

```text
native/
|-- upstream/
|   |-- MediaInfoLib/  # 固定提交的官方源码
|   `-- ZenLib/        # 固定提交的官方源码
|-- bridge/            # 本项目维护的最小 JNI 兼容与取消桥
`-- upstream.lock      # 仓库, 标签, 提交, 工具链与编译选项
```

- 上游目录使用固定提交的 Git 子模块或等价的内容寻址检出; 两个上游目录保持同级, 以符合官方 CMake 的相对路径布局.
- 本项目的补丁只放在 `native/bridge` 或显式补丁文件中. 不在上游子模块工作树内直接提交混合修改, 这样每次升级都能清楚审阅本地差异.
- APK 中的 `libmediainfo.so` 由 Gradle / CMake 构建图生成, 不再从个人仓库复制预编译文件.
- CMake 入口固定 `CMP0077=NEW`, 让 ZenLib 静态并入 MediaInfoLib; CI 拒绝 `DT_NEEDED` 中出现非 Android 系统库, 防止漏打包的共享依赖.
- 来源清单同时进入 APK 元数据或资源, 使运行时诊断, Release 说明和可复现构建使用同一来源.

******

## 为什么仍需要本地 JNI 桥

******

官方 JNI 不是当前插件 JNI 的直接替换件:

- 官方 Android JNI 注册到 `net.mediaarea.mediainfo.MediaInfo`, 当前插件使用 `org.mediainfo.android.MediaInfo`.
- 官方 JNI 使用 `Init` / `Open` 或 `OpenFd` / `Inform` / `Get` / `Close` 生命周期, 当前 Kotlin 类封装的是旧仓库提供的一次性路径查询方法.
- v1.1.0 已把 30 秒超时接到 JNI 缓冲循环的 `getIsCanceled()` 轮询; 官方 `OpenFd` 实现没有当前插件所需的同等取消回调.

v2 的桥接层应复用官方 MediaInfoLib API, 但继续履行现有 Kotlin / AIDL 契约. 它至少需要保留 regular FD 直读, 非 seekable 输入回退, 大文件安全, 超时取消, 字段查询, `Inform`, `Count_Get` 与 `Option("Info_Version")`; 不能通过简单替换 `.so` 牺牲 v1.1.0 已验证的行为.

******

## 上游更新流程

******

1. 每周检查一次 MediaInfoLib 的非 draft, 非 prerelease GitHub Release; 手动触发同样可用.
2. 新稳定版高于 `upstream.lock` 时, 自动创建或刷新一个更新 PR, 只改变官方固定引用, 来源清单与必要的许可归档.
3. PR 构建四种 ABI, 验证标签到提交的映射, 运行 ELF 架构 / 依赖 / 16 KB 对齐检查, 并在 APK 内核对 `Info_Version`.
4. 运行 JVM, 模拟器和 ARM64 实体机测试; 对同一批真实样本保存 v1.1.0 与候选版本的字段 / 报告差异, 将合理的上游解析变化作为可审阅产物.
5. 对 regular FD, pipe 回退, 缓存命中, 30 秒超时 / 取消和大文件分别回归; 19.37 GiB MP4 与 77.97 GiB MKV 只在获得 `--allow-large-transfer` 明确授权时执行, 测试后立即删除设备副本.
6. 更新 PR 永不自动合并, 永不自动创建 Release. 维护者确认兼容性与许可后再决定插件版本和发布日期.

这套流程追求“及时发现和可审阅地更新”, 而不是“最快把上游 master 打进发布包”. 后者会破坏可复现性, 也会把解析结果变化直接传给宿主用户.

******

## v2.0.0 发布门禁

******

- 从不含本地缓存的干净检出可构建五个 APK, 二次构建的来源清单和输入提交一致.
- 四个 `libmediainfo.so` 的 ELF 架构正确, 无缺失依赖, LOAD segment 对齐至少为 `0x4000`, 并可在 4 KB / 16 KB 页设备加载.
- API 24 最低版本, 当前目标 API, 四种 ABI 模拟器 / 设备的核心 AIDL 往返测试通过.
- `Info_Version` 返回 MediaInfoLib 26.05, 且与锁文件, APK 元数据和 Release 说明一致.
- v1.1.0 的免拷贝路径, 缓存, 30 秒超时 / 取消与清理保证全部保留.
- 合成样本, `T:\media-samples-for-autojs6-plugin-mediainfo` 真实样本, 多流样本和显式授权的大文件回归通过; 解析差异经过人工审阅.
- MediaInfoLib, ZenLib 及本地桥的许可证, 版权声明和修改说明随源码与发布包完整保留.

只有以上门禁全部满足, 才删除 v2 构建图中的旧预编译 `app/src/main/jniLibs/<abi>/libmediainfo.so`, 提升插件版本至 v2.0.0 并创建 Release. 在此之前, master 上的 v1.1.0 产物仍是可发布基线.
