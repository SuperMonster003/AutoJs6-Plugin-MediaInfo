# AutoJs6-Plugin-MediaInfo 开发路线图 (Roadmap)

更新日期: 2026-09-01

本文档以可勾选清单维护 MediaInfo 插件的能力现状与演进规划, 按里程碑组织.
未勾选条目表示规划意向而非当前版本能力; 欢迎通过 [Issues](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues) 参与讨论或认领条目.

******

## 状态与证据规则

******

- `[x]` 已完成: 仓库中存在可验证证据 (代码 / 产物 / CI), 条目附 `(落点: ...)` 指向证据位置.
- `[ ]` 规划中: 仅表达意向; 勾选前必须补充落点并通过对应里程碑的验收条件自查.
- `(协同项)` 标记: 需要宿主 AutoJs6 或插件 API (`libs/mediainfo-api.aar` / `libs/common-plugin-api.aar`) 同步变更, 无法仅在本仓库内独立完成.
- 里程碑编号表示建议优先级, 不构成时间承诺; 同一里程碑内条目可独立实施与合并.

******

## 总览

******

| 里程碑 | 主题 | 状态 |
|---|---|---|
| M0 | 基线能力 (v1.0.0) | 已完成 |
| M1 | 文档与工程化 | 进行中 |
| M2 | 读取能力增强 | 进行中 |
| M3 | 性能与大文件 | 已完成 |
| M4 | 健壮性与诊断 | 进行中 |
| M5 | 原生库演进 (v2.0.0+) | 进行中 |

******

## M0 基线能力 (v1.0.0) - 已完成

******

- [x] AIDL 插件服务: `IMediainfoPlugin` 暴露 `getInfo` / `inform` / `get` / `snapshot` 四个方法, 媒体内容以只读 `ParcelFileDescriptor` 加显示名传参. (落点: `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediainfoPluginService.kt`, `libs/mediainfo-api.aar`)
- [x] 插件自动发现与唤醒: 通过 `org.autojs.plugin.MEDIAINFO` action 与 `mediainfo` category 被宿主发现, `WakeActivity` 支持宿主唤醒; 服务与唤醒入口均受 `org.autojs.permission.PLUGIN` 权限保护. (落点: `app/src/main/AndroidManifest.xml`, `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/WakeActivity.kt`)
- [x] 能力协商: `PluginInfo` 携带 `REQUIRES_HOST_VERSION = 3923`, 宿主版本过低时明确拒绝而非静默失败. (落点: `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/PluginRuntimeInfo.kt`)
- [x] MediaInfoLib JNI 封装: 基于 MediaInfoLib-android 的 `MediaInfo` 类封装原生解析能力, 库加载失败时以 `MediaInfo library is not available` 显式报错. (落点: `app/src/main/java/org/mediainfo/android/MediaInfo.kt`, `app/src/main/jniLibs/<abi>/libmediainfo.so`)
- [x] 结构化快照: `snapshot` 输出 schema `autojs6-plugin-mediainfo-snapshot-v1`, 支持 `includeInform` / `includeSections` 选项, sections 字段名转换为 camelCase. (落点: `MediainfoPluginService.kt`)
- [x] 数据安全基线: 仅接收宿主传入的只读文件描述符, 媒体内容临时写入插件私有缓存目录, 解析完成立即删除; 清单不含网络与任何敏感系统权限. (落点: `MediainfoPluginService.kt`, `app/src/main/AndroidManifest.xml`)
- [x] 五种安装包: ABI 拆分产出 `arm64-v8a` / `armeabi-v7a` / `x86` / `x86_64` 四种单架构包与 `universal` 包; `appendDigestToReleasedFiles` 任务生成 `autojs6-plugin-mediainfo-v<版本>-<abi>-<crc32>.apk` 命名的发布文件. (落点: `app/build.gradle.kts`)
- [x] 10 语言资源: 插件描述与错误信息 (`strings.xml`), 使用说明 (`plugin_instruction.md`), 更新日志 (`CHANGELOG-*.md`) 覆盖 zh-Hans / zh-Hant-HK / zh-Hant-TW / en / fr / es / ja / ko / ru / ar. (落点: `app/src/main/res/values-*/strings.xml`, `app/src/main/res/raw-*/plugin_instruction.md`, `app/src/main/assets/doc/`)

