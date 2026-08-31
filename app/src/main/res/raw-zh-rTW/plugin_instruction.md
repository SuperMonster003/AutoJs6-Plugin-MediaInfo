MediaInfo 外掛 (MediaInfo Plugin) 為 AutoJs6 提供媒體檔案資訊讀取能力. 安裝後, 腳本只需一行程式碼即可取得視訊, 音訊, 圖片等檔案的容器格式, 編碼, 時長, 解析度, 位元速率, 聲道等上百項技術參數, AutoJs6 檔案清單中的媒體資訊對話方塊也將由本外掛提供完整解析報告. 解析能力來自與桌面端 MediaInfo 同源的開源程式庫 MediaInfoLib.

### 使用方法

1. 從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面下載與裝置相符的外掛 APK 並安裝到執行 AutoJs6 的裝置上; 拿不準選哪個時, 可直接選 `universal` 套件, 或參考下方 `如何選擇安裝套件`.
2. 開啟 AutoJs6 的外掛中心, 確認 `MediaInfo` 外掛已被識別並處於啟用狀態.
3. 在腳本中按下方 `腳本 API` 的範例呼叫 `mediainfo` 模組; 也可以在 AutoJs6 檔案清單中開啟媒體檔案的媒體資訊對話方塊直接檢視完整報告.

若外掛中心未顯示該外掛, 請先將 AutoJs6 升級到較新版本 (內部版本號 3923 及以上). 外掛自身支援 Android 7.0 (API 24) 及以上的裝置.

### 腳本 API

Node 環境 (腳本首行宣告 `"nodejs"`) 中透過 `require("mediainfo")` 取得模組, 全部方法回傳 Promise:

```javascript
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4");
  console.log(snapshot.sections.general[0].format);

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

### 串流類型

`get()` 的 `streamKind` 參數支援以下串流類型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不區分大小寫, 會對應到 MediaInfo 原生串流類型; 查詢不存在的串流或無值參數時回傳空字串.

更多用法與欄位說明可參閱 [AutoJs6 MediaInfo 文件](https://docs.autojs6.com/#/mediainfo) 與 [專案主頁](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
