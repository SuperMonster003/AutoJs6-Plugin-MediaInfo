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
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-MediaInfo?color=534BAE&label=License"/></a>
  </p>
</div>

******

### اللغات

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

يوفر مكون AutoJs6 MediaInfo الإضافي قدرة قراءة بيانات الوسائط الوصفية في AutoJs6 اعتمادا على MediaInfo, مع دعم التقارير الكاملة, واستعلام المعلمات الفردية, ولقطات JSON المنظمة.

******

### الميزات

******

- يوفر خدمة المكون الإضافي `mediainfo` مع معرف المكون `mediainfo`.
- يدعم `mediainfo.read(path, options)` و `mediainfo.get(path, streamKind, parameter, options)` في بيئة AutoJs6 Node.
- يدعم الاكتشاف والاستدعاء من المضيف عبر `org.autojs.plugin.MEDIAINFO`, مع كشف `inform` و `get` و `snapshot` عبر واجهة AIDL الأساسية.
- يتضمن `libmediainfo.so` من أجل `arm64-v8a` و `armeabi-v7a` و `x86_64` و `x86`.
- تمت ترجمة بيانات المكون الإضافي, وتعليمات الاستخدام, و README, و changelog إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية لهونغ كونغ والصينية التقليدية لتايوان.

******

### الاستخدام

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

يجب أن يشير المسار إلى ملف وسائط يستطيع المضيف الوصول إليه. في بيئة Node, تدعم المسارات النسبية داخل دليل العمل.

******

### أنواع التدفقات

******

تشمل أنواع تدفقات MediaInfo المدعومة:

```text
general, video, audio, text, other,
image, menu
```

`streamKind` في `mediainfo.get()` غير حساس لحالة الأحرف ويرتبط بأنواع تدفقات MediaInfo الأصلية.

******

### خيارات اللقطة

******

- `includeInform`: تضمين تقرير MediaInfo النصي في اللقطة, الافتراضي `true`.
- `includeSections`: تحليل التقرير إلى `sections`, الافتراضي `true`.

******

### سجل الإصدارات

******

# v1.0.0

###### 2026/07/15

* `ميزة` إضافة خدمة مكون MediaInfo الإضافي مع معرف المكون `mediainfo` والمحرك `mediainfo`
* `ميزة` إضافة الاكتشاف والاستدعاء من المضيف عبر `org.autojs.plugin.MEDIAINFO`
* `ميزة` إضافة قدرات `inform` و `get` و `snapshot` للتقارير الكاملة للوسائط, واستعلام المعلمات الفردية, ولقطات JSON المنظمة
* `ميزة` تضمين `libmediainfo.so` من أجل `arm64-v8a` و `armeabi-v7a` و `x86_64` و `x86`, مع متغير APK باسم `universal`
* `ميزة` إضافة بيانات ABI المدعومة إلى معلومات تشغيل المكون الإضافي وأسماء ملفات APK للإصدار مع الإصدار, ومتغير ABI, وملخص CRC32
* `ميزة` إضافة بيانات المكون الإضافي, وتعليمات الاستخدام, و README, و changelog مترجمة إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية لهونغ كونغ والصينية التقليدية لتايوان

##### لمزيد من سجل الإصدارات

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/blob/master/.changelog/CHANGELOG-ar.md)

******

### البناء

******

```powershell
.\gradlew.bat :app:assembleDebug
```

بناء Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

تأتي معاملات البناء من `version.properties`; الحد الأدنى الحالي من SDK هو 24 و SDK الهدف هو 36.

******

### هيكل الموارد

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
```

يحتوي `strings.xml` على أوصاف المكون الإضافي ورسائل الخطأ المترجمة; ويحتوي `plugin_instruction.md` على تعليمات الاستخدام التي يعرضها المضيف. يتم إنشاء ملفات README و CHANGELOG من مصادر JSON بواسطة `.python/generate_markdown.py`.

******

### روابط

******

- وثائق AutoJs6 MediaInfo: https://docs.autojs6.com/#/mediainfo
- مشروع MediaInfo الرسمي: https://mediaarea.net/en/MediaInfo
- مشروع MediaInfoLib الرسمي: https://github.com/MediaArea/MediaInfoLib
- مشروع MediaInfoLib Android: https://github.com/olegazyx/MediaInfoLib-android