验收条件 (已满足): v1.0.0 (2026/07/15) 已发布; 宿主脚本 (Node `require("mediainfo")` 与 Rhino 全局 `mediainfo`) 及文件列表媒体信息对话框可正常工作.

******

## M1 文档与工程化 - 进行中

******

- [x] README 重写: 以 v1.0.0 实际能力为准重写全部 10 语言 README, 覆盖功能亮点, 安装包选择, 双引擎 API 示例, 快照结构, 常见问题与安全说明. (落点: `README.md`, `.readme/`, 2026-08-31)
- [x] CHANGELOG 重写: v1.0.0 更新日志改为面向用户的能力描述, 10 语言同步. (落点: `.changelog/`, `app/src/main/assets/doc/CHANGELOG*.md`)
- [x] 使用说明纳入生成链路: `plugin_instruction.md` (11 份 res/raw 产物) 由 JSON 源与模板生成, 不再手工维护. (落点: `.readme/template_plugin_instruction.md`, `.python/generate_markdown.py`)
- [x] 生成脚本升级至同族最新实现: 支持 `--check` 漂移校验, 跨语言键位与列表形状对齐, 全角符号拦截, 版本对齐 (最新日志条目 == `version.properties`), `strings.xml` 描述同步校验. (落点: `.python/generate_markdown.py`)
- [x] Markdown CI: GitHub Actions 在推送与 PR 时运行 `--check`, 防止源文件与产物脱节. (落点: `.github/workflows/markdown.yml`, `.python/check_markdown.bat`)
- [x] ROADMAP 建立: 即本文档, 以可勾选清单维护能力规划. (落点: `ROADMAP.md`)
- [ ] 演示物料: README 增补脚本调用效果与 AutoJs6 媒体信息对话框截图 (深浅色各一组), 图片纳入仓库或 Release 资源.
- [x] 文档整改后的首个发布: v1.1.0 (2026/08/31) 发布说明与新版 CHANGELOG 保持一致, 并核对 Releases 页面五种 ABI 资产命名. (落点: `.changelog/`, `app/build.gradle.kts`, [v1.1.0 Release](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases/tag/v1.1.0))

验收条件: `py .python/generate_markdown.py --check` 输出 `MARKDOWN_OK languages=10 artifacts=36`; markdown CI 全绿; README 在 GitHub 明暗两种主题下渲染正常.

******

## M2 读取能力增强 - 进行中

******

说明: JNI 封装 `MediaInfo.kt` 已具备 `streamNum`, `InfoKind`, `countGet`, `getMIOption` 等完整能力; AIDL 与插件服务已可接收 `streamNumber`, 但当前宿主 Node / Rhino API 仍固定查询同类流第 1 条, InfoKind 也固定为参数值文本. 本里程碑以 "接线已有能力" 为主, 原生层无需改动.

当前进度: 插件侧已建立显式 opt-in 的 snapshot-v2 契约与 capability 广告, 缺省及空白 schema 仍返回 v1, 未知 schema 显式拒绝; v2 以 MediaInfoLib 原生 JSON 为数据源, 将同类流按数组分组并隔离动态字段, 属性与诊断扩展. 宿主与共享 API 尚未接入, 因此对应协同项保持未勾选. (落点: `MEDIAINFO_SNAPSHOT_V2.md`, `MediainfoSnapshot.kt`, `PluginRuntimeInfo.kt`, `MediainfoPluginService.kt`)

