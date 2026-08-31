<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>メディアファイル情報を読み取る MediaInfo プラグイン</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 言語 (Languages)

******

現在の README.md は次の言語に対応しています:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- 日本語 [ja] # 現在
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### 概要

******

MediaInfo プラグイン (MediaInfo Plugin) は AutoJs6 にメディアファイル情報の読み取り機能を提供します. インストール後は, スクリプト 1 行で動画, 音声, 画像ファイルのコンテナ形式, コーデック, 再生時間, 解像度, ビットレート, チャンネル数など数百項目の技術情報を取得でき, AutoJs6 のファイル一覧にあるメディア情報ダイアログも本プラグインによる完全な解析レポートを表示します. 解析エンジンには, デスクトップ版 MediaInfo と同じオープンソースライブラリ MediaInfoLib を採用しています.

プラグインは独立したプロセスで動作し, AutoJs6 が自動的に検出するため設定は一切不要です. ファイル読み取り時, ホストはメディア内容を読み取り専用ファイルディスクリプタとして渡します. ランダムアクセス可能な通常ファイルは MediaInfoLib が `/proc/self/fd` 経由で直接読み取り, パイプなどランダムアクセスできないディスクリプタや直接解析の失敗時だけ私有キャッシュの一時コピーへフォールバックし, 呼び出し終了時に直ちに削除します. 全過程でネットワークを使用せず, 機密性の高いシステム権限も要求しません.

******

### 機能ハイライト

******

- すぐに使える: 設定不要で, AutoJs6 がプラグインを自動検出し, スクリプトとファイル一覧のメディア情報ダイアログからすぐに利用できます.
- 情報が充実: コンテナ形式, コーデック, 再生時間, 解像度, フレームレート, ビットレート, チャンネル数, サンプリングレートなどを 1 回の呼び出しで取得できます.
- 3 つの読み取り方式: 完全なテキストレポート (`inform`), 単一パラメータ検索 (`get`), 構造化 JSON スナップショット (`read`/`snapshot`) を用途に応じて選べます.
- 2 つのスクリプトエンジンに対応: Node 環境では `require("mediainfo")` で非同期に呼び出し, Rhino 環境ではグローバルモジュール `mediainfo(path)` がプロパティアクセス可能な解析オブジェクトを同期的に返します.
- 幅広い形式に対応: 解析はデスクトップ版 MediaInfo と同源の MediaInfoLib によるもので, 一般的な形式からマイナーな形式まで多数の動画, 音声, 画像形式をサポートします.
- 5 種類のインストールパッケージ: 4 種類の単一アーキテクチャ版 (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) と全アーキテクチャ同梱の `universal` 版から, 端末に合わせて選択できます.
- プライバシーに配慮: 解析は独立プロセス内で行われ, ホストから渡される読み取り専用ファイルディスクリプタのみを受け取り, ネットワーク権限や機密性の高いシステム権限を要求しません.
- 多言語対応: プラグイン情報, 使用説明, README, 更新履歴が 10 言語で提供されます.

******

### 使用方法

******

1. [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) ページから端末に合ったプラグイン APK をダウンロードし, AutoJs6 が動作する端末にインストールします. 迷ったときは `universal` 版を選ぶか, 下の `インストールパッケージの選び方` を参照してください.
2. AutoJs6 のプラグインセンターを開き, `MediaInfo` プラグインが認識され有効になっていることを確認します.
3. 下の `スクリプト API` の例に従ってスクリプトから `mediainfo` モジュールを呼び出します. AutoJs6 のファイル一覧でメディアファイルのメディア情報ダイアログを開き, 完全なレポートを直接確認することもできます.

> プラグインセンターにプラグインが表示されない場合は, まず AutoJs6 を新しいバージョン (内部ビルド 3923 以上) に更新してください. プラグイン自体は Android 7.0 (API 24) 以上の端末をサポートします.

******

