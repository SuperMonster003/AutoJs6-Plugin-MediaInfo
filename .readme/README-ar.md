<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-mediainfo-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>مكون MediaInfo الإضافي لقراءة معلومات ملفات الوسائط</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-MediaInfo?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### اللغات (Languages)

******

يدعم README.md الحالي اللغات التالية:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.readme/README-ru.md)
- العربية [ar] # الحالية

******

### مقدمة

******

يضيف مكون MediaInfo الإضافي (MediaInfo Plugin) إلى AutoJs6 قدرة قراءة معلومات ملفات الوسائط. بعد التثبيت, يكفي سطر واحد من البرنامج النصي للحصول على مئات المعلمات التقنية لملفات الفيديو والصوت والصور, مثل تنسيق الحاوية والترميز والمدة والدقة ومعدل البت والقنوات; كما يعرض مربع حوار معلومات الوسائط في قائمة ملفات AutoJs6 التقرير الكامل الذي يوفره هذا المكون. تأتي قدرة التحليل من MediaInfoLib, وهي نفس المكتبة مفتوحة المصدر التي يقوم عليها تطبيق MediaInfo المكتبي.

يعمل المكون الإضافي في عملية مستقلة ويكتشفه AutoJs6 تلقائيا دون أي إعداد. عند قراءة ملف, يسلم المضيف محتوى الوسائط كواصف ملف للقراءة فقط. يقرأ MediaInfoLib واصفات الملفات العادية القابلة للوصول العشوائي مباشرة عبر `/proc/self/fd`; أما الواصفات غير القابلة لذلك مثل الأنابيب, أو فشل التحليل المباشر, فتستخدم نسخة مؤقتة في التخزين الخاص وتحذف فور انتهاء الاستدعاء. لا يستخدم الشبكة في أي مرحلة, ولا يطلب أي إذن نظام حساس.

******

### أبرز الميزات

******

- جاهز للاستخدام فورا: لا حاجة لأي إعداد; يكتشف AutoJs6 المكون تلقائيا, ويمكن للبرامج النصية ومربع حوار معلومات الوسائط في قائمة الملفات استخدامه مباشرة.
- معلومات شاملة: تنسيق الحاوية, والترميز, والمدة, والدقة, ومعدل الإطارات, ومعدل البت, والقنوات, ومعدل أخذ العينات وغيرها, كلها في استدعاء واحد.
- ثلاث طرق للقراءة: تقرير نصي كامل (`inform`), واستعلام معلمة واحدة (`get`), ولقطة JSON منظمة (`read`/`snapshot`), حسب الحاجة.
- محركا برمجة نصية: بيئة Node تستدعيه بشكل غير متزامن عبر `require("mediainfo")`; وبيئة Rhino تستخدم الوحدة العامة `mediainfo(path)` التي تعيد بشكل متزامن كائنا محللا يمكن الوصول إلى خصائصه.
- تغطية واسعة للتنسيقات: التحليل تقوم به MediaInfoLib, مكتبة MediaInfo المكتبي, وتدعم عددا كبيرا من تنسيقات الفيديو والصوت والصور الشائعة والنادرة.
- خمس حزم APK: أربع حزم أحادية البنية (`arm64-v8a` و `armeabi-v7a` و `x86` و `x86_64`) بالإضافة إلى حزمة `universal` الشاملة, ليثبت كل جهاز ما يحتاجه فقط.
- صديق للخصوصية: يجري التحليل في عملية معزولة لا تتلقى من المضيف سوى واصفات ملفات للقراءة فقط, دون أذونات شبكة أو أذونات نظام حساسة.
- متعدد اللغات: بيانات المكون الإضافي, والتعليمات, و README, وسجل التغييرات متوفرة بعشر لغات.

******

### الاستخدام

******