- [ ] `get` 支持流序号 (协同项): AIDL 层以选项或新方法携带 `streamNumber`, Node / Rhino API 同步透出, 使脚本可查询第 2 条及之后的音轨 / 字幕. (落点: `MediaInfo.kt` 的 `get(filename, streamKind, streamNum, parameter)`, `MediainfoPluginService.kt`)
- [ ] 流计数查询 (协同项): 透出 `countGet`, 返回某流类型的流数量, 配合流序号实现多流遍历. (落点: `MediaInfo.kt` 的 `countGet`)
- [ ] InfoKind 扩展查询 (协同项): 支持 `MEASURE` / `INFO` / `NAME_TEXT` 等信息种类, 获取参数单位, 说明与本地化名称. (落点: `MediaInfo.kt` 的 `InfoKind` 枚举)
- [ ] 引擎信息透出: 经 `getMIOption` 提供 `Info_Version` / `Info_Parameters` 等库级信息, 便于脚本诊断与参数发现. 插件发现信息已通过 capability 提供小体积的 `Info_Version`; 大体积 `Info_Parameters` 与宿主脚本入口仍待专用按需 API. (落点: `MediaInfo.kt` 的 `getMIOption`, `PluginRuntimeInfo.kt`, `MEDIAINFO_SNAPSHOT_V2.md`)
- [ ] 快照 schema v2 (协同项): 规范化多流 sections 表示 (以数组序号取代 `audio #1` 式小节名), 明确字段命名规则与 schema 版本协商方式, 保持对 v1 消费方的兼容期. 插件侧契约, opt-in 实现, 缓存隔离与测试已落地; 共享 API 常量, 宿主双引擎入口及跨仓库验收待完成. (落点: `MEDIAINFO_SNAPSHOT_V2.md`, `MediainfoSnapshot.kt`, `MediainfoPluginService.kt`, `MediaInfoSnapshotV2ContractTest.kt`)

验收条件: 双引擎示例脚本可读取多音轨样本的第 2 条流及其单位信息; `.readme` / `plugin_instruction` 文档同步更新; 涉及 AIDL 变更的条目与宿主版本要求 (`REQUIRES_HOST_VERSION`) 联动更新.

******

## M3 性能与大文件 - 已完成

******

发布状态: 本节实现已纳入 v1.1.0 (2026/08/31) CHANGELOG 与 Release.

- [x] 免整文件拷贝解析: 对可随机访问的常规文件描述符通过 `/proc/self/fd/<fd>` 交由 MediaInfoLib 直读, 以实际报告校验直读是否成功; 管道等非 regular 描述符, proc 路径不可读或直读解析失败时才回退私有缓存副本, 并保证描述符与临时文件清理. `get` 仅在结果为空时追加报告探测, 避免常见查询重复解析. (落点: `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediaInputAccess.kt`, `MediainfoPluginService.kt`, `app/src/androidTest/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediainfoPluginServiceTest.kt`)
- [x] 解析结果缓存: 对 API 27+ 可稳定识别的 regular FD, 以设备号, inode, 大小, 纳秒级 mtime / ctime 与显示名组成文件身份, 在插件进程内缓存完整报告, 字段查询 (含合法空结果) 与快照; API 24-26 因缺少纳秒级 stat 时间戳而保守禁用. 采用 access-order LRU 和 10 分钟滑动 TTL, 上限为 32 个文件, 每文件 64 个查询及约 2 MiB 文本, 文件元数据变化自动换键, 低内存或服务销毁时清空; 管道与私有副本不缓存. JVM 测试覆盖命中, 失效, LRU, TTL 与容量预算, API 31 ARM64 真实样本验证冷 / 热结果完全一致. (落点: `MediaInfoResultCache.kt`, `MediaInputAccess.kt`, `MediainfoPluginService.kt`, `MediaInfoResultCacheTest.kt`, `MediainfoRealMediaValidationTest.kt`)
- [x] 取消与超时: `MediaInfo.cancel()` 以 volatile 标志接通 JNI 缓冲循环的 `getIsCanceled()` 轮询, 服务为每次 AIDL 调用设置 30 秒硬上限并跟踪活动解析器 / 输入流; 回退复制以 100 ms `poll` 间隔检查取消状态, 避免旧 Android 上跨线程关闭管道仍不能唤醒阻塞读取. 超时或服务销毁时协作取消原生解析, 停止复制, 删除私有临时文件并以稳定前缀 `MEDIAINFO_TIMEOUT` 报错. JVM 并发测试验证 JNI 轮询标志置位与资源关闭, 停滞管道端到端测试在 API 24 / 36 x86_64 与 API 31 ARM64 上均约 30 秒结束且无临时文件泄漏. (落点: `MediaInfo.kt`, `MediaInfoCallGuard.kt`, `MediainfoPluginService.kt`, `MediaInfoCallGuardTest.kt`, `MediainfoTimeoutTest.kt`)
- [x] 耗时基准: 建立显式设备串号驱动的端到端基准, 以稀疏 PCM/WAVE 与 RGB/BMP 样本分别测量 regular FD 冷解析, 进程内缓存命中和 pipe 私有副本回退; full profile 对直读 / 缓存覆盖 1/64/256/1024 MiB, 对回退覆盖 1/16/64/256 MiB, 每项保留原始纳秒样本与中位数. API 36 x86_64, API 29 x86 与 API 31 ARM64 的 3 份串行完整结果使用相同输入哈希, 样本放大 1024 倍时直读延迟仅增长 1.11-3.84 倍, 1 GiB 缓存命中为 0.088-0.239 ms. ARM64 实体机除验证 MP4 / WebM / FLAC 与 561 MiB 问题样本外, 还以显式大文件授权验证 19.37 GiB MP4 与 77.97 GiB MKV 的冷 / 热结果一致, 冷解析分别为 137.417 ms 与 177.211 ms. (落点: `.python/run_mediainfo_benchmark.py`, `.python/run_real_media_validation.py`, `app/src/androidTest/java/io/github/supermonster003/autojs6/plugin/mediainfo/`, `benchmark/`)

