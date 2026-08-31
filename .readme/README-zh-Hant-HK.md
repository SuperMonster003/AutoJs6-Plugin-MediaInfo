<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用於讀取媒體文件信息的 MediaInfo 插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### 簡介

******

MediaInfo 插件 (MediaInfo Plugin) 為 AutoJs6 提供媒體文件信息讀取能力. 安裝後, 腳本只需一行代碼即可獲取視頻, 音頻, 圖片等文件的容器格式, 編碼, 時長, 分辨率, 碼率, 聲道等上百項技術參數, AutoJs6 文件列表中的媒體信息對話框也將由本插件提供完整解析報告. 解析能力來自與桌面端 MediaInfo 同源的開源庫 MediaInfoLib.

插件運行在獨立進程中, 由 AutoJs6 自動發現, 無需任何手動配置. 讀取文件時, 宿主將媒體內容以只讀文件描述符交給插件; 對可隨機存取的常規文件描述符, 插件透過 `/proc/self/fd` 交由 MediaInfoLib 直接讀取, 遇到管道等不可隨機存取描述符或直讀解析失敗時, 才回退到私有緩存臨時副本並在調用結束立即刪除. 全程無需網絡, 也不申請任何敏感系統權限.

******

### 功能亮點

******

- 開箱即用: 安裝後無需任何配置, AutoJs6 自動發現插件, 腳本與文件列表的媒體信息對話框即可直接使用.
- 信息全面: 容器格式, 編碼, 時長, 分辨率, 幀率, 碼率, 聲道, 採樣率等技術參數一次調用全部獲取.
- 三種讀取方式: 完整文本報告 (`inform`), 單項參數查詢 (`get`), 結構化 JSON 快照 (`read`/`snapshot`), 按需選用.
- 雙引擎支援: Node 環境通過 `require("mediainfo")` 異步調用; Rhino 環境通過全局模塊 `mediainfo(path)` 同步返回可屬性存取的解析對象.
- 格式覆蓋廣: 解析能力來自與桌面端 MediaInfo 同源的 MediaInfoLib, 支援視頻, 音頻, 圖片等大量常見與小眾格式.
- 五種安裝包: 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四種單架構包與包含全部架構的 `universal` 包, 按設備按需選擇.
- 私隱友好: 插件在獨立進程中解析, 僅接收宿主傳入的只讀文件描述符, 不申請網絡與任何敏感系統權限.
- 多語言: 插件信息, 使用說明, README 與更新日誌覆蓋 10 種語言.

******

### 使用方法

******

1. 從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面下載與設備匹配的插件 APK 並安裝到運行 AutoJs6 的設備上; 拿不準選哪個時, 可直接選 `universal` 包, 或參考下方 `如何選擇安裝包`.
2. 打開 AutoJs6 的插件中心, 確認 `MediaInfo` 插件已被識別並處於啟用狀態.
3. 在腳本中按下方 `腳本 API` 的示例調用 `mediainfo` 模塊; 也可以在 AutoJs6 文件列表中打開媒體文件的媒體信息對話框直接查看完整報告.

> 若插件中心未顯示該插件, 請先將 AutoJs6 升級到較新版本 (內部版本號 3923 及以上). 插件自身支援 Android 7.0 (API 24) 及以上的設備.

******

### 如何選擇安裝包

******

每個發行版本包含 5 個 APK, 差別僅在於內置了哪些架構的原生庫:

| 安裝包 | 適用對象 |
|---|---|
| `arm64-v8a` | 絕大多數現代 Android 手機與平板 (64 位 ARM), 優先選擇 |
| `armeabi-v7a` | 較早期的 32 位 ARM 設備 |
| `x86_64` | 64 位 x86 模擬器與少數 x86 設備 |
| `x86` | 32 位 x86 模擬器與少數 x86 設備 |
| `universal` | 內置全部 4 種架構, 體積最大; 適用於任何設備, 也是拿不準架構時的穩妥選擇 |

插件在自身進程中加載原生庫. 若誤裝了與設備架構不匹配的單架構包, 調用時會提示 MediaInfo 庫不可用 (`MediaInfo library is not available`), 換裝 `universal` 包即可解決.

******

### 腳本 API

******

Node 環境 (腳本首行聲明 `"nodejs"`) 中通過 `require("mediainfo")` 獲取模塊, 全部方法返回 Promise:

```javascript
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4");
  console.log(snapshot.sections.general[0].format);
  console.log(snapshot.sections.video[0].width);

  const duration = await mediainfo.get("sample.mp4", "general", "Duration");
  console.log(duration);
})();
```

`read(path, options?)` 返回結構化快照對象 (見下方 `快照結構與選項`); `get(path, streamKind?, parameter)` 返回參數原始文本, `streamKind` 預設為 `general`. 出於安全限制, Node 腳本只能存取工程目錄內的文件, 相對路徑基於工程根目錄解析.

Rhino 環境 (AutoJs6 預設腳本引擎) 中 `mediainfo` 為全局模塊, `mediainfo(path)` 與 `mediainfo.read(path)` 等價, 同步返回解析對象:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返回對象上, `path` 與 `inform` 分別為解析後的路徑與完整文本報告; 各流類型 (如 `general`, `video`, `audio`) 既可作為屬性讀取已解析字段 (如 `mi.video.width`, 字段名為 camelCase), 也可作為函數實時查詢原始參數 (如 `mi.audio("BitRate")`). Rhino 腳本可存取宿主有權讀取的任意路徑.

******

### 快照結構與選項

******

Node 環境 `read()` 返回的快照結構如下 (插件 AIDL `snapshot` 方法返回的 JSON 與之一致, 但 `schema` 為 `autojs6-plugin-mediainfo-snapshot-v1` 且不含 `path` 字段):

```json
{
  "schema": "autojs6-node-mediainfo-snapshot-v1",
  "path": "sample.mp4",
  "fileName": "sample.mp4",
  "sizeBytes": 10485760,
  "inform": "General\nComplete name : sample.mp4\n...",
  "sections": {
    "general": [{ "format": "MPEG-4", "duration": "10 s 0 ms" }],
    "video": [{ "format": "AVC", "width": "1 920 pixels" }],
    "audio": [{ "format": "AAC LC", "channels": "2 channels" }]
  }
}
```

- `includeInform`: 是否包含 `inform` 文本報告, 預設 `true`; 置為 `false` 時 `inform` 為空字符串, 可減小返回體積.
- `includeSections`: 是否解析報告並生成 `sections`, 預設 `true`; 置為 `false` 時 `sections` 為空對象.

`sections` 以報告小節名的小寫形式作為鍵 (存在多條同類流時, 小節名可能帶編號, 如 `audio #1`), 值一律為對象數組; 字段名轉換為 camelCase, 字段值保留 MediaInfo 原始文本 (含單位與千位分隔空格, 如 `1 920 pixels`).

******

### 流類型

******

`get()` 的 `streamKind` 參數支援以下流類型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不區分大小寫, 會映射到 MediaInfo 原生流類型; 查詢不存在的流或無值參數時返回空字符串.

******

### 常見問題

******

#### 如何確認插件已經生效?

打開 AutoJs6 的插件中心, 能看到 `MediaInfo` 插件即表示宿主已識別. 隨後運行任意 `mediainfo` 腳本示例, 能正常返回結果即說明插件工作正常.

#### 為什麼應用列表裏沒有插件的圖標?

這是正常現象. 插件沒有獨立界面, 也不在桌面創建啟動圖標, 安裝後由 AutoJs6 在後台自動發現和調用, 全部交互都在 AutoJs6 內完成.

#### Node 腳本提示路徑必須位於工作目錄內 (path must stay inside the scoped working directory)?

Node 引擎出於安全限制只允許存取工程目錄內的文件. 請將媒體文件放入工程目錄內再讀取; 若需要存取其他路徑 (如相冊或下載目錄), 可改用 Rhino 引擎腳本.

#### `get()` 返回了空字符串?

