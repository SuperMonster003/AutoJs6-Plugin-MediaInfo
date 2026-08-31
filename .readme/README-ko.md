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
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
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

MediaInfo 플러그인 (MediaInfo Plugin) 은 AutoJs6 에 미디어 파일 정보 읽기 기능을 제공합니다. 설치 후 스크립트 한 줄로 동영상, 오디오, 이미지 파일의 컨테이너 형식, 코덱, 재생 시간, 해상도, 비트레이트, 채널 수 등 수백 가지 기술 정보를 얻을 수 있으며, AutoJs6 파일 목록의 미디어 정보 대화 상자도 본 플러그인이 제공하는 전체 분석 보고서를 표시합니다. 분석 능력은 데스크톱 MediaInfo 와 같은 오픈 소스 라이브러리인 MediaInfoLib 에서 나옵니다.

플러그인은 독립된 프로세스에서 실행되며 AutoJs6 이 자동으로 발견하므로 별도의 설정이 필요 없습니다. 파일을 읽을 때 호스트는 미디어 내용을 읽기 전용 파일 디스크립터로 전달합니다. 임의 접근이 가능한 일반 파일은 MediaInfoLib 가 `/proc/self/fd` 를 통해 직접 읽고, 파이프처럼 임의 접근이 불가능한 디스크립터나 직접 분석 실패 시에만 전용 캐시의 임시 복사본으로 대체한 뒤 호출 종료 시 즉시 삭제합니다. 전 과정에서 네트워크를 사용하지 않으며 민감한 시스템 권한도 요청하지 않습니다.

******

### 기능 하이라이트

******

- 바로 사용 가능: 설정이 필요 없으며, AutoJs6 이 플러그인을 자동으로 발견하여 스크립트와 파일 목록의 미디어 정보 대화 상자에서 즉시 사용할 수 있습니다.
- 풍부한 정보: 컨테이너 형식, 코덱, 재생 시간, 해상도, 프레임 레이트, 비트레이트, 채널 수, 샘플링 레이트 등 기술 정보를 한 번의 호출로 모두 얻습니다.
- 세 가지 읽기 방식: 전체 텍스트 보고서 (`inform`), 단일 매개변수 조회 (`get`), 구조화된 JSON 스냅샷 (`read`/`snapshot`) 을 필요에 따라 선택합니다.
- 두 가지 스크립트 엔진 지원: Node 환경은 `require("mediainfo")` 로 비동기 호출하고, Rhino 환경은 전역 모듈 `mediainfo(path)` 가 속성으로 접근 가능한 분석 객체를 동기적으로 반환합니다.
- 넓은 형식 범위: 분석은 데스크톱 MediaInfo 와 동일한 MediaInfoLib 이 담당하며, 흔한 형식부터 희귀한 형식까지 수많은 동영상, 오디오, 이미지 형식을 지원합니다.
- 다섯 가지 설치 패키지: 단일 아키텍처 패키지 4 종 (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) 과 모든 아키텍처를 포함한 `universal` 패키지를 기기에 맞게 선택할 수 있습니다.
- 프라이버시 친화적: 분석은 격리된 프로세스에서 이루어지고 호스트가 전달하는 읽기 전용 파일 디스크립터만 받으며, 네트워크나 민감한 시스템 권한을 요청하지 않습니다.
- 다국어: 플러그인 정보, 사용 설명, README, 변경 로그가 10 개 언어로 제공됩니다.

******

### 사용 방법

******

