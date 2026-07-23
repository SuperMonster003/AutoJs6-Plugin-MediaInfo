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
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 言語 (Languages)

******

現在の README.md は次の言語をサポートしています:

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

AutoJs6 MediaInfo プラグインは AutoJs6 に MediaInfo ベースのメディアメタデータ読み取り機能を提供し, 完全なレポート, 単一パラメータ検索, 構造化 JSON スナップショットを出力できます.

******

### 機能

******

- `mediainfo` プラグインサービスを提供し, プラグイン ID は `mediainfo` です.
- AutoJs6 Node 環境の `mediainfo.read(path, options)` と `mediainfo.get(path, streamKind, parameter, options)` をサポートします.
- `org.autojs.plugin.MEDIAINFO` によるホスト検出と呼び出しをサポートし, 下位 AIDL インターフェースは `inform`/`get`/`snapshot` を公開します.
- `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 向けの `libmediainfo.so` を同梱します.
- プラグイン情報, 使用説明, README, CHANGELOG はスペイン語/フランス語/ロシア語/アラビア語/日本語/韓国語/英語/簡体字中国語/香港繁体字/台湾繁体字をサポートします.

******

### 使用例

******

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);

  const format = await mediainfo.get("sample.mp4", "general", "Format");
  console.log(format);
})();
```

パスはホストがアクセスできるメディアファイルを指す必要があります, Node 環境では作業ディレクトリ内の相対パスを使用できます.

******

### ストリーム種別

******

サポートされる MediaInfo ストリーム種別は次のとおりです:

```text
general, video, audio, text, other,
image, menu
```

`mediainfo.get()` の `streamKind` は大文字小文字を区別せず, MediaInfo のネイティブストリーム種別にマップされます.

******

### スナップショットオプション

******

- `includeInform`: スナップショットに MediaInfo テキストレポートを含めるかどうか, 既定値は `true`.
- `includeSections`: レポートを解析して `sections` に書き込むかどうか, 既定値は `true`.

******

### リリース履歴

******

# v1.0.0

###### 2026/07/15

* `追加` プラグイン ID `mediainfo`, エンジン `mediainfo` の MediaInfo プラグインサービスを追加
* `追加` `org.autojs.plugin.MEDIAINFO` によるホスト検出と呼び出しを追加
* `追加` 完全なメディアレポート, 単一パラメータ検索, 構造化 JSON スナップショット向けに `inform`/`get`/`snapshot` 機能を追加
* `追加` `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 向けの `libmediainfo.so` と `universal` APK バリアントを追加
* `追加` プラグイン実行時情報にサポート ABI メタデータを追加し, リリース APK ファイル名にバージョン, ABI バリアント, CRC32 ダイジェストを含めるように変更
* `追加` スペイン語/フランス語/ロシア語/アラビア語/日本語/韓国語/英語/簡体字中国語/香港繁体字/台湾繁体字向けにプラグイン情報, 使用説明, README, changelog をローカライズ

##### その他のリリース履歴

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release ビルド:

```powershell
.\gradlew.bat :app:assembleRelease
```

ビルドパラメータは `version.properties` から取得されます, 現在の最小 SDK は 24, ターゲット SDK は 36 です.

******

### リソース構成

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` はローカライズされたプラグイン説明とエラーメッセージを提供します; `plugin_instruction.md` はホスト側に表示されるプラグイン使用説明を提供します. README と CHANGELOG は `.python/generate_markdown.py` により JSON ソースから生成されます.

******

### 関連リンク

******

- AutoJs6 MediaInfo ドキュメント: https://docs.autojs6.com/#/mediainfo
- MediaInfo 公式プロジェクト: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 公式プロジェクト: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android プロジェクト: https://github.com/olegazyx/MediaInfoLib-android
