<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用於讀取媒體檔案資訊的 MediaInfo 外掛</p>

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
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- 繁體中文 (台灣) [zh-Hant-TW] # 目前
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

MediaInfo 外掛 (MediaInfo Plugin) 為 AutoJs6 提供媒體檔案資訊讀取能力. 安裝後, 腳本只需一行程式碼即可取得視訊, 音訊, 圖片等檔案的容器格式, 編碼, 時長, 解析度, 位元速率, 聲道等上百項技術參數, AutoJs6 檔案清單中的媒體資訊對話方塊也將由本外掛提供完整解析報告. 解析能力來自與桌面端 MediaInfo 同源的開源程式庫 MediaInfoLib.

外掛在獨立處理程序中執行, 由 AutoJs6 自動發現, 無需任何手動設定. 讀取檔案時, 主程式將媒體內容以唯讀檔案描述符交給外掛; 對可隨機存取的一般檔案描述符, 外掛透過 `/proc/self/fd` 交由 MediaInfoLib 直接讀取, 遇到管線等不可隨機存取描述符或直讀解析失敗時, 才回退到私有快取暫存副本並在呼叫結束立即刪除. 全程無需網路, 也不申請任何敏感系統權限.

******

### 功能亮點

******

- 開箱即用: 安裝後無需任何設定, AutoJs6 自動發現外掛, 腳本與檔案清單的媒體資訊對話方塊即可直接使用.
- 資訊全面: 容器格式, 編碼, 時長, 解析度, 影格率, 位元速率, 聲道, 取樣率等技術參數一次呼叫全部取得.
- 三種讀取方式: 完整文字報告 (`inform`), 單項參數查詢 (`get`), 結構化 JSON 快照 (`read`/`snapshot`), 按需選用.
- 雙引擎支援: Node 環境透過 `require("mediainfo")` 非同步呼叫; Rhino 環境透過全域模組 `mediainfo(path)` 同步回傳可屬性存取的解析物件.
- 格式覆蓋廣: 解析能力來自與桌面端 MediaInfo 同源的 MediaInfoLib, 支援視訊, 音訊, 圖片等大量常見與小眾格式.
- 五種安裝套件: 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四種單架構套件與包含全部架構的 `universal` 套件, 按裝置按需選擇.
- 隱私友善: 外掛在獨立處理程序中解析, 僅接收主程式傳入的唯讀檔案描述符, 不申請網路與任何敏感系統權限.
- 多語言: 外掛資訊, 使用說明, README 與更新日誌覆蓋 10 種語言.

******

### 使用方法

******

1. 從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面下載與裝置相符的外掛 APK 並安裝到執行 AutoJs6 的裝置上; 拿不準選哪個時, 可直接選 `universal` 套件, 或參考下方 `如何選擇安裝套件`.
2. 開啟 AutoJs6 的外掛中心, 確認 `MediaInfo` 外掛已被識別並處於啟用狀態.
3. 在腳本中按下方 `腳本 API` 的範例呼叫 `mediainfo` 模組; 也可以在 AutoJs6 檔案清單中開啟媒體檔案的媒體資訊對話方塊直接檢視完整報告.

> 若外掛中心未顯示該外掛, 請先將 AutoJs6 升級到較新版本 (內部版本號 3923 及以上). 外掛自身支援 Android 7.0 (API 24) 及以上的裝置.

******

### 如何選擇安裝套件

******

每個發行版本包含 5 個 APK, 差別僅在於內建了哪些架構的原生程式庫:

| 安裝套件 | 適用對象 |
|---|---|
| `arm64-v8a` | 絕大多數現代 Android 手機與平板 (64 位元 ARM), 優先選擇 |
| `armeabi-v7a` | 較早期的 32 位元 ARM 裝置 |
| `x86_64` | 64 位元 x86 模擬器與少數 x86 裝置 |
| `x86` | 32 位元 x86 模擬器與少數 x86 裝置 |
| `universal` | 內建全部 4 種架構, 體積最大; 適用於任何裝置, 也是拿不準架構時的穩妥選擇 |

