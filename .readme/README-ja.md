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

`read(path, options?)` は構造化スナップショットを返し (下記参照), `get(path, streamKind, parameter, options?)` はパラメーターの元のテキストを返します. 相対パスは作業ディレクトリを基準に解決されます. 絶対パスと親ディレクトリも使用できますが, Android がホストに読み取りを許可している必要があります. content URI ではなくファイルパスを渡してください.

Rhino 環境 (AutoJs6 の既定スクリプトエンジン) では `mediainfo` はグローバルモジュールで, `mediainfo(path)` と `mediainfo.read(path)` は等価であり, 解析オブジェクトを同期的に返します:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

返されたオブジェクトの `path` と `inform` はそれぞれ解決済みパスと完全なテキストレポートです. 各ストリーム種別 (`general`, `video`, `audio` など) は, 解析済みフィールドを公開するプロパティ (例: `mi.video.width`, フィールド名は camelCase) としても, 生パラメータをリアルタイムに問い合わせる関数 (例: `mi.audio("BitRate")`) としても機能します. Rhino スクリプトはホストが読み取り可能な任意のパスにアクセスできます.

MediaInfo クエリが 0 始まりの streamNumber, countGet によるストリーム数, 単位や説明や表示名を取得する infoKind に対応; Rhino と Node は既定の先頭ストリームの TEXT クエリを維持し, プラグインの拡張機能を確認:

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

### 動作例

******

AutoJs6 のライト/ダークテーマ画面です. Rhino の例は 8 kHz と 16 kHz の PCM 音声トラックを読み取ります. MediaInfo ボタンで開く完全なレポートには元のファイルパスが表示されます.

<table>
  <tr>
    <th>Rhino スクリプト出力</th>
    <th>メディアファイル情報</th>
    <th>MediaInfo 詳細</th>
  </tr>
  <tr>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-script.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-script.png?raw=true" alt="Rhino スクリプト出力" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-dialog.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-dialog.png?raw=true" alt="メディアファイル情報" width="260" /></picture></td>
    <td><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/dark-details.png?raw=true" /><img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/images/light-details.png?raw=true" alt="MediaInfo 詳細" width="260" /></picture></td>
  </tr>
</table>

[2 トラックのサンプル](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/androidTest/assets/mediainfo-two-audio.mka) で [デモスクリプト](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/demo/mediainfo.js) を実行するか, パスを自分のメディアファイルに変更してください. サンプルは合成した無音です. 元のパス表示と拡張クエリにはホストとプラグインの対応する更新が必要です.

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

#### Node スクリプトで `path must stay inside the scoped working directory` と表示される?

AutoJs6 と Node Runtime プラグインを両方更新してください. 現在のバージョンは Android のファイル権限に従い, プロジェクト外の通常のファイルパスにも対応しています. 古いホストやランタイムには従来のプロジェクト内限定の制限が残っている場合があります.

#### `get()` が空文字列を返しました?

パラメータ名は MediaInfo のネイティブパラメータ (`Format`, `Duration`, `Width`, `BitRate`, `FileSize` など) を使用し, 対象ストリームが実在する必要があります. まず `read()` で `sections` 内の実際に利用可能なフィールドを確認するか, `inform` の完全なレポートを参照してください.

#### 大きなファイルの読み取りが遅いのですが?

通常ファイルはキャッシュへの全体コピーなしで直接解析されるため, 大きなファイルでもサイズに比例するコピー時間を回避できます. パイプなどランダムアクセスできないディスクリプタや直接解析に失敗する形式では一時コピーへフォールバックし, その所要時間は受信バイト数に比例します. MediaInfoLib 自体の解析時間は形式と内容に依存します.

#### 解析結果はキャッシュされますか, タイムアウト時はどうなりますか?

はい. Android 8.1 (API 27) 以降では, ID が安定し変更されていない通常ファイルについて, プラグインは現在のプロセス内でレポート, クエリ, スナップショットをキャッシュします. 上限は 32 ファイル, ファイルごとに 64 クエリ, 10 分のスライド有効期限, 合計約 2 MiB のテキストです. API 24-26 ではナノ秒精度のファイル時刻を取得できないため, 安全側に倒してキャッシュを無効にします. また低メモリ時またはプロセス終了時に消去されます. 各 AIDL 呼び出しは 30 秒で制限され, 超過するとネイティブ解析またはフォールバックコピーを協調的にキャンセルし, 一時ファイルを削除して `MEDIAINFO_TIMEOUT` を含む例外を返します.

#### 複数の音声トラックや字幕がある場合, 2 番目以降のストリームをどう読み取りますか?

