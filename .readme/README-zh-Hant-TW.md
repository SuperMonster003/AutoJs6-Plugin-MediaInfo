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

`read(path, options?)` 傳回結構化快照 (見下文); `get(path, streamKind, parameter, options?)` 傳回參數原始文字. 相對路徑以工作目錄為基準, 亦支援絕對路徑及上層目錄, 檔案須在 Android 允許宿主讀取的範圍內. 請傳入檔案路徑, 而非 content URI.

Rhino 環境 (AutoJs6 預設腳本引擎) 中 `mediainfo` 為全域模組, `mediainfo(path)` 與 `mediainfo.read(path)` 等價, 同步回傳解析物件:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

回傳物件上, `path` 與 `inform` 分別為解析後的路徑與完整文字報告; 各串流類型 (如 `general`, `video`, `audio`) 既可作為屬性讀取已解析欄位 (如 `mi.video.width`, 欄位名為 camelCase), 也可作為函式即時查詢原始參數 (如 `mi.audio("BitRate")`). Rhino 腳本可存取主程式有權讀取的任意路徑.

MediaInfo 查詢支援從 0 開始的 streamNumber, countGet 串流計數以及用於單位, 說明和可讀名稱的 infoKind; Rhino 和 Node 保持預設第 1 條串流的 TEXT 查詢, 並協商外掛擴充能力:

```javascript
// Rhino
const path = "/sdcard/Download/movie.mkv";
const count = mediainfo.countGet(path, "audio");
for (let index = 0; index < count; index++) {
  console.log(mediainfo.get(path, "audio", "SamplingRate", { streamNumber: index }));
}
console.log(mediainfo.get(path, "audio", "SamplingRate", { infoKind: "MEASURE" }));
```

```javascript
"nodejs";
const mi = require("mediainfo");
(async () => {
  const count = await mi.countGet("movie.mkv", "audio");
  for (let index = 0; index < count; index++) {
    console.log(await mi.get("movie.mkv", "audio", "SamplingRate", { streamNumber: index }));
  }
})();
```

******

### 執行示範

******

AutoJs6 深淺色介面. Rhino 範例讀取 8 kHz 與 16 kHz 兩條 PCM 音軌; 點擊 MediaInfo 按鈕後, 完整報告顯示媒體檔案的原始路徑.

<table>
  <tr>
    <th>Rhino 指令碼輸出</th>
    <th>媒體檔案詳細資訊</th>
    <th>MediaInfo 詳情</th>
  </tr>
  <tr>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-script.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-script.png?raw=true" alt="Rhino 指令碼輸出" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-dialog.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-dialog.png?raw=true" alt="媒體檔案詳細資訊" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-details.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-details.png?raw=true" alt="MediaInfo 詳情" width="260" /></picture></td>
  </tr>
</table>

可使用 [雙音軌樣本](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/androidTest/assets/mediainfo-two-audio.mka) 執行 [示範指令碼](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/demo/mediainfo.js), 或將路徑改為自己的媒體檔案. 樣本為合成靜音. 原始路徑顯示與擴充查詢需同時更新配套宿主及外掛.

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

#### Node 指令碼提示路徑必須位於工作目錄內 (path must stay inside the scoped working directory)?

請同時更新 AutoJs6 與 Node Runtime 外掛. 目前版本支援專案目錄外的一般檔案路徑, 讀取權限由 Android 決定; 舊版宿主或執行環境可能仍保留原有的專案目錄限制.

#### `get()` 回傳了空字串?

