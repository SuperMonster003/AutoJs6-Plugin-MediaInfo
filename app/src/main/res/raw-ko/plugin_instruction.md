MediaInfo 플러그인 (MediaInfo Plugin) 은 AutoJs6 에 미디어 파일 정보 읽기 기능을 제공합니다. 설치 후 스크립트 한 줄로 동영상, 오디오, 이미지 파일의 컨테이너 형식, 코덱, 재생 시간, 해상도, 비트레이트, 채널 수 등 수백 가지 기술 정보를 얻을 수 있으며, AutoJs6 파일 목록의 미디어 정보 대화 상자도 본 플러그인이 제공하는 전체 분석 보고서를 표시합니다. 분석 능력은 데스크톱 MediaInfo 와 같은 오픈 소스 라이브러리인 MediaInfoLib 에서 나옵니다.

### 사용 방법

1. [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) 페이지에서 기기에 맞는 플러그인 APK 를 내려받아 AutoJs6 이 실행되는 기기에 설치합니다. 어떤 것을 골라야 할지 모르겠다면 `universal` 패키지를 선택하거나 아래의 `설치 패키지 선택 방법` 을 참고하세요.
2. AutoJs6 의 플러그인 센터를 열어 `MediaInfo` 플러그인이 인식되고 활성화되어 있는지 확인합니다.
3. 아래 `스크립트 API` 의 예시대로 스크립트에서 `mediainfo` 모듈을 호출합니다. AutoJs6 파일 목록에서 미디어 파일의 미디어 정보 대화 상자를 열어 전체 보고서를 바로 볼 수도 있습니다.

플러그인 센터에 플러그인이 표시되지 않으면 먼저 AutoJs6 을 최신 버전 (내부 빌드 3923 이상) 으로 업그레이드하세요. 플러그인 자체는 Android 7.0 (API 24) 이상 기기를 지원합니다.

### 스크립트 API

Node 환경 (스크립트 첫 줄에 `"nodejs"` 선언) 에서는 `require("mediainfo")` 로 모듈을 가져오며, 모든 메서드는 Promise 를 반환합니다:

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

`read(path, options?)` 는 구조화된 스냅샷 객체를 반환합니다 (아래 `스냅샷 구조와 옵션` 참고). `get(path, streamKind?, parameter)` 는 매개변수의 원본 텍스트를 반환하며 `streamKind` 의 기본값은 `general` 입니다. 보안상 Node 스크립트는 프로젝트 디렉터리 안의 파일에만 접근할 수 있고, 상대 경로는 프로젝트 루트를 기준으로 해석됩니다.

Rhino 환경 (AutoJs6 의 기본 스크립트 엔진) 에서 `mediainfo` 는 전역 모듈이며, `mediainfo(path)` 와 `mediainfo.read(path)` 는 동일하게 분석 객체를 동기적으로 반환합니다:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

반환된 객체의 `path` 와 `inform` 은 각각 해석된 경로와 전체 텍스트 보고서입니다. 각 스트림 종류 (`general`, `video`, `audio` 등) 는 분석된 필드를 노출하는 속성 (예: `mi.video.width`, 필드명은 camelCase) 이자 원본 매개변수를 실시간 조회하는 함수 (예: `mi.audio("BitRate")`) 로도 동작합니다. Rhino 스크립트는 호스트가 읽을 수 있는 모든 경로에 접근할 수 있습니다.

### 스트림 종류

`get()` 의 `streamKind` 매개변수는 다음 스트림 종류를 지원합니다:

```text
general, video, audio, text, other, image, menu
```

`streamKind` 는 대소문자를 구분하지 않으며 MediaInfo 네이티브 스트림 종류로 매핑됩니다. 존재하지 않는 스트림이나 값이 없는 매개변수를 조회하면 빈 문자열이 반환됩니다.

더 자세한 사용법과 필드 설명은 [AutoJs6 MediaInfo 문서](https://docs.autojs6.com/#/mediainfo) 와 [프로젝트 홈페이지](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo) 를 참고하세요.