1. نزل حزمة APK المطابقة للجهاز من صفحة [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) وثبتها على الجهاز الذي يشغل AutoJs6; وعند الشك, اختر حزمة `universal` أو راجع `اختيار حزمة APK` أدناه.
2. افتح مركز المكونات الإضافية في AutoJs6 وتأكد من أن مكون `MediaInfo` معروف ومفعل.
3. استدع الوحدة `mediainfo` في البرامج النصية كما هو موضح في `واجهة برمجة النصوص` أدناه, أو افتح مربع حوار معلومات الوسائط لأي ملف وسائط في قائمة ملفات AutoJs6 لعرض التقرير الكامل مباشرة.

> إذا لم يظهر المكون في مركز المكونات الإضافية, فقم أولا بترقية AutoJs6 إلى إصدار حديث (البنية الداخلية 3923 أو أعلى). يدعم المكون نفسه الأجهزة العاملة بنظام Android 7.0 (واجهة API 24) وما فوق.

******

### اختيار حزمة APK

******

يتضمن كل إصدار 5 حزم APK لا تختلف إلا في بنى المكتبة الأصلية المضمنة:

| الحزمة | الأنسب لـ |
|---|---|
| `arm64-v8a` | الغالبية العظمى من هواتف وأجهزة Android الحديثة (ARM بمعمارية 64 بت); الخيار الأول |
| `armeabi-v7a` | أجهزة ARM الأقدم بمعمارية 32 بت |
| `x86_64` | محاكيات x86 بمعمارية 64 بت وقلة من أجهزة x86 |
| `x86` | محاكيات x86 بمعمارية 32 بت وقلة من أجهزة x86 |
| `universal` | تضم البنى الأربع كلها وهي الأكبر حجما; تعمل على أي جهاز وهي الخيار الآمن عند الشك |

يحمل المكون الإضافي المكتبة الأصلية داخل عمليته الخاصة. إذا ثبتت بالخطأ حزمة أحادية البنية لا تطابق الجهاز, تفشل الاستدعاءات برسالة `MediaInfo library is not available`; والتحول إلى حزمة `universal` يحل المشكلة.

******

### واجهة برمجة النصوص

******

في بيئة Node (البرامج النصية التي تبدأ بالتوجيه `"nodejs"`), احصل على الوحدة عبر `require("mediainfo")`; وتعيد كل الطرق Promise:

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

تعيد `read(path, options?)` كائن لقطة منظم (انظر `بنية اللقطة وخياراتها` أدناه); وتعيد `get(path, streamKind?, parameter)` النص الخام للمعلمة, والقيمة الافتراضية لـ `streamKind` هي `general`. لأسباب أمنية, لا يمكن لبرامج Node النصية الوصول إلا إلى الملفات داخل دليل المشروع, وتحل المسارات النسبية اعتبارا من جذر المشروع.

في بيئة Rhino (محرك البرمجة النصية الافتراضي في AutoJs6), تكون `mediainfo` وحدة عامة; و `mediainfo(path)` و `mediainfo.read(path)` متكافئان ويعيدان كائنا محللا بشكل متزامن:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

في الكائن المعاد, يحمل `path` و `inform` المسار المحلول والتقرير النصي الكامل; ويعمل كل نوع تدفق (مثل `general` و `video` و `audio`) كخاصية تعرض الحقول المحللة (مثل `mi.video.width`, وأسماء الحقول بنمط camelCase) وكدالة للاستعلام الحي عن المعلمات الخام (مثل `mi.audio("BitRate")`). يمكن لبرامج Rhino النصية الوصول إلى أي مسار يحق للمضيف قراءته.

******

### بنية اللقطة وخياراتها

******

تبدو اللقطة التي تعيدها `read()` في بيئة Node كما يلي (يتطابق JSON الذي تعيده طريقة AIDL `snapshot` في المكون معها, إلا أن `schema` فيه هو `autojs6-plugin-mediainfo-snapshot-v1` وبلا حقل `path`):

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

- `includeInform`: تضمين تقرير `inform` النصي أم لا, الافتراضي `true`; اضبطه على `false` للحصول على سلسلة فارغة وحمولة أصغر.
- `includeSections`: تحليل التقرير إلى `sections` أم لا, الافتراضي `true`; اضبطه على `false` للحصول على كائن فارغ.