验收进度:

- [x] 大文件 (1 GiB 合成 WAVE/BMP, 19.37 GiB MP4 与 77.97 GiB MKV) 直读耗时不随文件大小线性增长, 实体机测试副本均已清理.
- [x] 取消后临时资源即时释放; 停滞管道在 x86_64 模拟器与 ARM64 实体机均于超时点结束, 测试前后私有临时文件集合一致.
- [x] 基准数据可复现, 且 API 36 x86_64, API 29 x86, API 31 ARM64 与 ARM64 真实媒体原始结果已入库.

******

## M4 健壮性与诊断 - 进行中

******

发布状态: 本节于 2026-08-31 勾选的实现已纳入 v1.1.0 CHANGELOG 与 Release; 未勾选条目继续保留在后续规划.

- [x] 动态 ABI 上报: 扫描已安装 base / split APK 中实际包含的 `libmediainfo.so`, 单架构包仅上报对应 ABI, `universal` 包上报全部 4 种; APK 不可读时按进程位数与已解压原生库回退. (落点: `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/NativeLibraryInventory.kt`, `PluginRuntimeInfo.kt`, `app/src/test/java/io/github/supermonster003/autojs6/plugin/mediainfo/NativeLibraryInventoryTest.kt`)
- [ ] 结构化错误码 (协同项): 库不可用, 文件不可读, 解析失败等场景以稳定错误码返回, 宿主与脚本可编程区分处理, 而非仅依赖异常消息文本.
- [x] 单元测试: 将 sections / camelCase / 快照选项解析抽为纯 Kotlin, 覆盖多流编号小节, 重复小节, 畸形字段, 值内冒号, 重复字段与默认 / 独立选项. (落点: `app/src/main/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediainfoSnapshot.kt`, `app/src/test/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediaInfoReportParserTest.kt`)
- [x] 服务级测试: androidTest 生成最小 PCM/WAVE 样本, 验证 action / category 发现, 服务绑定, 动态 ABI 信息与 `getInfo` / `inform` / `get` / `snapshot` 四个 AIDL 方法的真实往返; 另以显式开关覆盖缓存命中, 非 seekable 回退, 30 秒停滞管道超时及真实媒体矩阵. (落点: `app/src/androidTest/java/io/github/supermonster003/autojs6/plugin/mediainfo/`)
- [x] 构建 CI: GitHub Actions 在推送与 PR 时以 JDK 21 运行单元测试, 构建并审计 debug / minified release 的四 ABI 与 universal APK; Android 35 x86_64 模拟器先安装实际 release 变体运行公开 AIDL / JNI 冒烟测试, 再运行完整 `connectedDebugAndroidTest`. (落点: `.github/workflows/build.yml`, `MediainfoReleaseSmokeTest.kt`)

