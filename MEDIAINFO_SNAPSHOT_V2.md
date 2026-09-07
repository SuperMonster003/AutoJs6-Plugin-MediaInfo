# MediaInfo snapshot-v2 契约

## 状态与边界

本文档定义 `autojs6-plugin-mediainfo-snapshot-v2` 的稳定插件契约. M2 协同实现已接入共享 `mediainfo-api`, AutoJs6 宿主及 Node / Rhino 公共 API, 但不会替换 v1: 普通脚本仍默认得到 v1, 只有显式选择并通过能力协商后才得到 v2.

设计遵循三个边界:

1. v1 是缺省协议, 现有调用无需修改且输出语义不变.
2. v2 必须由调用方显式选择, 不根据插件版本或 MediaInfoLib 版本自动切换.
3. MediaInfoLib 原生 JSON 只是数据源. 插件拥有 v2 外层契约, 不把上游根对象直接作为公共快照返回.

## Schema 协商

现有 AIDL 已允许 `snapshot(fd, displayName, Bundle options)`, 因此插件侧无需修改 Binder 方法签名. `options` 使用以下值:

| Bundle 键 | 类型 | 值 | 行为 |
| --- | --- | --- | --- |
| `schema` | `String` | 缺失, `null`, 空白或 `autojs6-plugin-mediainfo-snapshot-v1` | 返回 v1 |
| `schema` | `String` | `autojs6-plugin-mediainfo-snapshot-v2` | 返回 v2 |
| `schema` | `String` | 其他值 (包括在标识两侧增加空白) | 以 `IllegalArgumentException` 拒绝, 不猜测或修正版本 |
| `includeInform` | `Boolean` | 缺省 `true` | 决定 `inform` 是否携带文本报告 |
| `includeSections` | `Boolean` | 缺省 `true` | v1 决定 `sections`; v2 为保持现有选项 ABI, 决定 `tracks` |

插件通过 `PluginInfo.capabilities` 广告能力:

| capability 键 | 类型 | 当前值 |
| --- | --- | --- |
| `snapshotSchemas` | `String[]` | v1, v2, 按优先兼容顺序排列 |
| `defaultSnapshotSchema` | `String` | `autojs6-plugin-mediainfo-snapshot-v1` |
| `engineVersion` | `String` | MediaInfoLib `Info_Version` 去除首尾空白后的非空结果; 查询失败时省略 |

共享 API 已通过 `MediainfoOptionKeys`, `MediainfoPluginCapabilityKeys` 与 `MediainfoSnapshotSchemas` 定义这些键名, schema 标识, 缺省策略及稳定的不支持错误前缀; 插件随仓库内 `libs/mediainfo-api.aar` 使用同一份定义. 这些附加 capability 不改变现有 `REQUIRES_HOST_VERSION = 3923`, 因为旧宿主会忽略未知 Bundle 键, 且缺省 snapshot 仍是 v1.

`Info_Parameters` 不放入发现阶段的 capability Bundle. 该值体积大, 应在后续通过专用 AIDL 查询按需取得, 避免每次插件发现都跨 Binder 传输完整参数表.

## v2 Envelope

示例:

```json
{
  "schema": "autojs6-plugin-mediainfo-snapshot-v2",
  "file": {
    "name": "movie.mkv",
    "sizeBytes": 4096
  },
  "engine": {
    "name": "MediaInfoLib",
    "version": "26.05",
    "url": "https://mediaarea.net/MediaInfo"
  },
  "inform": "",
  "tracks": {
    "general": [
      {
        "fields": {
          "Format": "Matroska",
          "FileSize": 4096
        }
      }
    ],
    "audio": [
      {
        "fields": {
          "Format": "PCM",
          "SamplingRate": 48000
        },
        "attributes": {
          "@typeorder": 1
        },
        "extra": {
          "IsTruncated": "Yes"
        }
      }
    ]
  }
}
```

外层规则:

| 路径 | 契约 |
| --- | --- |
| `schema` | 始终为完整 v2 标识 |
| `file.name` | 调用方传入的显示名; 无值时为空字符串 |
| `file.sizeBytes` | 插件实际解析的数据源大小, JSON 整数 |
| `engine.name` | 原生 `creatingLibrary.name`, 必须为非空字符串 |
| `engine.version` | 原生 `creatingLibrary.version`, 必须为非空字符串 |
| `engine.url` | 原生值为非空字符串时才出现; 其他 `creatingLibrary` 字段不透传 |
| `inform` | `includeInform=true` 时为文本报告, 否则为空字符串; 键始终存在 |
| `tracks` | `includeSections=true` 时按流类型分组, 否则为空对象; 键始终存在 |

Track 规则:

- 原生 `media.track[]` 的 `@type` 使用 `Locale.US` 转为小写并作为 `tracks` 的分组键. 分组键必须匹配 `[a-z][a-z0-9_-]*`.
- 同类流保持原生数组顺序. 数组下标就是从 0 开始的 `streamNumber`; v1 的 `audio #1` / `audio #2` 显示标题不再承担寻址语义.
- `@type` 只用于分组, 不进入 track 对象.
- 其他以 `@` 开头的原生键原名放入 `attributes`; 没有属性时省略该对象.
- 原生 `extra` 必须是对象并独立放入 `extra`; 没有 `extra` 时省略. 诊断字段由上游定义, 消费方不得假定固定集合.
- 其余原生 track 成员原名放入 `fields`. 字符串, 数字, 布尔值, `null`, 数组和对象保留 JSON 类型, 不转为 v1 的显示文本或 camelCase.

只有 v2 的 envelope, 分组方式与分区规则属于插件稳定契约. `fields`, `attributes` 和 `extra` 内的具体键、值与可用性仍是 MediaInfoLib 上游扩展面; 升级原生库时必须审阅差异, 但不应把新增字段误判为 v2 envelope 破坏.

## 兼容与错误行为

- 未指定 `schema` 的旧调用继续走原有文本 Inform 解析路径, 包括 v1 的 `fileName`, `sizeBytes`, `inform` 与 `sections`.
- v1 与 v2 使用包含 schema 标识的不同缓存键, 相同文件和选项不会串用结果.
- v2 原生 JSON 为空时, regular FD 直读仍按现有策略回退到插件私有副本; 私有副本仍无法解析时返回空结果.
- 原生 JSON envelope 缺少 `creatingLibrary`, `media.track`, 必需字符串或合法 track 类型时显式失败, 不生成看似成功但结构不完整的 v2.
- 解析, FD 回退, 30 秒取消和超时边界沿用现有服务机制.

## 协同接入状态

- [x] 共享 API 通过 `MediainfoOptionKeys` / capability keys / `MediainfoSnapshotSchemas` 统一本页常量, 并将新 AAR 同步回插件.
- [x] 宿主发现插件时读取 `snapshotSchemas`; 只有 capability 明确包含 v2 才发送 v2 schema, 对旧插件保留缺省 v1 路径.
- [x] Node / Rhino API 提供显式 schema 选择与 `capabilities()`, 默认保持 v1, 不支持显式 v2 时返回稳定错误.
- [x] 双引擎类型定义, API 文档与示例已同步; v2 的 `fields`, `attributes` 与 `extra` 均保留为动态 JSON 扩展面.
- [x] 插件侧多轨, 多字幕, MP4, WebM, FLAC + cover, 损坏文件及大文件矩阵保持通过; QV710AF65F 进一步通过真实 AIDL, Rhino 生产引擎, Node 直连 / compat 门面及真实 Android Provider 的跨仓库端到端验证.
- [ ] `Info_Parameters` 继续延后. 如后续决定公开, 新增按需查询方法与响应大小测试, 不扩张发现 Bundle.

因此 Roadmap 已勾选 “快照 schema v2 (协同项)” 与小体积的 “引擎版本透出”; `Info_Parameters`, 直接 `get` 流序号, 流计数与 InfoKind 仍保持独立未完成条目.