يستخدم `sections` أسماء أقسام التقرير بأحرف صغيرة كمفاتيح (مع تعدد التدفقات من نوع واحد, قد تحمل أسماء الأقسام رقما مثل `audio #1`), وكل قيمة هي مصفوفة كائنات; تحول أسماء الحقول إلى نمط camelCase بينما تحتفظ القيم بنص MediaInfo الأصلي, بما في ذلك الوحدات ومسافات فصل الآلاف مثل `1 920 pixels`.

******

### أنواع التدفقات

******

تدعم معلمة `streamKind` في `get()` أنواع التدفقات التالية:

```text
general, video, audio, text, other, image, menu
```

معلمة `streamKind` غير حساسة لحالة الأحرف وترتبط بأنواع تدفقات MediaInfo الأصلية; والاستعلام عن تدفق غير موجود أو معلمة بلا قيمة يعيد سلسلة فارغة.

******

### الأسئلة الشائعة

******

#### كيف أتأكد من أن المكون الإضافي يعمل?

افتح مركز المكونات الإضافية في AutoJs6; رؤية مكون `MediaInfo` هناك تعني أن المضيف تعرف عليه. ثم شغل أي مثال من أمثلة `mediainfo` النصية; وعودة النتائج بشكل طبيعي تعني أن المكون يعمل.

#### لماذا لا توجد أيقونة للمكون في قائمة التطبيقات?

هذا متوقع. لا يملك المكون واجهة مستقلة ولا ينشئ أيقونة تشغيل; وبعد التثبيت يكتشفه AutoJs6 ويديره بالكامل في الخلفية, ويجري كل تفاعل داخل AutoJs6.

#### برنامج Node نصي يفشل برسالة `path must stay inside the scoped working directory`?

لأسباب أمنية, لا يسمح محرك Node إلا بالوصول إلى الملفات داخل دليل المشروع. انقل ملف الوسائط أو انسخه إلى دليل المشروع قبل قراءته; وللوصول إلى مواقع أخرى (مثل المعرض أو مجلد التنزيلات), استخدم برنامجا نصيا بمحرك Rhino بدلا من ذلك.

#### أعادت `get()` سلسلة فارغة?

يجب أن يكون اسم المعلمة معلمة MediaInfo أصلية (مثل `Format` و `Duration` و `Width` و `BitRate` و `FileSize`), وأن يكون التدفق الهدف موجودا فعلا. استخدم `read()` أولا لفحص الحقول المتاحة فعلا في `sections`, أو راجع تقرير `inform` الكامل.

#### قراءة الملفات الكبيرة بطيئة?

تحلل الملفات العادية الآن مباشرة دون نسخة كاملة في التخزين المؤقت, فتتجنب الملفات الكبيرة زمن النسخ المتزايد خطيا مع الحجم. لا تزال الواصفات غير القابلة للوصول العشوائي مثل الأنابيب, أو الصيغ التي يفشل تحليلها المباشر, تستخدم نسخة مؤقتة تتناسب كلفتها مع عدد البايتات المستلمة; أما زمن تحليل MediaInfoLib نفسه فيعتمد على الصيغة والمحتوى.

#### هل تخزن نتائج التحليل مؤقتا, وماذا يحدث عند تجاوز المهلة?

نعم. على Android 8.1 (API 27) والإصدارات الأحدث, يخزن المكون للملف العادي ذي الهوية المستقرة والمحتوى غير المتغير التقرير والاستعلامات واللقطات مؤقتا داخل العملية الحالية: بحد أقصى 32 ملفا و 64 استعلاما لكل ملف, مع صلاحية متجددة مدتها 10 دقائق ونحو 2 MiB من النص إجمالا. يعطل التخزين المؤقت تحفظيا على API 24-26 لعدم توفر طوابع زمنية للملفات بدقة النانوثانية; كما يفرغ عند انخفاض الذاكرة أو انتهاء العملية. لكل استدعاء AIDL حد 30 ثانية; عند تجاوزه يلغى التحليل الأصلي أو النسخ الاحتياطي تعاونيا, ويحذف الملف المؤقت, ويتضمن الاستثناء `MEDIAINFO_TIMEOUT`.