ホストと Node を更新すると, get(path, "audio", "Format", {streamNumber: 1}) で 2 番目の音声を取得できます. countGet(path, "audio") はストリーム数, infoKind: "MEASURE" は単位を返します. capabilities() と [クエリ仕様](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_QUERY.md) を確認してください.

#### プラグインはネットワークにアクセスしたり機密権限を要求したりしますか?

いいえ. マニフェストにはネットワーク, ストレージ, カメラなどの機密性の高いシステム権限は含まれず, AutoJs6 との通信に必要なプラグイン権限のみを宣言します. メディア内容はホストから読み取り専用ディスクリプタとして渡され, 解析用の一時コピーは直ちに削除されます.

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

`MediainfoPluginService` は AIDL インターフェース `IMediainfoPlugin` を通じて `getInfo`/`inform`/`get`/`snapshot`/`getDetail`/`countGet` の 6 メソッドを公開します. メディア内容は読み取り専用 `ParcelFileDescriptor` と表示名で渡され, `snapshot` はさらに `includeInform`/`includeSections` を含む `Bundle` オプションを受け取ります. サービスと `WakeActivity` はいずれも `org.autojs.permission.PLUGIN` 権限で保護されています.

メディア解析には同梱の MediaInfoLib ネイティブライブラリを使用します.

******

### 開発ロードマップ

******

プラグインの機能計画と完了状況は, マイルストーンごとに受け入れ条件付きで整理されたチェック可能なリストとして ROADMAP.md で管理されています. ストリーム番号指定, コピー不要解析, 動的 ABI 報告, ネイティブライブラリの進化, 継続的インテグレーションなどを扱います. 未チェックの項目は計画中の意向であり, 現行バージョンの機能ではありません. Issues での議論を歓迎します.

- [ROADMAP.md を見る](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### リリース履歴

******

#### v2.1.2

_2026/09/13_

- `修正` 公開用 APK を現在のビルドから収集し, バージョンと署名を確認して旧 APK に新バージョン名が付く問題を防止
- `改善` ホストからの有効化, メタデータ, 多言語文書および署名済み APK の収集を共通規約に統一

#### v2.1.1

_2026/09/11_

- `改善` 64 ビットのネイティブライブラリの 16 KB ページアラインメントをビルド時に検証, manifest 契約の検査と JSON レポートに対応

#### v2.1.0

_2026/09/10_

- `追加` MediaInfo クエリが 0 始まりの streamNumber, countGet によるストリーム数, 単位や説明や表示名を取得する infoKind に対応; Rhino と Node は既定の先頭ストリームの TEXT クエリを維持し, プラグインの拡張機能を確認
- `追加` 明示的に選択する snapshot v2 はネイティブ JSON の同種ストリームを配列にまとめてエンジンバージョンを提供し, 既定は snapshot v1 を維持
- `修正` MediaInfo 詳細とスナップショットの Complete name に元のファイルパスを表示し, プライベートキャッシュや記述子のパスを表示しないよう改善; スナップショットの表示ファイル名は維持
- `改善` Node のファイルパス説明を更新し, 実行可能な 2 トラックのデモとライト/ダークテーマの画面画像を追加

##### その他のリリース履歴

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

この節はソースからプラグインをビルドしたい開発者向けです.

ビルド前にリポジトリと固定された2つの公式サブモジュールを再帰的に取得します:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

debug APK をビルド:

```powershell
.\gradlew.bat :app:assembleDebug
```

release APK をビルド:

```powershell
.\gradlew.bat :app:assembleRelease
```

リリースアーカイブには `:app:appendDigestToReleasedFiles` タスクを実行します. `app/release` 配下の APK を `app/releases` にコピーし, `autojs6-plugin-mediainfo-v2.1.2-<abi>-<crc32>.apk` 形式にリネームします.

ビルドパラメータは `version.properties` に集約されています: 最小 SDK 24 (Android 7.0), ターゲット SDK 36, 現在のバージョン 2.1.2.

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

プロジェクトコードは [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE) の下でライセンスされています. v2 のソースビルドでは, `libmediainfo.so` を公式 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) と [ZenLib](https://github.com/MediaArea/ZenLib) (zlib ライセンス) からビルドし, 互換 JNI ブリッジはこのリポジトリで保守します. 凍結された v1.1.0 バイナリの来歴は別途記録されています.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### 関連リンク

******

- AutoJs6 MediaInfo ドキュメント: https://docs.autojs6.com/#/mediainfo
- MediaInfo 公式サイト: https://mediaarea.net/en/MediaInfo
- MediaInfoLib プロジェクト: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android ラッパー: https://github.com/olegazyx/MediaInfoLib-android


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/docs/16kb.md)