### インストールパッケージの選び方

******

各リリースには 5 つの APK が含まれ, 違いは同梱するネイティブライブラリのアーキテクチャだけです:

| パッケージ | 適した対象 |
|---|---|
| `arm64-v8a` | 現代のほとんどの Android スマートフォンとタブレット (64 ビット ARM), 第一候補 |
| `armeabi-v7a` | やや古い 32 ビット ARM 端末 |
| `x86_64` | 64 ビット x86 エミュレータと一部の x86 端末 |
| `x86` | 32 ビット x86 エミュレータと一部の x86 端末 |
| `universal` | 全 4 アーキテクチャを同梱し最も大きい, どの端末でも動作し, 迷ったときの無難な選択 |

プラグインは自身のプロセス内でネイティブライブラリをロードします. 端末のアーキテクチャに合わない単一アーキテクチャ版を誤ってインストールすると, 呼び出し時に `MediaInfo library is not available` と表示されます. `universal` 版に入れ替えれば解決します.

******

### スクリプト API

******

Node 環境 (スクリプト先頭で `"nodejs"` を宣言) では `require("mediainfo")` でモジュールを取得し, すべてのメソッドが Promise を返します:

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

`read(path, options?)` は構造化スナップショットオブジェクトを返します (下の `スナップショットの構造とオプション` を参照). `get(path, streamKind?, parameter)` はパラメータの生テキストを返し, `streamKind` の既定値は `general` です. 安全上の制限により, Node スクリプトはプロジェクトディレクトリ内のファイルのみアクセスでき, 相対パスはプロジェクトルートを基準に解決されます.

Rhino 環境 (AutoJs6 の既定スクリプトエンジン) では `mediainfo` はグローバルモジュールで, `mediainfo(path)` と `mediainfo.read(path)` は等価であり, 解析オブジェクトを同期的に返します:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返されたオブジェクトの `path` と `inform` はそれぞれ解決済みパスと完全なテキストレポートです. 各ストリーム種別 (`general`, `video`, `audio` など) は, 解析済みフィールドを公開するプロパティ (例: `mi.video.width`, フィールド名は camelCase) としても, 生パラメータをリアルタイムに問い合わせる関数 (例: `mi.audio("BitRate")`) としても機能します. Rhino スクリプトはホストが読み取り可能な任意のパスにアクセスできます.

******

### スナップショットの構造とオプション

******

Node 環境の `read()` が返すスナップショットは次のとおりです (プラグイン AIDL の `snapshot` メソッドが返す JSON も同一ですが, `schema` は `autojs6-plugin-mediainfo-snapshot-v1` で `path` フィールドを含みません):

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

- `includeInform`: `inform` テキストレポートを含めるかどうか, 既定値は `true`. `false` にすると空文字列になり, 返却サイズを抑えられます.
- `includeSections`: レポートを解析して `sections` を生成するかどうか, 既定値は `true`. `false` にすると空オブジェクトになります.

`sections` のキーはレポートのセクション名を小文字化したものです (同種のストリームが複数ある場合, セクション名に `audio #1` のような番号が付くことがあります). 値はすべてオブジェクトの配列で, フィールド名は camelCase に変換され, フィールド値は単位や `1 920 pixels` のような千位区切りスペースを含む MediaInfo の元テキストを保持します.

******

### ストリーム種別

******

`get()` の `streamKind` パラメータは次のストリーム種別をサポートします:

```text
general, video, audio, text, other, image, menu
```

`streamKind` は大文字小文字を区別せず, MediaInfo のネイティブストリーム種別にマップされます. 存在しないストリームや値のないパラメータを問い合わせると空文字列が返ります.

******

### よくある質問

******

#### プラグインが有効になったことをどう確認できますか?

AutoJs6 のプラグインセンターを開き, `MediaInfo` プラグインが表示されていればホストに認識されています. その後, 任意の `mediainfo` スクリプト例を実行し, 正常に結果が返ればプラグインは動作しています.