验收条件: CI 全绿; 主要错误路径均有测试覆盖; 单架构包在插件中心显示的 ABI 与实际内容一致.

******

## M5 原生库演进 (v2.0.0+) - 进行中

******

版本边界与上游策略详见 [MEDIAINFO_UPSTREAM.md](MEDIAINFO_UPSTREAM.md). v1.1.0 是旧原生引擎的最终冻结点; 从 v2.0.0 起改为直接由 MediaArea 官方源码构建, 插件版本与 MediaInfoLib 上游版本分别管理.

- [x] v1.1.0 冻结边界: `v1.1.0` 标签指向已发布源码, 五个 APK 资产与其中的旧版 `libmediainfo.so` 不再重建, 替换或回溯更新; 精确标签规则阻止更新与删除, 后续原生引擎变更仅进入 v2.0.0 及以上版本. (落点: [v1.1.0 Release](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases/tag/v1.1.0), [Freeze v1.1.0 release tag](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/rules/21913972), `MEDIAINFO_UPSTREAM.md`)
- [x] 官方源码可复现构建: 以 Git 子模块的固定标签和提交引入 MediaArea 官方 `MediaInfoLib` 与 `ZenLib`, 首个 v2 基线固定为 MediaInfoLib `v26.05` (`f23e69ce89581343f3b7a42e06828a5331e290d5`) 和 ZenLib `v0.4.41` (`894980d3ecbc843d6ac685493f8f2ed5c2b6864c`); 使用固定 NDK `29.0.14206865` / CMake `3.22.1` 从源码构建全部 4 种 ABI, 构建图中不再使用旧预编译库. (落点: `.gitmodules`, `native/upstream/`, `native/CMakeLists.txt`, `app/build.gradle.kts`)
- [x] JNI 兼容桥: 在官方 MediaInfoLib 之上维护最小本地桥接层, 保持 `org.mediainfo.android.MediaInfo` 的现有 Kotlin 调用面, regular FD / 回退副本路径及 `getIsCanceled()` 协作取消语义; 上游子模块保持未修改. 导出面由 version script 收敛为唯一的 `JNI_OnLoad`. (落点: `native/bridge/mediainfo_jni.cpp`, `native/bridge/libmediainfo.map.txt`, `MediainfoPluginServiceTest.kt`)
- [x] MediaInfoLib 版本透出与来源清单: `native/upstream.lock.json` 记录上游仓库, 标签, 完整提交, 许可, 工具链和编译选项并原样打入全部 APK; 运行时 `Info_Version` 与锁定标签交叉验证, APK 同时携带 MediaInfoLib / ZenLib 许可原文. (落点: `native/upstream.lock.json`, `GenerateMediaInfoMetadataTask`, `scripts/verify_native_build.py`, `MediainfoPluginServiceTest.kt`)
- [x] 上游稳定版跟踪: 每周一及手动触发时查询 MediaInfoLib / ZenLib 的最新非 draft, 非 prerelease Release; 只有版本递增才更新固定标签, 完整提交, 许可与来源清单并创建或刷新 Draft PR. 同名标签移动会作为安全错误失败, PR 永不自动合并或发布. (落点: `.github/workflows/update-mediainfo-upstream.yml`, `scripts/update_mediainfo_upstream.py`)
- [x] 原生结构化输出评估 (v2.1.x): 已实现仅插件内部可见的 `Output=JSON` JNI 路径, 对进程级输出选项执行加锁, 保存与异常安全恢复, 并在 API 31 ARM64 上用 MP4 / WebM / FLAC / 损坏 MP4 及并发文本调用验证. 结论是原生 JSON 可作为未来 `snapshot-v2` 的数据源, 但其机器字段名, 原始值, nested `extra` 与动态字段集合不能透明替换 `autojs6-plugin-mediainfo-snapshot-v1`; v1 继续使用文本解析, AIDL 与公开结构不变. (落点: `MEDIAINFO_NATIVE_JSON.md`, `native/bridge/mediainfo_jni.cpp`, `MediainfoPluginServiceTest.kt`, `benchmark/results/2026-09-01-api31-arm64-v8a-native-json-evaluation.json`)
- [x] 16 KB page size 适配: 以 NDK r29 工具链和 `-z,max-page-size=16384` 生成四 ABI 的 16 KB 对齐 ELF; CI 校验每个 LOAD segment, 架构, `DT_NEEDED`, 导出符号和五个 APK 的原生内容, API 37 x86_64 16 KB 页模拟器已通过 JNI / AIDL 核心回归. (落点: `native/CMakeLists.txt`, `scripts/verify_native_build.py`, `.github/workflows/build.yml`)
- [x] 双版本解析兼容性审查: 在 API 31 ARM64 实体机上对 0.7.83 / 26.05 使用同一批 MP4, WebM, FLAC 与畸形 MP4, 审阅完整报告、固定字段查询和 sections 差异. 容器与核心流保持兼容; 日期 / 单位规范化、字段调整和新增元数据按上游演进接受, `IsTruncated` 明确视为非稳定诊断字段. (落点: `benchmark/results/2026-08-31-api31-arm64-v8a-v1.1.0-v2.0.0-diff.json`)
- [x] minified Release 防回归: 固定 JNI 精确类名不被 R8 改写, 以独立 androidTest 经公开 AIDL 安装并验证真实 release APK; ARM64 实体机已通过, CI 对 x86_64 release 重复该门禁. (落点: `app/proguard-rules.pro`, `app/src/androidTest/java/io/github/supermonster003/autojs6/plugin/mediainfo/MediainfoReleaseSmokeTest.kt`, `.github/workflows/build.yml`)
- [x] v2.0.0 发布候选: code 10 与十语言日志已生成; 五个生产签名 APK 的版本、签名连续性、CRC32 文件名、SHA-256、来源清单、许可和原生结构均通过审计, 最终 ARM64 / ARM32 字节在实体机复核并清理. (落点: `version.properties`, `.changelog/`, `benchmark/results/2026-08-31-v2.0.0-release.json`, `MEDIAINFO_UPSTREAM.md`)

