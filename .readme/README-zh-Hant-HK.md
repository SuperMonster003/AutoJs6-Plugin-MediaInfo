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

`read(path, options?)` 傳回結構化快照 (見下文); `get(path, streamKind, parameter, options?)` 傳回參數原始文字. 相對路徑以工作目錄為基準, 亦支援絕對路徑及上層目錄, 檔案須在 Android 允許宿主讀取的範圍內. 請傳入檔案路徑, 而非 content URI.

Rhino 環境 (AutoJs6 預設腳本引擎) 中 `mediainfo` 為全局模塊, `mediainfo(path)` 與 `mediainfo.read(path)` 等價, 同步返回解析對象:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返回對象上, `path` 與 `inform` 分別為解析後的路徑與完整文本報告; 各流類型 (如 `general`, `video`, `audio`) 既可作為屬性讀取已解析字段 (如 `mi.video.width`, 字段名為 camelCase), 也可作為函數實時查詢原始參數 (如 `mi.audio("BitRate")`). Rhino 腳本可存取宿主有權讀取的任意路徑.

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
    <th>Rhino 腳本輸出</th>
    <th>媒體檔案詳細資訊</th>
    <th>MediaInfo 詳情</th>
  </tr>
  <tr>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-script.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-script.png?raw=true" alt="Rhino 腳本輸出" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-dialog.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-dialog.png?raw=true" alt="媒體檔案詳細資訊" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-details.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-details.png?raw=true" alt="MediaInfo 詳情" width="260" /></picture></td>
  </tr>
</table>

可使用 [雙音軌樣本](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/androidTest/assets/mediainfo-two-audio.mka) 執行 [示範腳本](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/demo/mediainfo.js), 或將腳本路徑改為自己的媒體檔案. 樣本為合成靜音. 原始路徑顯示與擴充查詢需同時更新配套宿主及插件.

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

請同時更新 AutoJs6 與 Node Runtime 插件. 目前版本支援專案目錄外的一般檔案路徑, 讀取權限由 Android 決定; 舊版宿主或執行環境可能仍保留原有的專案目錄限制.

#### `get()` 返回了空字符串?

參數名需使用 MediaInfo 原生參數 (如 `Format`, `Duration`, `Width`, `BitRate`, `FileSize`), 且目標流需實際存在. 可先用 `read()` 查看 `sections` 中實際可用的字段, 或直接查看 `inform` 完整報告.

#### 讀取大文件時比較慢?

常規文件現已免整文件複製直接解析, 大文件可避開與文件大小線性相關的複製開銷. 管道等不可隨機存取描述符或直讀失敗的格式仍會使用臨時副本, 此回退路徑耗時仍與傳入位元組數相關; MediaInfoLib 本身解析耗時則取決於格式與內容.

#### 解析結果會緩存嗎, 超時如何處理?

會. 在 Android 8.1 (API 27) 及以上系統中, 對身份穩定且未變更的常規文件, 插件在目前進程內緩存報告, 查詢與快照: 最多 32 個文件, 每個文件 64 個查詢, 10 分鐘滑動有效期, 文字總量約 2 MiB. API 24-26 因無法取得納秒級文件時間戳而保守停用緩存; 低記憶體或進程結束時亦會清空. 每次 AIDL 調用上限為 30 秒; 超時會協作取消原生解析或中斷回退複製, 刪除臨時文件, 並返回包含 `MEDIAINFO_TIMEOUT` 的異常.

#### 文件有多條音軌或字幕, 如何讀取第二條及之後的流?

升級宿主和 Node 執行環境後, 使用 get(path, "audio", "Format", {streamNumber: 1}) 查詢第 2 條音軌, 使用 countGet(path, "audio") 取得串流數量, 使用 infoKind: "MEASURE" 查詢單位. 擴充查詢前可檢查 capabilities(). 詳見 [查詢契約](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_QUERY.md).

#### 插件會聯網或申請敏感權限嗎?

不會. 插件清單不含網絡, 存儲, 相機等任何敏感系統權限, 僅聲明與 AutoJs6 通信所需的插件權限. 媒體內容由宿主以只讀描述符傳入, 解析產生的臨時副本隨即刪除.

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

`MediainfoPluginService` 通過 AIDL 接口 `IMediainfoPlugin` 暴露 `getInfo`/`inform`/`get`/`snapshot`/`getDetail`/`countGet` 六個方法; 媒體內容以只讀 `ParcelFileDescriptor` 加顯示名傳參, `snapshot` 另接受包含 `includeInform`/`includeSections` 的 `Bundle` 選項. 服務與 `WakeActivity` 均受 `org.autojs.permission.PLUGIN` 權限保護.

媒體解析由內置的 MediaInfoLib 原生庫提供.

******

### 開發路線圖

******

插件的能力規劃與完成情況以可勾選清單維護在 ROADMAP.md 中, 按里程碑組織並附驗收條件, 涵蓋跨流查詢, 免拷貝解析, 動態 ABI 上報, 原生庫演進與持續集成等方向. 未勾選條目表示規劃意向而非目前版本能力, 歡迎通過 Issues 參與討論.

- [查看 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v2.1.2

_2026/09/13_

- `修復` 發佈彙整使用目前建構並驗證版本和簽章, 避免舊 APK 被冠以新版本檔案名稱
- `修復` 插件中繼資料的建置日期固定使用英文, 不受建置機器語言影響
- `優化` 宿主啟用, 外掛中繼資料, 多語言文件與簽章發佈彙整遵循統一外掛規範

#### v2.1.1

_2026/09/11_

- `優化` 建置階段校驗 64 位原生程式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告

#### v2.1.0

_2026/09/10_

- `新增` MediaInfo 查詢支援從 0 開始的 streamNumber, countGet 串流計數以及用於單位, 說明和可讀名稱的 infoKind; Rhino 和 Node 保持預設第 1 條串流的 TEXT 查詢, 並協商外掛擴充能力
- `新增` 明確選擇的 snapshot v2 將原生 JSON 同類串流按陣列分組並提供引擎版本, snapshot v1 繼續作為預設協定
- `修復` MediaInfo 詳情與快照的 Complete name 顯示原始檔案路徑, 避免顯示私有快取或描述符路徑, 同時保留快照的顯示檔案名稱
- `優化` 同步 Node 檔案路徑存取說明, 補充深淺色裝置截圖與可執行的雙音軌示範腳本

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

本節面向希望從源碼構建插件的開發者.

構建前遞歸檢出倉庫及兩個固定提交的官方子模組:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

構建 debug APK:

```powershell
.\gradlew.bat :app:assembleDebug
```

構建 release APK:

```powershell
.\gradlew.bat :app:assembleRelease
```

發布歸檔可運行 `:app:appendDigestToReleasedFiles` 任務, 將 `app/release` 下的 APK 複製到 `app/releases` 並重命名為 `autojs6-plugin-mediainfo-v2.1.2-<abi>-<crc32>.apk` 形式.

構建參數集中於 `version.properties`: 最低 SDK 24 (Android 7.0), 目標 SDK 36, 目前版本 2.1.2.

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

項目代碼使用 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). v2 官方源碼構建線的 `libmediainfo.so` 來自 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) 與 [ZenLib](https://github.com/MediaArea/ZenLib) (zlib 許可), 兼容 JNI 橋由本倉庫維護. 已凍結的 v1.1.0 二進制來源另行記錄.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### 相關連結

******

- AutoJs6 MediaInfo 文件: https://docs.autojs6.com/#/mediainfo
- MediaInfo 官方網站: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 項目: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 封裝: https://github.com/olegazyx/MediaInfoLib-android


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/docs/16kb.md)