#### アプリ一覧にプラグインのアイコンがないのはなぜですか?

正常な動作です. プラグインには独立した画面がなく, ランチャーアイコンも作成しません. インストール後は AutoJs6 がバックグラウンドで自動検出して呼び出し, すべての操作は AutoJs6 内で完結します.

#### Node スクリプトで `path must stay inside the scoped working directory` と表示されます?

Node エンジンは安全上の制限により, プロジェクトディレクトリ内のファイルのみアクセスを許可します. メディアファイルをプロジェクトディレクトリ内に置いてから読み取ってください. 他のパス (アルバムやダウンロードフォルダなど) にアクセスする必要がある場合は, Rhino エンジンのスクリプトをご利用ください.

#### `get()` が空文字列を返しました?

パラメータ名は MediaInfo のネイティブパラメータ (`Format`, `Duration`, `Width`, `BitRate`, `FileSize` など) を使用し, 対象ストリームが実在する必要があります. まず `read()` で `sections` 内の実際に利用可能なフィールドを確認するか, `inform` の完全なレポートを参照してください.

#### 大きなファイルの読み取りが遅いのですが?

通常ファイルはキャッシュへの全体コピーなしで直接解析されるため, 大きなファイルでもサイズに比例するコピー時間を回避できます. パイプなどランダムアクセスできないディスクリプタや直接解析に失敗する形式では一時コピーへフォールバックし, その所要時間は受信バイト数に比例します. MediaInfoLib 自体の解析時間は形式と内容に依存します.

#### 解析結果はキャッシュされますか, タイムアウト時はどうなりますか?

はい. Android 8.1 (API 27) 以降では, ID が安定し変更されていない通常ファイルについて, プラグインは現在のプロセス内でレポート, クエリ, スナップショットをキャッシュします. 上限は 32 ファイル, ファイルごとに 64 クエリ, 10 分のスライド有効期限, 合計約 2 MiB のテキストです. API 24-26 ではナノ秒精度のファイル時刻を取得できないため, 安全側に倒してキャッシュを無効にします. また低メモリ時またはプロセス終了時に消去されます. 各 AIDL 呼び出しは 30 秒で制限され, 超過するとネイティブ解析またはフォールバックコピーを協調的にキャンセルし, 一時ファイルを削除して `MEDIAINFO_TIMEOUT` を含む例外を返します.

#### 複数の音声トラックや字幕がある場合, 2 番目以降のストリームをどう読み取りますか?

スナップショットの `sections` はレポートの全セクションを保持します (複数ストリームの場合, セクション名に `audio #2` のような番号が付きます) ので, そこから直接読み取れます. `get()` は現在, 同種ストリームの 1 番目のみを検索します. ストリーム番号指定は [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md) で計画されています.

#### プラグインはネットワークにアクセスしたり機密権限を要求したりしますか?

いいえ. マニフェストにはネットワーク, ストレージ, カメラなどの機密性の高いシステム権限は含まれず, AutoJs6 との通信に必要なプラグイン権限のみを宣言します. メディア内容はホストから読み取り専用ディスクリプタとして渡され, 解析用の一時コピーは直ちに削除されます.

******

### 権限とセキュリティ

******

メディアファイルは信頼できない提供元から届く可能性があるため, 設計上, 解析には複数の防御層を設けています:

- プロセス分離: 解析はプラグイン自身のプロセスで行われ, ネイティブライブラリがホストプロセスに注入されることはないため, 解析に失敗しても AutoJs6 は正常に動作し続けます.
- 最小のデータ面: プラグイン自身は端末ストレージを読み取れず, ホストが開いた読み取り専用ファイルディスクリプタと表示名のみを受け取ります.
- 可能なら直接読み取り, フォールバック時は即削除: ランダムアクセス可能な通常ディスクリプタはメディアコピーを作成せず, 互換フォールバックだけが私有キャッシュへ書き込み, 呼び出し終了時に一時ファイルを削除します.
- 最小権限: ネットワーク, ストレージ, カメラなどの機密性の高いシステム権限を要求しません. サービスとウェイクエントリはいずれも AutoJs6 プラグイン権限 (`org.autojs.permission.PLUGIN`) で保護され, サードパーティアプリから直接呼び出せません.
- オープンで監査可能: プラグインコード, ビルドスクリプト, ドキュメント生成パイプラインはすべてオープンソースで, ネイティブライブラリと JNI ラッパーの出所はライセンスの節に明記されています.

プラグインは公式の [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) ページまたは信頼できる経路からのみ入手してください. 出所不明のパッケージは, 名前とバージョン番号が同じでも改ざんされている可能性があります.

******

### プラグインインターフェース

******

以下の情報は AutoJs6 ホストとプラグイン開発者向けです. ホストはこれらの識別子でプラグインを検出し, 機能ネゴシエーションを行います:

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

`MediainfoPluginService` は AIDL インターフェース `IMediainfoPlugin` を通じて `getInfo`/`inform`/`get`/`snapshot` の 4 メソッドを公開します. メディア内容は読み取り専用 `ParcelFileDescriptor` と表示名で渡され, `snapshot` はさらに `includeInform`/`includeSections` を含む `Bundle` オプションを受け取ります. サービスと `WakeActivity` はいずれも `org.autojs.permission.PLUGIN` 権限で保護されています.

プラグインはインストール済みの base / split APK を走査し, `libmediainfo.so` を実際に含む ABI を動的に報告します. 単一 ABI 版は該当 ABI のみ, `universal` 版は全 4 種類を報告します. APK パスを読み取れない場合は, 展開済みネイティブライブラリが存在するときに現在のプロセスのビット数へ安全にフォールバックします.

******

### 開発ロードマップ

******

プラグインの機能計画と完了状況は, マイルストーンごとに受け入れ条件付きで整理されたチェック可能なリストとして ROADMAP.md で管理されています. ストリーム番号指定, コピー不要解析, 動的 ABI 報告, ネイティブライブラリの進化, 継続的インテグレーションなどを扱います. 未チェックの項目は計画中の意向であり, 現行バージョンの機能ではありません. Issues での議論を歓迎します.