參數名需使用 MediaInfo 原生參數 (如 `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), 且目標串流需實際存在. 可先用 `read()` 檢視 `sections` 中實際可用的欄位, 或直接檢視 `inform` 完整報告.

#### 讀取大檔案時比較慢?

一般檔案現已免整檔複製直接解析, 大檔案可避開與檔案大小線性相關的複製開銷. 管線等不可隨機存取描述符或直讀失敗的格式仍會使用暫存副本, 此回退路徑耗時仍與傳入位元組數相關; MediaInfoLib 本身解析耗時則取決於格式與內容.

#### 解析結果會快取嗎, 逾時如何處理?

會. 在 Android 8.1 (API 27) 及以上系統中, 對身分穩定且未變更的一般檔案, 外掛在目前處理程序內快取報告, 查詢與快照: 最多 32 個檔案, 每個檔案 64 個查詢, 10 分鐘滑動有效期, 文字總量約 2 MiB. API 24-26 因無法取得奈秒級檔案時間戳而保守停用快取; 低記憶體或處理程序結束時也會清空. 每次 AIDL 呼叫上限為 30 秒; 逾時會協同取消原生解析或中斷回退複製, 刪除暫存檔案, 並回傳包含 `MEDIAINFO_TIMEOUT` 的例外.

#### 檔案有多條音軌或字幕, 如何讀取第二條及之後的串流?

升級宿主和 Node 執行環境後, 使用 get(path, "audio", "Format", {streamNumber: 1}) 查詢第 2 條音軌, 使用 countGet(path, "audio") 取得串流數量, 使用 infoKind: "MEASURE" 查詢單位. 擴充查詢前可檢查 capabilities(). 詳見 [查詢契約](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_QUERY.md).

#### 外掛會連網或申請敏感權限嗎?

不會. 外掛資訊清單不含網路, 儲存空間, 相機等任何敏感系統權限, 僅宣告與 AutoJs6 通訊所需的外掛權限. 媒體內容由主程式以唯讀描述符傳入, 解析產生的暫存副本隨即刪除.

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

`MediainfoPluginService` 透過 AIDL 介面 `IMediainfoPlugin` 公開 `getInfo`/`inform`/`get`/`snapshot`/`getDetail`/`countGet` 六個方法; 媒體內容以唯讀 `ParcelFileDescriptor` 加顯示名傳參, `snapshot` 另接受包含 `includeInform`/`includeSections` 的 `Bundle` 選項. 服務與 `WakeActivity` 均受 `org.autojs.permission.PLUGIN` 權限保護.

媒體解析由內建的 MediaInfoLib 原生程式庫提供.

******

### 開發路線圖

******

外掛的能力規劃與完成情況以可勾選清單維護在 ROADMAP.md 中, 按里程碑組織並附驗收條件, 涵蓋跨串流查詢, 免複製解析, 動態 ABI 上報, 原生程式庫演進與持續整合等方向. 未勾選條目表示規劃意向而非目前版本能力, 歡迎透過 Issues 參與討論.

- [檢視 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v2.1.3

_2026/09/19_

- `修復` AGP 9.1 建置時的 SDK XML v4 解析警告及 JVM 單元測試組裝工作誤觸發 APK 原生程式庫對齊檢查的問題 (共用建置外掛 1.8.3)
- `最佳化` 將 compileSdk 與 targetSdk 提升到 37 (Android 17), 外掛程式行為不受新目標版本影響

#### v2.1.2

_2026/09/13_

- `修復` 發佈彙整使用目前建構並驗證版本和簽章, 避免舊 APK 被冠以新版本檔案名稱
- `修復` 外掛中繼資料的建置日期固定使用英文, 不受建置機器語言影響
- `最佳化` 宿主啟用, 外掛中繼資料, 多語言文件與簽章發佈彙整遵循統一外掛規範

#### v2.1.1

_2026/09/11_

- `最佳化` 建置階段校驗 64 位原生函式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-TW.md)

******

### 建置

******

本節面向希望從原始碼建置外掛的開發者.

建置前遞迴簽出儲存庫及兩個固定提交的官方子模組:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

建置 debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

建置 release APK:

```powershell
.\gradlew.bat :app:assembleRelease
```

發佈歸檔可執行 `:app:appendDigestToReleasedFiles` 工作, 將 `app/release` 下的 APK 複製到 `app/releases` 並重新命名為 `autojs6-plugin-mediainfo-v2.1.3-<abi>-<crc32>.apk` 形式.

建置參數集中於 `version.properties`: 最低 SDK 24 (Android 7.0), 目標 SDK 37, 目前版本 2.1.3.

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

專案程式碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). v2 官方原始碼建置線的 `libmediainfo.so` 來自 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) 與 [ZenLib](https://github.com/MediaArea/ZenLib) (zlib 授權), 相容 JNI 橋由本儲存庫維護. 已凍結的 v1.1.0 二進位來源另行記錄.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### 相關連結

******

- AutoJs6 MediaInfo 文件: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方網站: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 專案: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 封裝: https://github.com/olegazyx/MediaInfoLib-android


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/docs/16kb.md)