參數名需使用 MediaInfo 原生參數 (如 `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), 且目標流需實際存在. 可先用 `read()` 查看 `sections` 中實際可用的字段, 或直接查看 `inform` 完整報告.

#### 讀取大文件時比較慢?

常規文件現已免整文件複製直接解析, 大文件可避開與文件大小線性相關的複製開銷. 管道等不可隨機存取描述符或直讀失敗的格式仍會使用臨時副本, 此回退路徑耗時仍與傳入位元組數相關; MediaInfoLib 本身解析耗時則取決於格式與內容.

#### 解析結果會緩存嗎, 超時如何處理?

會. 在 Android 8.1 (API 27) 及以上系統中, 對身份穩定且未變更的常規文件, 插件在目前進程內緩存報告, 查詢與快照: 最多 32 個文件, 每個文件 64 個查詢, 10 分鐘滑動有效期, 文字總量約 2 MiB. API 24-26 因無法取得納秒級文件時間戳而保守停用緩存; 低記憶體或進程結束時亦會清空. 每次 AIDL 調用上限為 30 秒; 超時會協作取消原生解析或中斷回退複製, 刪除臨時文件, 並返回包含 `MEDIAINFO_TIMEOUT` 的異常.

#### 文件有多條音軌或字幕, 如何讀取第二條及之後的流?

快照 `sections` 會完整保留報告中的全部小節 (多流時小節名帶編號, 如 `audio #2`), 可直接從中讀取; `get()` 目前固定查詢同類流中的第 1 條, 指定流序號的能力已列入 [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### 插件會聯網或申請敏感權限嗎?

不會. 插件清單不含網絡, 存儲, 相機等任何敏感系統權限, 僅聲明與 AutoJs6 通信所需的插件權限. 媒體內容由宿主以只讀描述符傳入, 解析產生的臨時副本隨即刪除.

******

### 權限與安全

******

媒體文件可能來自不可信來源, 插件在設計上為解析過程設置了多道防線:

- 進程隔離: 解析在插件自身進程中完成, 原生庫不注入宿主進程, 即使解析異常也不影響 AutoJs6 穩定運行.
- 最小數據面: 插件自身無法讀取設備存儲, 僅接收宿主打開的只讀文件描述符與文件顯示名.
- 能直讀則直讀, 回退即用即清: 可隨機存取的常規描述符不產生媒體副本; 僅兼容回退寫入私有緩存, 調用結束立即刪除.
- 最小權限: 不申請網絡, 存儲, 相機等任何敏感系統權限; 服務與喚醒入口均受 AutoJs6 插件權限 (`org.autojs.permission.PLUGIN`) 保護, 第三方應用無法直接調用.
- 開源可審計: 插件代碼, 構建腳本與文檔生成鏈路全部開源, 原生庫來源與 JNI 封裝出處在許可章節明確標註.

請僅從官方 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面或其他可信渠道獲取插件安裝包; 來源不明的安裝包即使名稱與版本號相同, 也可能被篡改.

******

### 插件接口

******

以下信息面向 AutoJs6 宿主與插件開發者, 宿主通過這些標識發現插件並完成能力協商:

```text
application id: io.github.supermonster003.autojs6.plugin.mediainfo
plugin id: mediainfo
engine: mediainfo
variant: default
discovery action: org.autojs.plugin.MEDIAINFO
discovery category: mediainfo
wake action: org.autojs.plugin.action.WAKE
binder interface: IMediainfoPlugin
minimum host build: 3923
native library: libmediainfo.so
snapshot schema: autojs6-plugin-mediainfo-snapshot-v1
```

`MediainfoPluginService` 通過 AIDL 接口 `IMediainfoPlugin` 暴露 `getInfo`/`inform`/`get`/`snapshot` 四個方法; 媒體內容以只讀 `ParcelFileDescriptor` 加顯示名傳參, `snapshot` 另接受包含 `includeInform`/`includeSections` 的 `Bundle` 選項. 服務與 `WakeActivity` 均受 `org.autojs.permission.PLUGIN` 權限保護.

插件掃描已安裝的 base / split APK 中實際包含的 `libmediainfo.so` 並動態上報 ABI; 單架構包只上報對應 ABI, `universal` 包上報全部 4 種. 若 APK 路徑無法讀取, 則按目前程序位元與已解壓的原生庫安全回退.

******

### 開發路線圖

******

插件的能力規劃與完成情況以可勾選清單維護在 ROADMAP.md 中, 按里程碑組織並附驗收條件, 涵蓋跨流查詢, 免拷貝解析, 動態 ABI 上報, 原生庫演進與持續集成等方向. 未勾選條目表示規劃意向而非目前版本能力, 歡迎通過 Issues 參與討論.

- [查看 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v1.1.0

_2026/08/31_

- `新增` 免整文件複製解析: 可隨機存取的常規文件描述符透過 `/proc/self/fd` 交由 MediaInfoLib 直讀, 管道或直讀失敗時才使用私有臨時副本
- `新增` 進程內結果緩存: API 27+ 對完整報告, 字段查詢與快照採用穩定文件身份, LRU, 10 分鐘滑動有效期及低記憶體清理, 避免重複解析
- `新增` 協作取消與超時: 每次 AIDL 調用設有 30 秒上限, 超時中止原生解析或回退複製, 釋放臨時資源並返回 `MEDIAINFO_TIMEOUT`
- `修復` 修復大文件常規描述符被不必要地整文件複製的問題, 並保證成功, 失敗, 參數校驗, 取消和超時路徑均關閉描述符與回退文件
- `修復` 修復緩存對快速文件變更的識別精度: 使用納秒級 mtime / ctime, 並在 API 24-26 保守停用緩存重用
- `優化` ABI 上報按已安裝 base / split APK 中實際存在的 `libmediainfo.so` 動態生成, 並保留安全的進程 ABI 回退
- `優化` 增強快照小節解析, 正確處理重複和編號流, 畸形行, 值內冒號, 重複字段與獨立輸出選項
- `優化` 加入可重現的合成基準與真實媒體驗證工具, 並記錄 x86, x86_64 與 ARM64 的完整效能基線
- `優化` 重建 10 語言 README, 插件使用說明與更新日誌生成鏈路, 加入漂移校驗和 GitHub Actions 門禁

#### v1.0.0

_2026/07/15_

- `新增` 首個正式版本: 為 AutoJs6 提供基於 MediaInfoLib 的媒體文件信息讀取能力, 一次調用即可獲取容器格式, 編碼, 時長, 分辨率, 碼率, 聲道等技術參數
- `新增` 腳本 API: Node 環境 `require("mediainfo")` 提供異步 `read`/`get`; Rhino 環境全局模塊 `mediainfo(path)` 同步返回可屬性存取的解析對象
- `新增` 三種讀取能力: 完整文本報告 (`inform`), 單項參數查詢 (`get`), 結構化 JSON 快照 (`snapshot`, schema 為 `autojs6-plugin-mediainfo-snapshot-v1`)
- `新增` 支援被 AutoJs6 通過 `org.autojs.plugin.MEDIAINFO` 自動發現; 插件在獨立進程中以只讀文件描述符接收並解析媒體內容, 不申請網絡與任何敏感系統權限
- `新增` 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四種單架構安裝包與包含全部架構的 `universal` 包, 發布文件名含版本號, 架構與 CRC32 摘要
- `新增` 插件信息, 使用說明, README 與更新日誌覆蓋 10 種語言: 簡體中文, 香港繁體, 台灣繁體, 英語, 法語, 西班牙語, 日語, 韓語, 俄語與阿拉伯語

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

本節面向希望從源碼構建插件的開發者.

構建 debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

構建 release APK (已啟用 ABI 拆分, 一次產出 4 個單架構包與 1 個 `universal` 包; 在不入庫的 `sign.properties` 中配置簽名後自動簽名):

```powershell
.\gradlew.bat :app:assembleRelease
```

發布歸檔可運行 `:app:appendDigestToReleasedFiles` 任務, 將 `app/release` 下的 APK 複製到 `app/releases` 並重命名為 `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk` 形式.

構建參數集中於 `version.properties`: 最低 SDK 24 (Android 7.0), 目標 SDK 36, 目前版本 1.1.0.

******

### 本地化與文檔生成

******

```text
.readme/common.json
.readme/lang_*.json
.readme/template_readme.md
.readme/template_plugin_instruction.md
.changelog/lang_*.json
.changelog/template_changelog.md
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 提供本地化插件描述與錯誤信息, `plugin_instruction.md` 提供宿主插件中心展示的使用說明. README, 更新日誌與使用說明均由 JSON 源生成: 修改 `.readme/` 與 `.changelog/` 下的源文件後運行 `py .python/generate_markdown.py` 重新生成全部產物, 生成產物不手工編輯; 運行 `py .python/generate_markdown.py --check` 可校驗源文件與產物是否同步 (CI 亦會自動校驗).

******

### 許可

******

項目代碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). 內置的 `libmediainfo.so` 基於 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (MediaArea.net SARL, BSD 風格許可) 構建, JNI 封裝源自 [MediaInfoLib-android](https://github.com/olegazyx/MediaInfoLib-android) 項目.

******

### 相關連結

******

- AutoJs6 MediaInfo 文件: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方網站: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 項目: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 封裝: https://github.com/olegazyx/MediaInfoLib-android
