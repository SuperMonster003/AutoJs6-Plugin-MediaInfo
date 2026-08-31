MediaInfo 插件 (MediaInfo Plugin) 為 AutoJs6 提供媒體文件信息讀取能力. 安裝後, 腳本只需一行代碼即可獲取視頻, 音頻, 圖片等文件的容器格式, 編碼, 時長, 分辨率, 碼率, 聲道等上百項技術參數, AutoJs6 文件列表中的媒體信息對話框也將由本插件提供完整解析報告. 解析能力來自與桌面端 MediaInfo 同源的開源庫 MediaInfoLib.

### 使用方法

1. 從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 頁面下載與設備匹配的插件 APK 並安裝到運行 AutoJs6 的設備上; 拿不準選哪個時, 可直接選 `universal` 包, 或參考下方 `如何選擇安裝包`.
2. 打開 AutoJs6 的插件中心, 確認 `MediaInfo` 插件已被識別並處於啟用狀態.
3. 在腳本中按下方 `腳本 API` 的示例調用 `mediainfo` 模塊; 也可以在 AutoJs6 文件列表中打開媒體文件的媒體信息對話框直接查看完整報告.

若插件中心未顯示該插件, 請先將 AutoJs6 升級到較新版本 (內部版本號 3923 及以上). 插件自身支援 Android 7.0 (API 24) 及以上的設備.

### 腳本 API

Node 環境 (腳本首行聲明 `"nodejs"`) 中通過 `require("mediainfo")` 獲取模塊, 全部方法返回 Promise:

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

`read(path, options?)` 返回結構化快照對象 (見下方 `快照結構與選項`); `get(path, streamKind?, parameter)` 返回參數原始文本, `streamKind` 預設為 `general`. 出於安全限制, Node 腳本只能存取工程目錄內的文件, 相對路徑基於工程根目錄解析.

Rhino 環境 (AutoJs6 預設腳本引擎) 中 `mediainfo` 為全局模塊, `mediainfo(path)` 與 `mediainfo.read(path)` 等價, 同步返回解析對象:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返回對象上, `path` 與 `inform` 分別為解析後的路徑與完整文本報告; 各流類型 (如 `general`, `video`, `audio`) 既可作為屬性讀取已解析字段 (如 `mi.video.width`, 字段名為 camelCase), 也可作為函數實時查詢原始參數 (如 `mi.audio("BitRate")`). Rhino 腳本可存取宿主有權讀取的任意路徑.

### 流類型

`get()` 的 `streamKind` 參數支援以下流類型:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 不區分大小寫, 會映射到 MediaInfo 原生流類型; 查詢不存在的流或無值參數時返回空字符串.

更多用法與字段說明可參閱 [AutoJs6 MediaInfo 文件](https://docs.autojs6.com/#/mediainfo) 與 [項目主頁](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
