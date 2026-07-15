미디어 파일에서 MediaInfo 메타데이터를 읽습니다:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

지원되는 스트림 종류는 `general`, `video`, `audio`, `text`, `other`, `image`, `menu` 입니다.

`includeInform` 은 텍스트 보고서를 제어합니다. `includeSections` 는 파싱된 `sections` 객체를 제어합니다.

더 많은 사용 예시는 AutoJs6 문서의 [MediaInfo](https://docs.autojs6.com/#/mediainfo) 섹션을 참고하세요.