外掛在自身處理程序中載入原生程式庫. 若誤裝了與裝置架構不相符的單架構套件, 呼叫時會提示 MediaInfo 程式庫不可用 (`MediaInfo library is not available`), 換裝 `universal` 套件即可解決.

******

### 腳本 API

******

Node 環境 (腳本首行宣告 `"nodejs"`) 中透過 `require("mediainfo")` 取得模組, 全部方法回傳 Promise:

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

`read(path, options?)` 回傳結構化快照物件 (見下方 `快照結構與選項`); `get(path, streamKind?, parameter)` 回傳參數原始文字, `streamKind` 預設為 `general`. 出於安全限制, Node 腳本只能存取專案目錄內的檔案, 相對路徑基於專案根目錄解析.

Rhino 環境 (AutoJs6 預設腳本引擎) 中 `mediainfo` 為全域模組, `mediainfo(path)` 與 `mediainfo.read(path)` 等價, 同步回傳解析物件:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

回傳物件上, `path` 與 `inform` 分別為解析後的路徑與完整文字報告; 各串流類型 (如 `general`, `video`, `audio`) 既可作為屬性讀取已解析欄位 (如 `mi.video.width`, 欄位名為 camelCase), 也可作為函式即時查詢原始參數 (如 `mi.audio("BitRate")`). Rhino 腳本可存取主程式有權讀取的任意路徑.

******

### 快照結構與選項

******

Node 環境 `read()` 回傳的快照結構如下 (外掛 AIDL `snapshot` 方法回傳的 JSON 與之一致, 但 `schema` 為 `autojs6-plugin-mediainfo-snapshot-v1` 且不含 `path` 欄位):

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

- `includeInform`: 是否包含 `inform` 文字報告, 預設 `true`; 設為 `false` 時 `inform` 為空字串, 可減小回傳體積.
- `includeSections`: 是否解析報告並產生 `sections`, 預設 `true`; 設為 `false` 時 `sections` 為空物件.

`sections` 以報告小節名的小寫形式作為鍵 (存在多條同類串流時, 小節名可能帶編號, 如 `audio #1`), 值一律為物件陣列; 欄位名轉換為 camelCase, 欄位值保留 MediaInfo 原始文字 (含單位與千位分隔空格, 如 `1 920 pixels`).

******

### 串流類型

******

`get()` 的 `streamKind` 參數支援以下串流類型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不區分大小寫, 會對應到 MediaInfo 原生串流類型; 查詢不存在的串流或無值參數時回傳空字串.

******

### 常見問題

******

#### 如何確認外掛已經生效?

開啟 AutoJs6 的外掛中心, 能看到 `MediaInfo` 外掛即表示主程式已識別. 隨後執行任意 `mediainfo` 腳本範例, 能正常回傳結果即說明外掛運作正常.

#### 為什麼應用程式清單裏沒有外掛的圖示?

這是正常現象. 外掛沒有獨立介面, 也不在桌面建立啟動圖示, 安裝後由 AutoJs6 在背景自動發現和呼叫, 全部互動都在 AutoJs6 內完成.

#### Node 腳本提示路徑必須位於工作目錄內 (path must stay inside the scoped working directory)?

Node 引擎出於安全限制只允許存取專案目錄內的檔案. 請將媒體檔案放入專案目錄內再讀取; 若需要存取其他路徑 (如相簿或下載目錄), 可改用 Rhino 引擎腳本.

#### `get()` 回傳了空字串?