验收条件: v1.1.0 标签与 Release 资产保持不变; v2.0.0 可从干净检出构建全部 4 种 ABI; `Info_Version` 与来源清单, 发布说明一致; 4 KB / 16 KB 页设备, API 24 最低版本及当前目标版本均可加载; 合成样本, 真实媒体, 多流, minified Release, 超时 / 取消, 缓存, 19.37 GiB MP4 与 77.97 GiB MKV 回归通过; 上游更新 PR 不绕过人工审阅. v2.0.0 已于 2026-09-01 合并并发布; v2.1.x 原生 JSON 可行性, 并发隔离和 snapshot-v1 兼容边界已于同日完成验证.

******

## 边界与非目标

******

- 不做媒体转码, 播放与缩略图生成: 插件专注于只读的媒体信息解析.
- 不提供独立用户界面: 插件无桌面图标与独立页面, 全部交互经由 AutoJs6 完成.
- 不面向第三方应用开放调用: 服务与唤醒入口受 `org.autojs.permission.PLUGIN` 权限保护, 仅服务于 AutoJs6 宿主.
- 不承诺字段值文本格式稳定: 字段值保留 MediaInfo 原始文本 (含单位与千位分隔), 结构稳定性由 snapshot schema 版本机制承诺.

******

## 维护约定

******

- 完成条目时: 将 `[ ]` 勾选为 `[x]`, 补充 `(落点: ...)` 证据, 并在对应版本的 CHANGELOG (`.changelog/lang_*.json`) 记录.
- 新增规划时: 先经 Issue 讨论达成一致, 再按里程碑归类入库.
- `(协同项)` 条目落地时: 与宿主仓库 [AutoJs6](https://github.com/SuperMonster003/AutoJs6) 及插件 API 版本对齐, 必要时同步提升 `REQUIRES_HOST_VERSION`.
- 文档改动一律修改 `.readme/` 与 `.changelog/` 下的 JSON 源并运行 `py .python/generate_markdown.py` 重新生成, 生成产物不手工编辑.