#### يحتوي الملف على عدة مسارات صوتية أو ترجمات; كيف أقرأ الثاني وما بعده?

تحتفظ لقطة `sections` بكل أقسام التقرير (مع تعدد التدفقات تحمل أسماء الأقسام رقما مثل `audio #2`), فاقرأها من هناك مباشرة; أما `get()` فتستعلم حاليا عن أول تدفق من كل نوع دائما, واختيار رقم التدفق مخطط له في [ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md).

#### هل يصل المكون الإضافي إلى الشبكة أو يطلب أذونات حساسة?

لا. لا يحتوي بيانه على أذونات شبكة أو تخزين أو كاميرا أو أي أذونات نظام حساسة أخرى; ولا يعلن إلا إذن المكون الإضافي المستخدم للتواصل مع AutoJs6. يصل محتوى الوسائط من المضيف كواصف للقراءة فقط, وتحذف النسخة المؤقتة المنشأة للتحليل فورا.

******

### الأذونات والأمان

******

قد تأتي ملفات الوسائط من مصادر غير موثوقة, لذا يضع التصميم عدة خطوط دفاع حول التحليل:

- عزل العمليات: يجري التحليل في عملية المكون الخاصة ولا تحقن المكتبة الأصلية أبدا في عملية المضيف, فحتى فشل التحليل يترك AutoJs6 يعمل بشكل طبيعي.
- سطح بيانات أدنى: لا يستطيع المكون قراءة تخزين الجهاز بنفسه; ولا يتلقى سوى واصف ملف للقراءة فقط يفتحه المضيف مع اسم للعرض.
- قراءة مباشرة عند الإمكان وحذف عند الرجوع: لا تنشئ الواصفات العادية القابلة للوصول العشوائي نسخة من الوسائط; وحده مسار التوافق يكتب في التخزين الخاص ويحذف الملف المؤقت فور انتهاء الاستدعاء.
- أذونات دنيا: لا أذونات شبكة أو تخزين أو كاميرا أو أي أذونات نظام حساسة أخرى; والخدمة ونقطة الإيقاظ محميتان بإذن مكون AutoJs6 الإضافي (`org.autojs.permission.PLUGIN`), فلا تستطيع تطبيقات الجهات الخارجية استدعاءهما مباشرة.
- مفتوح وقابل للتدقيق: كود المكون وبرامج البناء النصية وخط إنتاج الوثائق كلها مفتوحة المصدر, ومصدر المكتبة الأصلية وغلاف JNI مذكوران في قسم الترخيص.

ثبت المكون الإضافي فقط من صفحة [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) الرسمية أو من قنوات موثوقة أخرى; فقد تكون الحزم مجهولة المصدر معدلة حتى لو بدا الاسم ورقم الإصدار متطابقين.

******

### واجهة المكون الإضافي

******

المعلومات التالية موجهة لمطوري مضيف AutoJs6 والمكونات الإضافية; يستخدم المضيف هذه المعرفات لاكتشاف المكون والتفاوض على القدرات:

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

تكشف `MediainfoPluginService` أربع طرق, `getInfo`/`inform`/`get`/`snapshot`, عبر واجهة AIDL باسم `IMediainfoPlugin`; يمرر محتوى الوسائط كـ `ParcelFileDescriptor` للقراءة فقط مع اسم للعرض, وتقبل `snapshot` إضافة إلى ذلك حزمة `Bundle` من الخيارات تحمل `includeInform`/`includeSections`. الخدمة و `WakeActivity` كلتاهما محميتان بإذن `org.autojs.permission.PLUGIN`.

يفحص المكون ملفات APK المثبتة من نوع base / split ويبلغ ديناميكيا عن بنى ABI التي تحتوي فعليا على `libmediainfo.so`; تبلغ حزمة ABI الأحادية عن بنيتها فقط, بينما تبلغ حزمة `universal` عن البنى الأربع كلها. إذا تعذرت قراءة مسارات APK, يستخدم المكون بديلا آمنا بحسب معمارية العملية الحالية عند وجود مكتبة أصلية مستخرجة.

