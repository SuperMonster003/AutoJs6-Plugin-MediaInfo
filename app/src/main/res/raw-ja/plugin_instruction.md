MediaInfo プラグイン (MediaInfo Plugin) は AutoJs6 にメディアファイル情報の読み取り機能を提供します. インストール後は, スクリプト 1 行で動画, 音声, 画像ファイルのコンテナ形式, コーデック, 再生時間, 解像度, ビットレート, チャンネル数など数百項目の技術情報を取得でき, AutoJs6 のファイル一覧にあるメディア情報ダイアログも本プラグインによる完全な解析レポートを表示します. 解析エンジンには, デスクトップ版 MediaInfo と同じオープンソースライブラリ MediaInfoLib を採用しています.

### 使用方法

1. [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) ページから端末に合ったプラグイン APK をダウンロードし, AutoJs6 が動作する端末にインストールします. 迷ったときは `universal` 版を選ぶか, 下の `インストールパッケージの選び方` を参照してください.
2. AutoJs6 のプラグインセンターを開き, `MediaInfo` プラグインが認識され有効になっていることを確認します.
3. 下の `スクリプト API` の例に従ってスクリプトから `mediainfo` モジュールを呼び出します. AutoJs6 のファイル一覧でメディアファイルのメディア情報ダイアログを開き, 完全なレポートを直接確認することもできます.

プラグインセンターにプラグインが表示されない場合は, まず AutoJs6 を新しいバージョン (内部ビルド 3923 以上) に更新してください. プラグイン自体は Android 7.0 (API 24) 以上の端末をサポートします.

### スクリプト API

Node 環境 (スクリプト先頭で `"nodejs"` を宣言) では `require("mediainfo")` でモジュールを取得し, すべてのメソッドが Promise を返します:

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

`read(path, options?)` は構造化スナップショットオブジェクトを返します (下の `スナップショットの構造とオプション` を参照). `get(path, streamKind?, parameter)` はパラメータの生テキストを返し, `streamKind` の既定値は `general` です. 安全上の制限により, Node スクリプトはプロジェクトディレクトリ内のファイルのみアクセスでき, 相対パスはプロジェクトルートを基準に解決されます.

Rhino 環境 (AutoJs6 の既定スクリプトエンジン) では `mediainfo` はグローバルモジュールで, `mediainfo(path)` と `mediainfo.read(path)` は等価であり, 解析オブジェクトを同期的に返します:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返されたオブジェクトの `path` と `inform` はそれぞれ解決済みパスと完全なテキストレポートです. 各ストリーム種別 (`general`, `video`, `audio` など) は, 解析済みフィールドを公開するプロパティ (例: `mi.video.width`, フィールド名は camelCase) としても, 生パラメータをリアルタイムに問い合わせる関数 (例: `mi.audio("BitRate")`) としても機能します. Rhino スクリプトはホストが読み取り可能な任意のパスにアクセスできます.

### ストリーム種別

`get()` の `streamKind` パラメータは次のストリーム種別をサポートします:

```text
general, video, audio, text, other, image, menu
```

`streamKind` は大文字小文字を区別せず, MediaInfo のネイティブストリーム種別にマップされます. 存在しないストリームや値のないパラメータを問い合わせると空文字列が返ります.

より詳しい使い方とフィールドの説明は [AutoJs6 MediaInfo ドキュメント](https://docs.autojs6.com/#/mediainfo) と [プロジェクトホームページ](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo) を参照してください.
