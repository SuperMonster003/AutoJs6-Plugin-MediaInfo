# MediaInfoLib 原生 JSON 评估

## 结论

MediaInfoLib 26.05 的 `Output=JSON` 在当前四 ABI 构建配置中可用, JSON 语法和外层结构稳定, 适合作为未来结构化接口的数据源. 它不能在不改变语义的情况下直接替换 `autojs6-plugin-mediainfo-snapshot-v1`: 原生 JSON 使用机器字段名和原始数值, 可包含嵌套对象及上游诊断字段, 而 v1 使用文本报告中的显示标签, 格式化值和插件生成的 `file` 小节.

因此 v2.1.x 的决定是:

1. 保留内部 `MediaInfo.getMIJson()` 与 JNI 安全切换能力, 作为测试和后续 schema 设计的基础.
2. `inform`, `get`, `snapshot` 三个公开行为和 AIDL 均不变; `snapshot-v1` 继续以文本报告解析结果为唯一数据源.
3. 不对原生 JSON 做静默透传, 也不在上游更新时自动改变公开快照.
4. 如后续引入 `snapshot-v2`, 必须采用新的 schema 标识和显式宿主协商, 并由插件维护稳定字段映射, 单位与类型规则.

## 官方源码审查

当前固定源码提供完整 JSON 能力:

- `Source/MediaInfo/Setup.h` 在未设置 `MEDIAINFO_EXPORT_NO` 或 `MEDIAINFO_JSON_NO` 时定义 `MEDIAINFO_JSON_YES`; 当前 Android CMake profile 未关闭该功能.
- `MediaInfo_Config::Option("Output", value)` 将值写入进程级 `Inform` 配置, 并非 `MediaInfo` 实例私有选项.
- `MediaInfo_Internal::Inform()` 为 JSON 生成 `creatingLibrary` 与 `media` 外层对象; 每条流放在 `media.track[]`, 流类型通过 `@type` 表示.
- track 字段由当前解析结果的 `Info_Name` 动态生成. 官方源码没有独立的 JSON schema 版本, `creatingLibrary.version` 只能标识生成该结果的引擎版本.

对 v24.12, v25.04, v25.10, v26.01 与 v26.05 五个稳定标签逐一检查后, `creatingLibrary` / `media` / `track` 外层节点保持一致; 下面的结论只把这一 envelope 视为观察到的兼容性, 不把动态 track 字段提升为官方承诺.

相邻稳定版的源码差异也证明字段层不能视为固定契约. 下表统计 JSON / XML 共用的 `MediaInfo_Inform.cpp`, `OutputHelpers.cpp` 及文本字段资源; 即使外层 envelope 保持一致, 生成规则, 显示资源和解析器可用字段仍随版本演进.

| 官方稳定版区间 | 涉及文件 | 增加 | 删除 | `MediaInfo_Inform.cpp` 变化 | 默认字段语言变化 |
| --- | ---: | ---: | ---: | ---: | ---: |
| v24.12..v25.04 | 8 | 86 | 12 | +59 / -8 | +2 / -0 |
| v25.04..v25.10 | 10 | 157 | 60 | +69 / -23 | +22 / -0 |
| v25.10..v26.01 | 3 | 131 | 6 | +127 / -6 | +3 / -0 |
| v26.01..v26.05 | 5 | 20 | 4 | +2 / -2 | 0 |

这些数字用于判断上游字段层的演进性质, 不表示每项差异都会影响任意给定媒体文件.

## JNI 隔离策略

`Output` 是进程级全局配置, 所以不能简单地在并行 Binder 调用中执行 `Option("Output", "JSON")` 后直接 `Inform()`. 本地桥接层采用以下顺序:

1. 获取专用互斥锁.
2. 读取并保存 `Output_Get`.
3. 临时切换为 `JSON` 或显式 `Text`.
4. 生成 `Inform()`.
5. 通过 RAII 恢复原输出格式及文本 `Complete` 状态, 包括异常路径.
6. 释放锁.

`getMediaInfoOption()` 也使用同一把锁, 防止其他本地选项访问与临时输出格式交错. JSON 解析被取消时返回空字符串, 不返回混入取消说明的无效 JSON. JNI 仍只导出 `JNI_OnLoad`, 新方法通过 `RegisterNatives` 注册, 未扩大 ELF 导出面.