1. [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 페이지에서 기기에 맞는 플러그인 APK 를 내려받아 AutoJs6 이 실행되는 기기에 설치합니다. 어떤 것을 골라야 할지 모르겠다면 `universal` 패키지를 선택하거나 아래의 `설치 패키지 선택 방법` 을 참고하세요.
2. AutoJs6 의 플러그인 센터를 열어 `MediaInfo` 플러그인이 인식되고 활성화되어 있는지 확인합니다.
3. 아래 `스크립트 API` 의 예시대로 스크립트에서 `mediainfo` 모듈을 호출합니다. AutoJs6 파일 목록에서 미디어 파일의 미디어 정보 대화 상자를 열어 전체 보고서를 바로 볼 수도 있습니다.

> 플러그인 센터에 플러그인이 표시되지 않으면 먼저 AutoJs6 을 최신 버전 (내부 빌드 3923 이상) 으로 업그레이드하세요. 플러그인 자체는 Android 7.0 (API 24) 이상 기기를 지원합니다.

******

### 설치 패키지 선택 방법

******

각 릴리스에는 5 개의 APK 가 포함되며, 차이는 내장된 네이티브 라이브러리 아키텍처뿐입니다:

| 패키지 | 적합한 대상 |
|---|---|
| `arm64-v8a` | 대부분의 최신 Android 휴대폰과 태블릿 (64 비트 ARM), 우선 선택 |
| `armeabi-v7a` | 비교적 오래된 32 비트 ARM 기기 |
| `x86_64` | 64 비트 x86 에뮬레이터와 일부 x86 기기 |
| `x86` | 32 비트 x86 에뮬레이터와 일부 x86 기기 |
| `universal` | 4 개 아키텍처를 모두 포함하여 가장 크며, 모든 기기에서 동작하고 확신이 없을 때의 안전한 선택 |

플러그인은 자체 프로세스 안에서 네이티브 라이브러리를 로드합니다. 기기 아키텍처와 맞지 않는 단일 아키텍처 패키지를 잘못 설치하면 호출 시 `MediaInfo library is not available` 오류가 발생하며, `universal` 패키지로 교체하면 해결됩니다.

******

### 스크립트 API

******

Node 환경 (스크립트 첫 줄에 `"nodejs"` 선언) 에서는 `require("mediainfo")` 로 모듈을 가져오며, 모든 메서드는 Promise 를 반환합니다:

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

`read(path, options?)` 는 구조화된 스냅샷 객체를 반환합니다 (아래 `스냅샷 구조와 옵션` 참고). `get(path, streamKind?, parameter)` 는 매개변수의 원본 텍스트를 반환하며 `streamKind` 의 기본값은 `general` 입니다. 보안상 Node 스크립트는 프로젝트 디렉터리 안의 파일에만 접근할 수 있고, 상대 경로는 프로젝트 루트를 기준으로 해석됩니다.

Rhino 환경 (AutoJs6 의 기본 스크립트 엔진) 에서 `mediainfo` 는 전역 모듈이며, `mediainfo(path)` 와 `mediainfo.read(path)` 는 동일하게 분석 객체를 동기적으로 반환합니다:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

반환된 객체의 `path` 와 `inform` 은 각각 해석된 경로와 전체 텍스트 보고서입니다. 각 스트림 종류 (`general`, `video`, `audio` 등) 는 분석된 필드를 노출하는 속성 (예: `mi.video.width`, 필드명은 camelCase) 이자 원본 매개변수를 실시간 조회하는 함수 (예: `mi.audio("BitRate")`) 로도 동작합니다. Rhino 스크립트는 호스트가 읽을 수 있는 모든 경로에 접근할 수 있습니다.

******

### 스냅샷 구조와 옵션

******

Node 환경에서 `read()` 가 반환하는 스냅샷 구조는 다음과 같습니다 (플러그인 AIDL `snapshot` 메서드가 반환하는 JSON 도 동일하지만 `schema` 가 `autojs6-plugin-mediainfo-snapshot-v1` 이고 `path` 필드가 없습니다):

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

- `includeInform`: `inform` 텍스트 보고서 포함 여부, 기본값 `true`. `false` 로 설정하면 빈 문자열이 되어 반환 크기를 줄일 수 있습니다.
- `includeSections`: 보고서를 분석해 `sections` 를 생성할지 여부, 기본값 `true`. `false` 로 설정하면 빈 객체가 됩니다.

`sections` 는 보고서 섹션 이름의 소문자 형태를 키로 사용하며 (같은 종류의 스트림이 여러 개면 `audio #1` 처럼 번호가 붙을 수 있습니다), 값은 모두 객체 배열입니다. 필드명은 camelCase 로 변환되고, 필드 값은 단위와 `1 920 pixels` 같은 천 단위 구분 공백을 포함한 MediaInfo 원본 텍스트를 유지합니다.

******

### 스트림 종류

******

`get()` 의 `streamKind` 매개변수는 다음 스트림 종류를 지원합니다:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 는 대소문자를 구분하지 않으며 MediaInfo 네이티브 스트림 종류로 매핑됩니다. 존재하지 않는 스트림이나 값이 없는 매개변수를 조회하면 빈 문자열이 반환됩니다.

******

### 자주 묻는 질문

******

#### 플러그인이 정상 동작하는지 어떻게 확인하나요?

AutoJs6 의 플러그인 센터를 열어 `MediaInfo` 플러그인이 보이면 호스트가 인식한 것입니다. 이후 아무 `mediainfo` 스크립트 예시나 실행해 정상적으로 결과가 반환되면 플러그인이 잘 동작하는 것입니다.

#### 앱 목록에 플러그인 아이콘이 없는 이유는 무엇인가요?

정상입니다. 플러그인은 독립된 화면이 없고 런처 아이콘도 만들지 않습니다. 설치 후 AutoJs6 이 백그라운드에서 자동으로 발견하고 호출하며, 모든 상호작용은 AutoJs6 안에서 이루어집니다.

#### Node 스크립트에서 `path must stay inside the scoped working directory` 오류가 발생합니다?

Node 엔진은 보안상 프로젝트 디렉터리 안의 파일만 접근을 허용합니다. 미디어 파일을 프로젝트 디렉터리로 옮기거나 복사한 뒤 읽어 주세요. 다른 경로 (갤러리나 다운로드 폴더 등) 에 접근해야 한다면 Rhino 엔진 스크립트를 사용하세요.

#### `get()` 이 빈 문자열을 반환했습니다?

매개변수 이름은 MediaInfo 네이티브 매개변수 (`Format`, `Duration`, `Width`, `BitRate`, `FileSize` 등) 여야 하고, 대상 스트림이 실제로 존재해야 합니다. 먼저 `read()` 로 `sections` 에서 실제 사용 가능한 필드를 확인하거나 `inform` 전체 보고서를 살펴보세요.

#### 큰 파일을 읽을 때 느립니다?

일반 파일은 이제 전체 캐시 복사 없이 직접 분석되므로 큰 파일에서 크기에 비례하는 복사 시간을 피할 수 있습니다. 파이프처럼 임의 접근이 불가능한 디스크립터나 직접 분석에 실패한 형식은 여전히 임시 복사본을 사용하며 이 대체 경로의 비용은 수신 바이트 수에 비례합니다. MediaInfoLib 자체 분석 시간은 형식과 내용에 따라 달라집니다.

#### 분석 결과는 캐시되며 시간 초과 시에는 어떻게 되나요?

예. Android 8.1 (API 27) 이상에서는 식별자가 안정적이고 변경되지 않은 일반 파일의 보고서, 쿼리, 스냅샷을 현재 프로세스에 캐시합니다. 한도는 최대 32개 파일, 파일당 64개 쿼리, 10분 슬라이딩 유효 기간, 총 약 2 MiB 텍스트입니다. 나노초 정밀도의 파일 타임스탬프가 없는 API 24-26에서는 보수적으로 캐시를 비활성화하며, 메모리 부족이나 프로세스 종료 시에도 캐시를 비웁니다. 각 AIDL 호출은 30초로 제한되며, 초과 시 네이티브 분석 또는 대체 복사를 협력적으로 취소하고 임시 파일을 삭제한 뒤 `MEDIAINFO_TIMEOUT` 이 포함된 예외를 반환합니다.

#### 파일에 오디오 트랙이나 자막이 여러 개인데, 두 번째 이후 스트림은 어떻게 읽나요?

스냅샷의 `sections` 는 보고서의 모든 섹션을 보존하므로 (스트림이 여러 개면 섹션 이름에 `audio #2` 처럼 번호가 붙습니다) 거기서 바로 읽으면 됩니다. `get()` 은 현재 같은 종류 스트림 중 첫 번째만 조회하며, 스트림 번호 지정 기능은 [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md) 에 계획되어 있습니다.

#### 플러그인이 네트워크에 접속하거나 민감한 권한을 요청하나요?

아니요. 매니페스트에는 네트워크, 저장소, 카메라 등 민감한 시스템 권한이 없으며, AutoJs6 과 통신하는 데 필요한 플러그인 권한만 선언합니다. 미디어 내용은 호스트가 읽기 전용 디스크립터로 전달하고, 분석용 임시 사본은 즉시 삭제됩니다.

******

### 권한과 보안

******

미디어 파일은 신뢰할 수 없는 출처에서 올 수 있으므로, 설계상 분석 과정에 여러 방어선을 두었습니다:

- 프로세스 격리: 분석은 플러그인 자체 프로세스에서 이루어지고 네이티브 라이브러리는 호스트 프로세스에 주입되지 않으므로, 분석이 실패해도 AutoJs6 은 정상 동작합니다.
- 최소 데이터 노출: 플러그인은 스스로 기기 저장소를 읽을 수 없으며, 호스트가 연 읽기 전용 파일 디스크립터와 표시 이름만 받습니다.
- 가능하면 직접 읽고 대체 시 즉시 삭제: 임의 접근 가능한 일반 디스크립터는 미디어 복사본을 만들지 않으며, 호환성 대체 경로만 전용 캐시에 기록한 뒤 호출 종료 시 임시 파일을 삭제합니다.
- 최소 권한: 네트워크, 저장소, 카메라 등 민감한 시스템 권한을 요청하지 않습니다. 서비스와 웨이크 진입점은 모두 AutoJs6 플러그인 권한 (`org.autojs.permission.PLUGIN`) 으로 보호되어 서드파티 앱이 직접 호출할 수 없습니다.
- 공개적이고 감사 가능: 플러그인 코드, 빌드 스크립트, 문서 생성 파이프라인이 모두 오픈 소스이며, 네이티브 라이브러리와 JNI 래퍼의 출처는 라이선스 절에 명시되어 있습니다.

플러그인은 공식 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 페이지나 신뢰할 수 있는 경로에서만 설치하세요. 출처가 불분명한 패키지는 이름과 버전이 같아 보여도 변조되었을 수 있습니다.

******

### 플러그인 인터페이스

******

다음 정보는 AutoJs6 호스트와 플러그인 개발자를 위한 것으로, 호스트는 이 식별자들로 플러그인을 발견하고 기능을 협상합니다:

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

`MediainfoPluginService` 는 AIDL 인터페이스 `IMediainfoPlugin` 을 통해 `getInfo`/`inform`/`get`/`snapshot` 네 가지 메서드를 노출합니다. 미디어 내용은 읽기 전용 `ParcelFileDescriptor` 와 표시 이름으로 전달되며, `snapshot` 은 추가로 `includeInform`/`includeSections` 를 담은 `Bundle` 옵션을 받습니다. 서비스와 `WakeActivity` 모두 `org.autojs.permission.PLUGIN` 권한으로 보호됩니다.

플러그인은 설치된 base / split APK 를 검사하여 실제로 `libmediainfo.so` 를 포함한 ABI 를 동적으로 보고합니다. 단일 ABI 패키지는 해당 ABI 만, `universal` 패키지는 4 개 모두를 보고합니다. APK 경로를 읽을 수 없으면 추출된 네이티브 라이브러리가 있을 때 현재 프로세스 비트 수를 기준으로 안전하게 대체합니다.

******

### 개발 로드맵

******

플러그인의 기능 계획과 완료 현황은 ROADMAP.md 에서 체크 가능한 목록으로 관리되며, 마일스톤별로 정리되고 수용 기준이 첨부되어 있습니다. 스트림 번호 조회, 복사 없는 분석, 동적 ABI 보고, 네이티브 라이브러리 진화, 지속적 통합 등을 다룹니다. 체크되지 않은 항목은 계획 의도일 뿐 현재 버전의 기능이 아니며, Issues 를 통한 토론을 환영합니다.

- [ROADMAP.md 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### 릴리스 기록

******

#### v2.0.0

_2026/08/31_

- `추가` 공식 소스 빌드: 고정된 MediaArea MediaInfoLib 26.05와 ZenLib 0.4.41 소스에서 4개 ABI를 직접 생성하며 오래된 개인 저장소의 사전 빌드 라이브러리를 제거
- `추가` 재현 가능한 출처: 업스트림 태그, 전체 커밋, NDK / CMake 설정, 라이선스 원문을 잠금 파일과 모든 APK에 기록하고 ELF와 5개 APK를 자동 감사
- `추가` 안정 버전 추적: 매주 또는 수동으로 공식 Release를 확인하고 고정 버전 업데이트를 Draft PR로만 제안하며 태그 이동을 감지하고 자동 병합이나 배포는 수행하지 않음
- `수정` R8 처리 후에도 정확한 JNI 래퍼 클래스와 메서드를 유지하고 실제 minified Release APK를 설치하는 공개 AIDL 스모크 테스트를 추가하여 릴리스 빌드의 네이티브 로드 실패를 방지
- `개선` MediaInfoLib 26.05가 코덱, HDR / 색상, 체크섬, 표지 이미지 메타데이터를 확장하면서 공개 AIDL 및 `autojs6-plugin-mediainfo-snapshot-v1` 계약을 유지
- `개선` 모든 ABI가 16 KB page size를 지원하고 API 24-37, x86 / x86_64, ARM32 / ARM64, 시간 제한, 캐시, 실제 미디어, 초대형 파일 게이트를 통과
- `개선` 동일한 실제 샘플에서 0.7.83과 26.05의 전체 보고서, 필드 조회, sections 차이를 검토함; 컨테이너와 핵심 스트림은 호환되며 필드 텍스트는 업스트림 분석 결과를 따름
- `의존성` 동결된 네이티브 파서를 MediaInfoLib 0.7.83에서 26.05로 업그레이드하고 ZenLib 0.4.41 및 Android NDK 29.0.14206865를 고정

#### v1.1.0

_2026/08/31_

- `추가` 전체 파일 복사 없는 분석: 탐색 가능한 일반 파일 설명자를 /proc/self/fd 경로로 MediaInfoLib가 직접 읽습니다; 파이프이거나 직접 읽기에 실패한 경우에만 앱 전용 임시 복사본을 사용합니다
- `추가` 프로세스 내 결과 캐시: API 27 이상에서 전체 보고서, 필드 조회, 스냅샷이 안정적인 파일 식별자, LRU, 10분 슬라이딩 만료, 메모리 부족 시 정리를 공유합니다
- `추가` 협력적 취소와 시간 제한: 각 AIDL 호출은 30초로 제한됩니다; 초과하면 네이티브 분석 또는 대체 복사를 중단하고 임시 리소스를 해제한 뒤 MEDIAINFO_TIMEOUT을 반환합니다
- `수정` 미디어 파일 전체를 항상 복사하던 동작을 제거하고 모든 오류 경로에서 설명자, 네이티브 분석기, 스트림, 임시 파일을 확실히 닫습니다
- `수정` 캐시 식별자가 나노초 타임스탬프를 보존하며 해당 정보를 안전하게 검증할 수 없는 API 24에서 26까지는 캐시를 비활성화합니다
- `개선` 동적 ABI 인벤토리가 실제로 패키징된 MediaInfoLib 라이브러리를 확인하고 런타임 보고서, 메타데이터, 5개 APK 변형의 일관성을 유지합니다
- `개선` 스냅샷 파서가 현지화된 레이블, 반복 그룹, 알 수 없는 필드, MediaInfoLib의 부분 출력을 더 견고하게 처리합니다
- `개선` 콜드와 웜 호출, 동시성, 시간 제한, 실제 미디어 검증을 위한 재현 가능한 벤치마크 도구를 추가하고 소스 매니페스트와 SHA-256 요약을 기록합니다
- `개선` 검증된 문서 생성이 이제 10개 언어를 지원하며 README, 내장 지침, 변경 기록을 결정적으로 생성합니다

#### v1.0.0

_2026/07/15_

- `추가` 첫 안정 버전: MediaInfoLib 기반 미디어 파일 정보 읽기를 AutoJs6 에 제공하여, 한 번의 호출로 컨테이너 형식, 코덱, 재생 시간, 해상도, 비트레이트, 채널 수 등을 얻을 수 있음
- `추가` 스크립트 API: Node 환경은 `require("mediainfo")` 로 비동기 `read`/`get` 을, Rhino 환경은 속성 접근 가능한 분석 객체를 동기적으로 반환하는 전역 모듈 `mediainfo(path)` 를 사용 가능
- `추가` 세 가지 읽기 기능: 전체 텍스트 보고서 (`inform`), 단일 매개변수 조회 (`get`), 구조화된 JSON 스냅샷 (`snapshot`, 스키마 `autojs6-plugin-mediainfo-snapshot-v1`)
- `추가` `org.autojs.plugin.MEDIAINFO` 를 통해 AutoJs6 이 자동 발견; 플러그인은 독립 프로세스에서 읽기 전용 파일 디스크립터로 미디어 내용을 받아 분석하며, 네트워크나 민감한 시스템 권한을 요청하지 않음
- `추가` 단일 아키텍처 패키지 4 종 (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) 과 모든 아키텍처를 포함한 `universal` 패키지 제공, 릴리스 파일명에 버전, ABI, CRC32 요약 포함
- `추가` 플러그인 정보, 사용 설명, README, 변경 로그가 10 개 언어 지원: 중국어 간체, 홍콩 번체, 대만 번체, 영어, 프랑스어, 스페인어, 일본어, 한국어, 러시아어, 아랍어

##### 더 많은 릴리스 기록

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ko.md)

******

### 빌드

******

이 절은 소스에서 플러그인을 빌드하려는 개발자를 위한 것입니다.

빌드 전에 저장소와 고정된 두 공식 하위 모듈을 재귀적으로 가져옵니다:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

debug APK 빌드:

```powershell
.\gradlew.bat :app:assembleDebug
```

release APK 빌드 (ABI 분할이 활성화되어 단일 아키텍처 패키지 4 개와 `universal` 패키지 1 개를 한 번에 생성합니다. 버전 관리에서 제외된 `sign.properties` 를 설정하면 자동 서명됩니다):

```powershell
.\gradlew.bat :app:assembleRelease
```

릴리스 보관을 위해 `:app:appendDigestToReleasedFiles` 작업을 실행하면 `app/release` 아래의 APK 를 `app/releases` 로 복사하고 `autojs6-plugin-mediainfo-v2.0.0-<abi>-<crc32>.apk` 형식으로 이름을 바꿉니다.

빌드 매개변수는 `version.properties` 에 집중되어 있습니다: 최소 SDK 24 (Android 7.0), 대상 SDK 36, 현재 버전 2.0.0.

******

### 현지화와 문서 생성

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

`strings.xml` 은 현지화된 플러그인 설명과 오류 메시지를, `plugin_instruction.md` 는 호스트 플러그인 센터에 표시되는 사용 설명을 제공합니다. README, 변경 로그, 사용 설명은 모두 JSON 소스에서 생성됩니다: `.readme/` 와 `.changelog/` 아래의 소스를 수정한 뒤 `py .python/generate_markdown.py` 를 실행해 모든 산출물을 다시 생성하세요. 생성된 산출물은 손으로 편집하지 않습니다. `py .python/generate_markdown.py --check` 로 소스와 산출물의 동기화를 검증할 수 있습니다 (CI 도 자동으로 검사합니다).

******

### 라이선스

******

프로젝트 코드는 [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE) 라이선스를 따릅니다. v2 소스 빌드에서는 공식 [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) 와 [ZenLib](https://github.com/MediaArea/ZenLib) (zlib 라이선스) 에서 `libmediainfo.so` 를 빌드하며, 호환 JNI 브리지는 이 저장소에서 유지합니다. 동결된 v1.1.0 바이너리의 출처는 별도로 기록됩니다.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### 관련 링크

******

- AutoJs6 MediaInfo 문서: https://docs.autojs6.com/#/mediainfo
- MediaInfo 공식 웹사이트: https://mediaarea.net/en/MediaInfo
- MediaInfoLib 프로젝트: https://github.com/MediaArea/MediaInfoLib
- MediaInfoLib Android 래퍼: https://github.com/olegazyx/MediaInfoLib-android