******

### خارطة الطريق

******

تدار قدرات المكون المخطط لها وحالة إنجازها كقائمة قابلة للتأشير في ROADMAP.md, منظمة حسب مراحل رئيسية مع معايير قبول, وتغطي الاستعلام برقم التدفق, والتحليل دون نسخ, والإبلاغ الديناميكي عن ABI, وتطور المكتبة الأصلية, والتكامل المستمر. البنود غير المؤشرة نوايا وليست قدرات مسلمة; والنقاش عبر Issues موضع ترحيب.

- [عرض ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/ROADMAP.md)

******

### سجل الإصدارات

******

#### v1.1.0

_2026/08/31_

- `ميزة` تحليل بلا نسخ كامل للملف: تقرأ MediaInfoLib واصفات الملفات العادية القابلة للبحث مباشرة عبر /proc/self/fd; ولا تستخدم نسخة مؤقتة خاصة إلا للأنابيب أو عند فشل القراءة المباشرة
- `ميزة` ذاكرة مؤقتة للنتائج داخل العملية: في API 27 وما بعده تشترك التقارير الكاملة واستعلامات الحقول واللقطات في هوية ملف ثابتة و LRU وانتهاء متجدد بعد 10 دقائق وتنظيف عند انخفاض الذاكرة
- `ميزة` إلغاء تعاوني ومهلة زمنية: يقتصر كل استدعاء AIDL على 30 ثانية; وعند التجاوز يتوقف التحليل الأصلي أو النسخ الاحتياطي وتحرر الموارد المؤقتة وتعاد MEDIAINFO_TIMEOUT
- `إصلاح` ألغي النسخ الدائم لملف الوسائط كاملا مع ضمان إغلاق الواصفات والمحللات الأصلية والتدفقات والملفات المؤقتة في جميع مسارات الخطأ
- `إصلاح` تحافظ هوية الذاكرة المؤقتة على الطوابع الزمنية بالنانوثانية وتعطل الذاكرة المؤقتة في API 24 إلى 26 حيث لا يمكن التحقق من هذه المعلومات بأمان
- `تحسين` يتحقق مخزون ABI الديناميكي من مكتبات MediaInfoLib المضمنة فعليا ويحافظ على اتساق تقرير وقت التشغيل والبيانات الوصفية وخمسة متغيرات APK
- `تحسين` يتعامل محلل اللقطات بمتانة أكبر مع التسميات المترجمة والمجموعات المتكررة والحقول غير المعروفة والمخرجات الجزئية من MediaInfoLib
- `تحسين` أضيفت أدوات قياس قابلة لإعادة الإنتاج للاستدعاءات الباردة والدافئة والتزامن والمهل والتحقق بوسائط حقيقية مع بيان للمصادر وملخص SHA-256
- `تحسين` يغطي توليد الوثائق المتحقق منه الآن 10 لغات وينتج README والتعليمات المضمنة وسجلات التغييرات بصورة حتمية

#### v1.0.0

_2026/07/15_