## API 31 ARM64 实测

2026-09-01 在 Sony XQ-AT72 (`QV710AF65F`, Android API 31, `arm64-v8a`) 上使用 MediaInfoLib 26.05 验证三个正常样本和一个损坏样本. 每项原生 JSON 都能由 Android `JSONObject` 解析, `creatingLibrary.name` 为 `MediaInfoLib`, track 类型与有效媒体的 v1 核心小节一致.

| 样本 | 原生 track 与字段数 | snapshot-v1 小节与字段数 | 原生 JSON 冷调用 | 文本 Inform 冷调用 |
| --- | --- | --- | ---: | ---: |
| MP4 | General 21 / Video 41 / Audio 24 | file 1 / general 10 / video 29 / audio 19 | 26.820 ms | 101.987 ms |
| WebM | General 16 / Video 48 | file 1 / general 8 / video 28 | 34.629 ms | 32.541 ms |
| FLAC + cover | General 27 / Audio 17 / Image 10 | file 1 / general 24 / audio 13 / image 10 | 221.928 ms | 233.456 ms |
| 损坏 MP4 | General 11 | file 1 / general 4 | 21.784 ms | 22.009 ms |

两列时间都包含一次独立的完整文件解析, 只用于发现明显回归, 不能解释为 JSON 序列化本身比文本更快或更慢. 完整原始记录位于 [`benchmark/results/2026-09-01-api31-arm64-v8a-native-json-evaluation.json`](benchmark/results/2026-09-01-api31-arm64-v8a-native-json-evaluation.json), 仓库规范化 LF 字节的 SHA-256 为 `97ea9ebfe7f7498df46b37ff80e7336b7152a0e52aef9576bded2a7665279085`.

同机仪器测试还并发重复执行 8 次 JSON 读取与 8 次文本读取. 所有 JSON 均可解析, 所有文本报告仍以 `File` 开头并包含 `Audio`; JSON 输出模式没有泄漏到公开文本路径. 测试结束后四个 staged 样本, 插件测试包及插件开发包均已从设备删除.

## 与 snapshot-v1 的不可等价项

| 维度 | snapshot-v1 | MediaInfoLib 原生 JSON | 影响 |
| --- | --- | --- | --- |
| 文件小节 | 插件前置 `File` / `Complete name` 并解析为 `file.completeName` | 路径位于 `media.@ref`, 不存在 File track | 直接替换会删除公开字段和小节 |
| 字段名 | 从显示标签转 camelCase, 例如 `Codec ID` -> `codecId` | 机器名, 例如 `CodecID`, `Part_Position_Total` | 机械 camelCase 无法保持现有名称与别名语义 |
| 值 | 面向人的格式化文本, 包含单位与组合说明 | 常为无单位原始值, 例如字节数, 秒数, Hz | 相同键会产生不同值语义 |
| 字段集合 | 只包含文本 Inform 当前显示的字段 | 包含更多内部字段, 例如 count, header / data size | 小版本更新可增加或删除公开数据 |
| 结构 | section -> array -> string map | 字符串, 属性对象与嵌套 `extra` 可混合 | v1 的 `Map<String, String>` 模型不能无损承载 |
| 诊断字段 | 当前损坏样本只保留四个 General 显示字段 | 另含 `extra.IsTruncated=Yes` | 非稳定诊断信息会重新进入公开契约 |

## 后续 snapshot-v2 门槛

未来如推进 `snapshot-v2`, 至少应满足:

- 使用新的 AIDL/API 能力协商与 `schema` 值, 不复用 v1 名称.
- 明确区分稳定规范字段, 上游扩展字段和显示文本; 不把整个 track 对象无版本透传.
- 为时长, 大小, 码率, 采样率等定义类型与单位, 同时决定是否保留显示值.
- 统一重复流, `@typeorder`, nested `extra` 和二进制 / cover data 的表示及大小上限.
- 固定至少前一稳定版与当前稳定版的 JSON fixtures, 并在每次上游更新 Draft PR 中审阅 schema diff.
- 使用现有 MP4, WebM, FLAC, 损坏文件, 多流文件与大文件矩阵回归; 上游升级不得自动合并或发布结构变化.
