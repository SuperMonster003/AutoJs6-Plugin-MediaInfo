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

GitHub 上的 [`Freeze v1.1.0 release tag`](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/rules/21913972) 规则精确匹配 `refs/tags/v1.1.0`, 无 bypass actor, 并阻止更新或删除该标签. 仓库级 Release immutability 也已于 2026-08-31 启用, 但 [GitHub 明确说明它只对启用后的未来 Release 生效](https://docs.github.com/en/code-security/how-tos/secure-your-supply-chain/establish-provenance-and-integrity/prevent-release-changes), 因而先前发布的 v1.0.0 / v1.1.0 API 状态仍为 `immutable=false`; 不通过删除, 转草稿或重发旧 Release 来改写这一历史事实. v1.1.0 的五个资产继续由已记录 SHA-256, 本地备份与维护约定共同冻结.

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

## 四 ABI 集成构建与运行验证

******

2026-08-31 先完成了与插件隔离的 ARM64 源码构建证明, 随后已把同一规则正式接入 Gradle / CMake. 从干净构建图一次生成四种 ABI 与五个 debug APK, 旧 `app/src/main/jniLibs` 二进制仅在 v2 功能分支中删除, v1.1.0 标签与 Release 资产未发生任何变化.

| ABI | 剥离后字节数 | SHA-256 |
|---|---:|---|
| `arm64-v8a` | 11,227,272 | `a689212b294f2e7f1504bd67c733584f19b2c2a04dac28f9b1753fb7e5fd4ae6` |
| `armeabi-v7a` | 6,903,216 | `e09db8de0a45bf482d39b7cce9141eff32f6ae17bca54d7ffb84c1e97a4bb22e` |
| `x86` | 12,476,056 | `a0dcab8f0fee822e6938e215153886fa97cbe533c9f101a845ed4b4172c9106c` |
| `x86_64` | 11,118,472 | `431b666ec6079c6e284f5e27e9c2dc47596f2385c0c840a15bac7a49df39b2e4` |

- 四个 ELF 的全部 LOAD segment 均为 `0x4000` 对齐, SONAME 均为 `libmediainfo.so`, 动态依赖严格只有 Android 系统 `libz.so`, `libm.so`, `libdl.so`, `libc.so`; ZenLib 与 C++ 运行时均静态并入.
- 本地 version script 将动态导出面限制为唯一的 `JNI_OnLoad`; Kotlin 需要的旧类名和方法签名在 `JNI_OnLoad` 中显式注册, 不暴露上游 C API.
- 五个 APK 均通过逐项审计: 四个 split APK 只含目标 ABI, `universal` 含全部四种 ABI; APK 内库哈希与已审计的剥离产物一致, 来源锁和两份许可也逐字核对.
- 核心 JNI / AIDL 测试已在 API 24 x86_64 (4 KB), API 29 x86, API 36 x86_64 与 API 37 x86_64 (16 KB) 通过. API 24 的停滞管道于 30.128 秒超时并验证无临时文件泄漏; API 37 实际页大小为 16384.
- QV710AF65F (API 31) 上的 v2 ARM64 与 ARM32 候选 APK 核心回归均为 4/4 通过; ARM64 停滞管道于 30.094 秒超时且无临时文件泄漏. 同一 ARM64 候选还通过 MP4, WebM, FLAC 与 561 MiB 问题样本的 regular FD、冷 / 热缓存和清理验证, 四份样本均为 `failureCount=0`.
- QV770340J7 (API 33) 上的 v2 ARM64 候选使用显式 `--allow-large-transfer` 授权通过 19.37 GiB MP4 与 77.97 GiB MKV 回归. 两份样本的冷解析分别为 303.451 ms 与 548.698 ms, 缓存命中分别为 0.254 ms 与 0.406 ms, `inform`, `get` 与 `snapshot` 的冷 / 热结果一致. 每次测试后均独立确认设备副本和测试包已删除, 可用空间恢复至约 130 GiB; 最后以设备原 APK 的 SHA-256 逐字校验恢复 v1.1.0.
- 至此 v2 的四 ABI、4 KB / 16 KB 页、最低 / 当前 API、ARM 实机、真实媒体、超时及超大文件运行门禁均已通过. 合并或发布前仍需审阅 v1.1.0 与 v2 候选的多流解析差异, 再决定 v2.0.0 版本提升与 Release 资产.

******

## 集成结构

******

v2 实现采用以下职责分离:

```text
native/
|-- upstream/
|   |-- MediaInfoLib/  # 固定提交的官方源码
|   `-- ZenLib/        # 固定提交的官方源码
|-- bridge/            # 本项目维护的最小 JNI 兼容与取消桥
|-- CMakeLists.txt     # 固定 feature profile 与 Android 链接规则
`-- upstream.lock.json # 仓库, 标签, 提交, 许可, 工具链与编译选项
```

- 上游目录使用固定提交的 Git 子模块; 两个上游目录保持同级, 以符合官方 CMake 的相对路径布局. 干净检出必须使用 `--recurse-submodules` 或随后执行 `git submodule update --init --recursive`.
- 本项目的补丁只放在 `native/bridge` 或显式补丁文件中. 不在上游子模块工作树内直接提交混合修改, 这样每次升级都能清楚审阅本地差异.
- APK 中的 `libmediainfo.so` 由 Gradle / CMake 构建图生成, 不再从个人仓库复制预编译文件.
- CMake 入口固定 `CMP0077=NEW`, 让 ZenLib 静态并入 MediaInfoLib; CI 拒绝 `DT_NEEDED` 中出现非 Android 系统库, 防止漏打包的共享依赖.
- 来源清单以 `assets/mediainfo-upstream.lock.json` 进入每个 APK, 使运行时诊断, Release 说明和可复现构建使用同一来源; MediaInfoLib / ZenLib 许可原文也随 APK 打包.

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

1. `.github/workflows/update-mediainfo-upstream.yml` 每周一检查 MediaInfoLib 与 ZenLib 的非 draft, 非 prerelease GitHub Release; `workflow_dispatch` 手动触发同样可用.
2. 新稳定版高于 `native/upstream.lock.json` 时, 自动创建或刷新 `automation/update-mediainfo-upstream` Draft PR, 只改变官方固定引用, 来源清单与许可归档. 相同版本号解析到不同提交时直接作为安全错误停止, 不静默接受标签移动.
3. 更新分支推送后显式 `workflow_dispatch` 完整 Build integrity 流程, 构建四种 ABI, 验证标签到提交的映射, 运行 ELF 架构 / 依赖 / 16 KB 对齐检查, 并在 APK 内核对 `Info_Version`; 即使机器人 PR 的普通事件等待批准, 也会立即产生候选提交的 CI 证据.
4. 运行 JVM, 模拟器和 ARM64 实体机测试; 对同一批真实样本保存 v1.1.0 与候选版本的字段 / 报告差异, 将合理的上游解析变化作为可审阅产物.
5. 对 regular FD, pipe 回退, 缓存命中, 30 秒超时 / 取消和大文件分别回归; 19.37 GiB MP4 与 77.97 GiB MKV 只在获得 `--allow-large-transfer` 明确授权时执行, 测试后立即删除设备副本.
6. Release 先以 draft 创建并一次性上传全部 APK, 校验标签, 资产名, 大小与 SHA-256 后才发布; 发布后由已启用的 GitHub Release immutability 锁定标签和资产.
7. 更新 PR 永不自动合并, 永不自动创建 Release. 维护者确认兼容性与许可后再决定插件版本和发布日期.

这套流程追求“及时发现和可审阅地更新”, 而不是“最快把上游 master 打进发布包”. 后者会破坏可复现性, 也会把解析结果变化直接传给宿主用户.

******

## v2.0.0 发布门禁

******

- [x] 从固定子模块和不含个人预编译库的构建图生成五个 APK; 锁文件完整记录输入提交与工具链.
- [x] 四个 `libmediainfo.so` 的 ELF 架构, SONAME, 依赖, 导出面与 LOAD segment `0x4000` 对齐通过自动校验, 并在 4 KB / 16 KB 页模拟器加载.
- [x] API 24 最低版本和当前目标 API 已通过; x86 / x86_64、ARM64 与 ARM32 候选 APK 均已运行核心 AIDL 回归, ARM64 另通过 30 秒超时 / 取消回归.
- [x] `Info_Version` 返回 MediaInfoLib 26.05, 且与锁文件和 APK 元数据一致; Release 说明在 v2 发布时补齐.
- [x] v1.1.0 的免拷贝路径, 缓存, 30 秒超时 / 取消与清理保证已在 JVM 与模拟器回归中保留.
- [x] 合成样本、ARM64 实机上的四份非超大真实样本及显式授权的 19.37 GiB MP4 / 77.97 GiB MKV 候选版本回归均已通过; 设备副本和测试包已删除并复核.
- [ ] 对同一批多流样本审阅 v1.1.0 与 v2 候选的字段、报告与 sections 差异, 将合理的上游解析变化记录为可审阅证据.
- [x] MediaInfoLib, ZenLib 及本地桥的许可证, 版权声明和来源锁随源码与 APK 保留.
- [ ] v2.0.0 版本号, 最终发布说明与五个 release APK 尚未生成; draft Release 只会在全部运行门禁通过后创建.

v2 功能分支已经从其构建图移除旧预编译库, 这是验证官方源码为唯一输入所必需的可审阅改动; 在上述门禁全部满足并人工合并前, `master` 仍保持 v1.1.0 可发布基线. ARM 实机和超大文件回归已经通过, 但仍需完成解析差异审阅并取得人工发布决定, 才会提升插件版本至 v2.0.0 并创建 Release.
