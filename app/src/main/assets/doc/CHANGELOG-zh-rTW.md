# v1.0.0

###### 2026/07/15

* `新增` MediaInfo 外掛服務, 外掛 ID 為 `mediainfo`, 引擎為 `mediainfo`
* `新增` 支援透過 `org.autojs.plugin.MEDIAINFO` 探索並呼叫外掛
* `新增` 支援 `inform`/`get`/`snapshot` 能力, 可產生完整媒體報告, 查詢單一參數, 輸出結構化 JSON 快照
* `新增` 內建 `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 的 `libmediainfo.so`, 並支援 `universal` 通用包
* `新增` 外掛執行時資訊上報支援的 ABI 清單, 發布 APK 檔名包含版本號/ABI 變體和 CRC32 摘要
* `新增` 外掛資訊, 使用說明, README 與 CHANGELOG 的多語言資源: 西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體
