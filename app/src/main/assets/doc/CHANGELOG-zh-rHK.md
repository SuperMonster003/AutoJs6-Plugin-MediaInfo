******

### 發行歷史

******

# v1.0.0

###### 2026/07/15

* `新增` MediaInfo 插件服務, 插件 ID 為 `mediainfo`, 引擎為 `mediainfo`
* `新增` 支援通過 `org.autojs.plugin.MEDIAINFO` 發現並調用插件
* `新增` 支援 `inform`/`get`/`snapshot` 能力, 可生成完整媒體報告, 查詢單個參數, 輸出結構化 JSON 快照
* `新增` 內置 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`, 並支援 `universal` 通用包
* `新增` 插件運行時信息上報支援的 ABI 列表, 發布 APK 文件名包含版本號/ABI 變體和 CRC32 摘要
* `新增` 插件信息, 使用說明, README 與 CHANGELOG 的多語言資源: 西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體