- `ميزة` أول إصدار مستقر: يجلب إلى AutoJs6 قراءة معلومات ملفات الوسائط عبر MediaInfoLib, فيحصل باستدعاء واحد على تنسيق الحاوية والترميز والمدة والدقة ومعدل البت والقنوات وغيرها
- `ميزة` واجهة برمجة النصوص: بيئة Node تحصل على `read`/`get` غير المتزامنين عبر `require("mediainfo")`; وبيئة Rhino تحصل على الوحدة العامة `mediainfo(path)` التي تعيد بشكل متزامن كائنا محللا يمكن الوصول إلى خصائصه
- `ميزة` ثلاث قدرات قراءة: تقرير نصي كامل (`inform`), واستعلام معلمة واحدة (`get`), ولقطة JSON منظمة (`snapshot`, بالمخطط `autojs6-plugin-mediainfo-snapshot-v1`)
- `ميزة` يكتشفه AutoJs6 تلقائيا عبر `org.autojs.plugin.MEDIAINFO`; يتلقى المكون الإضافي محتوى الوسائط ويحلله في عمليته الخاصة عبر واصفات ملفات للقراءة فقط, دون طلب أذونات شبكة أو أذونات نظام حساسة
- `ميزة` يوفر أربع حزم أحادية البنية (`arm64-v8a` و `armeabi-v7a` و `x86` و `x86_64`) بالإضافة إلى حزمة `universal` بكل البنى; وتحمل أسماء ملفات الإصدار رقم الإصدار و ABI وملخص CRC32
- `ميزة` بيانات المكون الإضافي والتعليمات و README وسجل التغييرات تغطي 10 لغات: الصينية المبسطة, والصينية التقليدية لهونغ كونغ, والصينية التقليدية لتايوان, والإنجليزية, والفرنسية, والإسبانية, واليابانية, والكورية, والروسية, والعربية

##### لمزيد من سجل الإصدارات

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/app/src/main/assets/doc/CHANGELOG-ar.md)

******

### البناء

******

هذا القسم موجه للمطورين الراغبين في بناء المكون من المصدر.

استنسخ المستودع مع الوحدتين الفرعيتين الرسميتين المثبتتين قبل البناء:

```powershell
git clone --recurse-submodules https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo.git
Set-Location AutoJs6-Plugin-MediaInfo
git submodule update --init --recursive
```

- [native/README.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/native/README.md)

بناء حزم APK بوضع debug:

```powershell
.\gradlew.bat :app:assembleDebug
```

بناء حزم APK بوضع release (تقسيم ABI مفعل, فتنتج 4 حزم أحادية البنية وحزمة `universal` واحدة دفعة واحدة; اضبط ملف `sign.properties` غير المتتبع للتوقيع التلقائي):

```powershell
.\gradlew.bat :app:assembleRelease
```

لأرشفة الإصدارات, شغل المهمة `:app:appendDigestToReleasedFiles` التي تنسخ حزم APK من `app/release` إلى `app/releases` وتعيد تسميتها بالنمط `autojs6-plugin-mediainfo-v1.1.0-<abi>-<crc32>.apk`.

معاملات البناء مجمعة في `version.properties`: الحد الأدنى من SDK هو 24 (Android 7.0), و SDK الهدف 36, والإصدار الحالي 1.1.0.

******

### الترجمة وإنشاء الوثائق

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

يحمل `strings.xml` وصف المكون المترجم ورسائل الخطأ, ويحمل `plugin_instruction.md` التعليمات المعروضة في مركز المكونات الإضافية بالمضيف. ينشأ README وسجل التغييرات والتعليمات كلها من مصادر JSON: عدل المصادر تحت `.readme/` و `.changelog/`, ثم شغل `py .python/generate_markdown.py` لإعادة إنشاء كل المخرجات; ولا تحرر المخرجات المولدة يدويا أبدا. شغل `py .python/generate_markdown.py --check` للتحقق من تزامن المصادر والمخرجات (كما تتحقق CI من ذلك تلقائيا).

******

### الترخيص

******

كود المشروع مرخص بموجب [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE). في مسار v2 المبني من المصدر, يبنى `libmediainfo.so` من المصدرين الرسميين [MediaInfoLib](https://github.com/MediaArea/MediaInfoLib) (BSD 2-Clause) و [ZenLib](https://github.com/MediaArea/ZenLib) (ترخيص zlib), بينما تتم صيانة جسر JNI المتوافق في هذا المستودع. ويوثق أصل ملف v1.1.0 الثنائي المجمد بشكل منفصل.

- [MEDIAINFO_UPSTREAM.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/MEDIAINFO_UPSTREAM.md)

******

### روابط

******

- وثائق AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- موقع MediaInfo الرسمي: https://mediaarea.net/en/MediaInfo
- مشروع MediaInfoLib: https://github.com/MediaArea/MediaInfoLib
- غلاف MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
