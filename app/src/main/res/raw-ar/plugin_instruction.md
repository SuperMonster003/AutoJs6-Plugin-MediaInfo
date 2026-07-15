اقرأ بيانات MediaInfo الوصفية من ملف وسائط:

```js
"nodejs";

const mediainfo = require("mediainfo");

(async () => {
  const snapshot = await mediainfo.read("sample.mp4", { includeInform: false });
  console.log(snapshot.fileName);
  console.log(await mediainfo.get("sample.mp4", "general", "Format"));
})();
```

أنواع التدفقات المدعومة هي `general`, `video`, `audio`, `text`, `other`, `image`, و `menu`.

يتحكم `includeInform` في التقرير النصي. يتحكم `includeSections` في كائن `sections` الذي تم تحليله.

لمزيد من أمثلة الاستخدام, راجع قسم [MediaInfo](https://docs.autojs6.com/#/mediainfo) في وثائق AutoJs6.
