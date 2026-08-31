يضيف مكون MediaInfo الإضافي (MediaInfo Plugin) إلى AutoJs6 قدرة قراءة معلومات ملفات الوسائط. بعد التثبيت, يكفي سطر واحد من البرنامج النصي للحصول على مئات المعلمات التقنية لملفات الفيديو والصوت والصور, مثل تنسيق الحاوية والترميز والمدة والدقة ومعدل البت والقنوات; كما يعرض مربع حوار معلومات الوسائط في قائمة ملفات AutoJs6 التقرير الكامل الذي يوفره هذا المكون. تأتي قدرة التحليل من MediaInfoLib, وهي نفس المكتبة مفتوحة المصدر التي يقوم عليها تطبيق MediaInfo المكتبي.

### الاستخدام

1. نزل حزمة APK المطابقة للجهاز من صفحة [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) وثبتها على الجهاز الذي يشغل AutoJs6; وعند الشك, اختر حزمة `universal` أو راجع `اختيار حزمة APK` أدناه.
2. افتح مركز المكونات الإضافية في AutoJs6 وتأكد من أن مكون `MediaInfo` معروف ومفعل.
3. استدع الوحدة `mediainfo` في البرامج النصية كما هو موضح في `واجهة برمجة النصوص` أدناه, أو افتح مربع حوار معلومات الوسائط لأي ملف وسائط في قائمة ملفات AutoJs6 لعرض التقرير الكامل مباشرة.

إذا لم يظهر المكون في مركز المكونات الإضافية, فقم أولا بترقية AutoJs6 إلى إصدار حديث (البنية الداخلية 3923 أو أعلى). يدعم المكون نفسه الأجهزة العاملة بنظام Android 7.0 (واجهة API 24) وما فوق.

### واجهة برمجة النصوص

في بيئة Node (البرامج النصية التي تبدأ بالتوجيه `"nodejs"`), احصل على الوحدة عبر `require("mediainfo")`; وتعيد كل الطرق Promise:

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

تعيد `read(path, options?)` كائن لقطة منظم (انظر `بنية اللقطة وخياراتها` أدناه); وتعيد `get(path, streamKind?, parameter)` النص الخام للمعلمة, والقيمة الافتراضية لـ `streamKind` هي `general`. لأسباب أمنية, لا يمكن لبرامج Node النصية الوصول إلا إلى الملفات داخل دليل المشروع, وتحل المسارات النسبية اعتبارا من جذر المشروع.

في بيئة Rhino (محرك البرمجة النصية الافتراضي في AutoJs6), تكون `mediainfo` وحدة عامة; و `mediainfo(path)` و `mediainfo.read(path)` متكافئان ويعيدان كائنا محللا بشكل متزامن:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

في الكائن المعاد, يحمل `path` و `inform` المسار المحلول والتقرير النصي الكامل; ويعمل كل نوع تدفق (مثل `general` و `video` و `audio`) كخاصية تعرض الحقول المحللة (مثل `mi.video.width`, وأسماء الحقول بنمط camelCase) وكدالة للاستعلام الحي عن المعلمات الخام (مثل `mi.audio("BitRate")`). يمكن لبرامج Rhino النصية الوصول إلى أي مسار يحق للمضيف قراءته.

### أنواع التدفقات

تدعم معلمة `streamKind` في `get()` أنواع التدفقات التالية:

```text
general, video, audio, text, other, image, menu
```

معلمة `streamKind` غير حساسة لحالة الأحرف وترتبط بأنواع تدفقات MediaInfo الأصلية; والاستعلام عن تدفق غير موجود أو معلمة بلا قيمة يعيد سلسلة فارغة.

لمزيد من تفاصيل الاستخدام ومرجع الحقول, راجع [وثائق AutoJs6 MediaInfo](https://docs.autojs6.com/#/mediainfo) و [صفحة المشروع](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
