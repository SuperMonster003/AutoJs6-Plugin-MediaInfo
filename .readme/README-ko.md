<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>미디어 파일 정보를 읽는 MediaInfo 플러그인</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
  </p>
</div>

******

### 언어 (Languages)

******

현재 README.md 는 다음 언어를 지원합니다:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- 한국어 [ko] # 현재
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ar.md)

******

### 소개

******

AutoJs6 MediaInfo 플러그인은 AutoJs6에 MediaInfo 기반 미디어 메타데이터 읽기 기능을 제공하며, 전체 보고서, 단일 매개변수 조회, 구조화된 JSON 스냅샷 출력을 지원합니다.

******

### 기능

******

- `mediainfo` 플러그인 서비스를 제공하며, 플러그인 ID 는 `mediainfo` 입니다.
- AutoJs6 Node 환경의 `mediainfo.read(path, options)` 및 `mediainfo.get(path, streamKind, parameter, options)` 를 지원합니다.
- `org.autojs.plugin.MEDIAINFO` 를 통한 호스트 검색 및 호출을 지원하며, 하위 AIDL 인터페이스는 `inform`/`get`/`snapshot` 을 노출합니다.
- `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 용 `libmediainfo.so` 를 포함합니다.
- 플러그인 메타데이터, 사용 설명, README 및 CHANGELOG 는 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/중국어 간체/홍콩 번체/대만 번체를 지원합니다.

******

### 사용 예시

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

경로는 호스트가 접근할 수 있는 미디어 파일을 가리켜야 하며, Node 환경에서는 작업 디렉터리 안의 상대 경로를 사용할 수 있습니다.

******

### 스트림 종류

******

지원되는 MediaInfo 스트림 종류는 다음과 같습니다:

```text
general, video, audio, text, other,
image, menu
```

`mediainfo.get()` 의 `streamKind` 는 대소문자를 구분하지 않으며 MediaInfo 네이티브 스트림 종류로 매핑됩니다.

******

### 스냅샷 옵션

******

- `includeInform`: 스냅샷에 MediaInfo 텍스트 보고서를 포함할지 여부, 기본값은 `true` 입니다.
- `includeSections`: 보고서를 파싱해 `sections` 에 기록할지 여부, 기본값은 `true` 입니다.

******

### 릴리스 기록

******

# v1.0.0

###### 2026/07/15

* `추가` 플러그인 ID `mediainfo`, 엔진 `mediainfo` 인 MediaInfo 플러그인 서비스 추가
* `추가` `org.autojs.plugin.MEDIAINFO` 를 통한 호스트 검색 및 호출 추가
* `추가` 전체 미디어 보고서, 단일 매개변수 조회, 구조화된 JSON 스냅샷을 위한 `inform`/`get`/`snapshot` 기능 추가
* `추가` `arm64-v8a`/`armeabi-v7a`/`x86_64`/`x86` 용 `libmediainfo.so` 와 `universal` APK 변형 포함
* `추가` 플러그인 런타임 정보에 지원 ABI 메타데이터를 추가하고 릴리스 APK 파일명에 버전, ABI 변형, CRC32 요약 포함
* `추가` 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/중국어 간체/홍콩 번체/대만 번체용 플러그인 메타데이터, 사용 설명, README, changelog 추가

##### 더 많은 릴리스 기록

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-ko.md)

******

### 빌드

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 빌드:

```powershell
.\gradlew.bat :app:assembleRelease
```

빌드 매개변수는 `version.properties` 에서 가져오며, 현재 최소 SDK 는 24, 대상 SDK 는 36 입니다.

******

### 리소스 구조

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

`strings.xml` 은 현지화된 플러그인 설명과 오류 메시지를 제공합니다; `plugin_instruction.md` 는 호스트에 표시되는 플러그인 사용 설명을 제공합니다. README 와 CHANGELOG 는 `.python/generate_markdown.py` 가 JSON 소스에서 생성합니다.

******

### 관련 링크

******

- AutoJs6 MediaInfo 문서: https://docs.autojs6.com/#/mediainfo
- MediaInfo 공식 프로젝트: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 공식 프로젝트: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 프로젝트: https://github.com/olegazyx/MediaInfoLib-android