- [ROADMAP.md を見る](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### リリース履歴

******

#### v1.1.0

_2026/08/31_

- `追加` ファイル全体をコピーしない解析: seek可能な通常ファイルの記述子を /proc/self/fd 経由で MediaInfoLib が直接読み取ります; パイプまたは直接読み取りに失敗した場合だけアプリ専用の一時コピーを使います
- `追加` プロセス内結果キャッシュ: API 27 以降では完全レポート, フィールド照会, スナップショットが安定したファイル識別子, LRU, 10分のスライド有効期限, 低メモリ時の消去を共有します
- `追加` 協調的キャンセルとタイムアウト: 各 AIDL 呼び出しを30秒に制限します; 超過時はネイティブ解析または代替コピーを停止し, 一時リソースを解放して MEDIAINFO_TIMEOUT を返します
- `修正` メディアファイル全体の常時コピーを廃止し, すべてのエラーパスで記述子, ネイティブ解析器, ストリーム, 一時ファイルを確実に閉じます
- `修正` キャッシュ識別子がナノ秒単位の時刻を保持し, その情報を安全に検証できない API 24 から 26 ではキャッシュを無効にします
- `改善` 動的 ABI インベントリが実際に同梱された MediaInfoLib を検証し, 実行時レポート, メタデータ, 5種類の APK の整合性を維持します
- `改善` スナップショット解析がローカライズされたラベル, 重複グループ, 未知のフィールド, MediaInfoLib の部分出力をより堅牢に処理します
- `改善` コールドとウォーム呼び出し, 並行実行, タイムアウト, 実メディア検証に対応する再現可能なベンチマークツールを追加し, ソースマニフェストと SHA-256 要約を記録します
- `改善` 検証付きドキュメント生成が10言語を対象とし, README, 内蔵説明, 変更履歴を決定的に生成します

#### v1.0.0

_2026/07/15_

- `追加` 初の安定版: MediaInfoLib によるメディアファイル情報読み取りを AutoJs6 に提供し, コンテナ形式, コーデック, 再生時間, 解像度, ビットレート, チャンネル数などを 1 回の呼び出しで取得可能に
- `追加` スクリプト API: Node 環境は `require("mediainfo")` で非同期の `read`/`get` を, Rhino 環境はプロパティアクセス可能な解析オブジェクトを同期的に返すグローバルモジュール `mediainfo(path)` を利用可能に
- `追加` 3 つの読み取り機能: 完全なテキストレポート (`inform`), 単一パラメータ検索 (`get`), 構造化 JSON スナップショット (`snapshot`, スキーマ `autojs6-plugin-mediainfo-snapshot-v1`)
- `追加` `org.autojs.plugin.MEDIAINFO` により AutoJs6 から自動検出; プラグインは独立プロセス内で読み取り専用ファイルディスクリプタによりメディア内容を受け取って解析し, ネットワーク権限や機密性の高いシステム権限を要求しない設計に
- `追加` 4 種類の単一アーキテクチャ版 (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) と全アーキテクチャ同梱の `universal` 版を提供, リリースファイル名にバージョン, ABI, CRC32 ダイジェストを付与
- `追加` プラグイン情報, 使用説明, README, 更新履歴が 10 言語に対応: 簡体字中国語, 香港繁体字, 台湾繁体字, 英語, フランス語, スペイン語, 日本語, 韓国語, ロシア語, アラビア語

##### その他のリリース履歴

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

この節はソースからプラグインをビルドしたい開発者向けです.

debug APK をビルド:

```powershell
.\gradlew.bat :app:assembleDebug
```

release APK をビルド (ABI 分割が有効で, 4 つの単一アーキテクチャ版と 1 つの `universal` 版を一度に生成します. バージョン管理外の `sign.properties` を設定すると自動署名されます):

```powershell
.\gradlew.bat :app:assembleRelease
```

リリースアーカイブには `:app:appendDigestToReleasedFiles` タスクを実行します. `app/release` 配下の APK を `app/releases` にコピーし, `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk` 形式にリネームします.

ビルドパラメータは `version.properties` に集約されています: 最小 SDK 24 (Android 7.0), ターゲット SDK 36, 現在のバージョン 1.1.0.

******

### ローカライズとドキュメント生成

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

`strings.xml` はローカライズされたプラグイン説明とエラーメッセージを, `plugin_instruction.md` はホストのプラグインセンターに表示される使用説明を提供します. README, 更新履歴, 使用説明はすべて JSON ソースから生成されます: `.readme/` と `.changelog/` 配下のソースを編集した後, `py .python/generate_markdown.py` を実行して全成果物を再生成してください. 生成物は手動で編集しません. `py .python/generate_markdown.py --check` でソースと成果物の同期を検証できます (CI でも自動検証されます).

******

### ライセンス

******

プロジェクトコードは [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE) の下でライセンスされています. 同梱の `libmediainfo.so` は [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (MediaArea.net SARL, BSD スタイルライセンス) からビルドされ, JNI ラッパーは [MediaInfoLib-android](https://github.com/olegazyx/MediaInfoLib-android) プロジェクトに由来します.

******

### 関連リンク

******

- AutoJs6 MediaInfo ドキュメント: https://docs.autojs6.com/#/mediainfo
- MediaInfo 公式サイト: https://mediaarea.net/en/MediaInfo
- MediaInfoLib プロジェクト: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android ラッパー: https://github.com/olegazyx/MediaInfoLib-android