參數名需使用 MediaInfo 原生參數 (如 `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), 且目標串流需實際存在. 可先用 `read()` 檢視 `sections` 中實際可用的欄位, 或直接檢視 `inform` 完整報告.

#### 讀取大檔案時比較慢?

一般檔案現已免整檔複製直接解析, 大檔案可避開與檔案大小線性相關的複製開銷. 管線等不可隨機存取描述符或直讀失敗的格式仍會使用暫存副本, 此回退路徑耗時仍與傳入位元組數相關; MediaInfoLib 本身解析耗時則取決於格式與內容.

#### 解析結果會快取嗎, 逾時如何處理?

會. 在 Android 8.1 (API 27) 及以上系統中, 對身分穩定且未變更的一般檔案, 外掛在目前處理程序內快取報告, 查詢與快照: 最多 32 個檔案, 每個檔案 64 個查詢, 10 分鐘滑動有效期, 文字總量約 2 MiB. API 24-26 因無法取得奈秒級檔案時間戳而保守停用快取; 低記憶體或處理程序結束時也會清空. 每次 AIDL 呼叫上限為 30 秒; 逾時會協同取消原生解析或中斷回退複製, 刪除暫存檔案, 並回傳包含 `MEDIAINFO_TIMEOUT` 的例外.

#### 檔案有多條音軌或字幕, 如何讀取第二條及之後的串流?

快照 `sections` 會完整保留報告中的全部小節 (多串流時小節名帶編號, 如 `audio #2`), 可直接從中讀取; `get()` 目前固定查詢同類串流中的第 1 條, 指定串流序號的能力已列入 [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### 外掛會連網或申請敏感權限嗎?

不會. 外掛資訊清單不含網路, 儲存空間, 相機等任何敏感系統權限, 僅宣告與 AutoJs6 通訊所需的外掛權限. 媒體內容由主程式以唯讀描述符傳入, 解析產生的暫存副本隨即刪除.

******

### 權限與安全

******

媒體檔案可能來自不可信來源, 外掛在設計上為解析過程設置了多道防線:

- 處理程序隔離: 解析在外掛自身處理程序中完成, 原生程式庫不注入主程式處理程序, 即使解析異常也不影響 AutoJs6 穩定執行.
- 最小資料面: 外掛自身無法讀取裝置儲存空間, 僅接收主程式開啟的唯讀檔案描述符與檔案顯示名.
- 能直讀則直讀, 回退即用即清: 可隨機存取的一般描述符不產生媒體副本; 僅相容性回退寫入私有快取, 呼叫結束立即刪除.
- 最小權限: 不申請網路, 儲存空間, 相機等任何敏感系統權限; 服務與喚醒入口均受 AutoJs6 外掛權限 (`org.autojs.permission.PLUGIN`) 保護, 第三方應用程式無法直接呼叫.
- 開源可稽核: 外掛程式碼, 建置腳本與文件產生鏈路全部開源, 原生程式庫來源與 JNI 封裝出處在授權章節明確標註.

請僅從官方 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面或其他可信渠道取得外掛安裝套件; 來源不明的安裝套件即使名稱與版本號相同, 也可能被竄改.

******

### 外掛介面

******

以下資訊面向 AutoJs6 主程式與外掛開發者, 主程式透過這些識別碼發現外掛並完成能力協商:

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

`MediainfoPluginService` 透過 AIDL 介面 `IMediainfoPlugin` 公開 `getInfo`/`inform`/`get`/`snapshot` 四個方法; 媒體內容以唯讀 `ParcelFileDescriptor` 加顯示名傳參, `snapshot` 另接受包含 `includeInform`/`includeSections` 的 `Bundle` 選項. 服務與 `WakeActivity` 均受 `org.autojs.permission.PLUGIN` 權限保護.

外掛掃描已安裝的 base / split APK 中實際包含的 `libmediainfo.so` 並動態上報 ABI; 單架構套件僅上報對應 ABI, `universal` 套件上報全部 4 種. 若 APK 路徑無法讀取, 則依目前程序位元與已解壓的原生程式庫安全回退.

******

### 開發路線圖

******

外掛的能力規劃與完成情況以可勾選清單維護在 ROADMAP.md 中, 按里程碑組織並附驗收條件, 涵蓋跨串流查詢, 免複製解析, 動態 ABI 上報, 原生程式庫演進與持續整合等方向. 未勾選條目表示規劃意向而非目前版本能力, 歡迎透過 Issues 參與討論.

- [檢視 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v1.1.0

_2026/08/31_

- `新增` 免整檔複製解析: 可隨機存取的一般檔案描述符透過 `/proc/self/fd` 交由 MediaInfoLib 直讀, 管線或直讀失敗時才使用私有暫存副本
- `新增` 處理程序內結果快取: API 27+ 對完整報告, 欄位查詢與快照採用穩定檔案身分, LRU, 10 分鐘滑動有效期及低記憶體清理, 避免重複解析
- `新增` 協同取消與逾時: 每次 AIDL 呼叫設有 30 秒上限, 逾時中止原生解析或回退複製, 釋放暫存資源並回傳 `MEDIAINFO_TIMEOUT`
- `修復` 修正大檔案一般描述符被不必要地整檔複製的問題, 並保證成功, 失敗, 參數驗證, 取消和逾時路徑均關閉描述符與回退檔案
- `修復` 修正快取對快速檔案變更的識別精度: 使用奈秒級 mtime / ctime, 並在 API 24-26 保守停用快取重用
- `最佳化` ABI 上報依已安裝 base / split APK 中實際存在的 `libmediainfo.so` 動態產生, 並保留安全的處理程序 ABI 回退
- `最佳化` 增強快照小節解析, 正確處理重複和編號串流, 異常行, 值內冒號, 重複欄位與獨立輸出選項
- `最佳化` 加入可重現的合成基準與真實媒體驗證工具, 並記錄 x86, x86_64 與 ARM64 的完整效能基線
- `最佳化` 重建 10 語言 README, 外掛使用說明與更新日誌產生鏈路, 加入漂移驗證和 GitHub Actions 門禁

#### v1.0.0

_2026/07/15_

- `新增` 首個正式版本: 為 AutoJs6 提供基於 MediaInfoLib 的媒體檔案資訊讀取能力, 一次呼叫即可取得容器格式, 編碼, 時長, 解析度, 位元速率, 聲道等技術參數
- `新增` 腳本 API: Node 環境 `require("mediainfo")` 提供非同步 `read`/`get`; Rhino 環境全域模組 `mediainfo(path)` 同步回傳可屬性存取的解析物件
- `新增` 三種讀取能力: 完整文字報告 (`inform`), 單項參數查詢 (`get`), 結構化 JSON 快照 (`snapshot`, schema 為 `autojs6-plugin-mediainfo-snapshot-v1`)
- `新增` 支援被 AutoJs6 透過 `org.autojs.plugin.MEDIAINFO` 自動發現; 外掛在獨立處理程序中以唯讀檔案描述符接收並解析媒體內容, 不申請網路與任何敏感系統權限
- `新增` 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 四種單架構安裝套件與包含全部架構的 `universal` 套件, 發布檔名含版本號, 架構與 CRC32 摘要
- `新增` 外掛資訊, 使用說明, README 與更新日誌覆蓋 10 種語言: 簡體中文, 香港繁體, 台灣繁體, 英語, 法語, 西班牙語, 日語, 韓語, 俄語與阿拉伯語

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-TW.md)

******

### 建置

******

本節面向希望從原始碼建置外掛的開發者.

建置 debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

建置 release APK (已啟用 ABI 拆分, 一次產出 4 個單架構套件與 1 個 `universal` 套件; 在不入庫的 `sign.properties` 中設定簽章後自動簽章):

```powershell
.\gradlew.bat :app:assembleRelease
```

發佈歸檔可執行 `:app:appendDigestToReleasedFiles` 工作, 將 `app/release` 下的 APK 複製到 `app/releases` 並重新命名為 `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk` 形式.

建置參數集中於 `version.properties`: 最低 SDK 24 (Android 7.0), 目標 SDK 36, 目前版本 1.1.0.

******

### 在地化與文件產生

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

`strings.xml` 提供在地化外掛描述與錯誤資訊, `plugin_instruction.md` 提供主程式外掛中心展示的使用說明. README, 更新日誌與使用說明均由 JSON 來源產生: 修改 `.readme/` 與 `.changelog/` 下的來源檔後執行 `py .python/generate_markdown.py` 重新產生全部產物, 產生產物不手工編輯; 執行 `py .python/generate_markdown.py --check` 可校驗來源檔與產物是否同步 (CI 亦會自動校驗).

******

### 授權

******

專案程式碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). 內建的 `libmediainfo.so` 基於 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (MediaArea.net SARL, BSD 風格授權) 建置, JNI 封裝源自 [MediaInfoLib-android](https://github.com/olegazyx/MediaInfoLib-android) 專案.

******

### 相關連結

******

- AutoJs6 MediaInfo 文件: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方網站: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 專案: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 封裝: https://github.com/olegazyx/MediaInfoLib-android
