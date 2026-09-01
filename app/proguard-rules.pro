-dontwarn kotlinx.parcelize.Parcelize

# JNI_OnLoad resolves and registers this exact class and its native method names.
# Keep the full wrapper stable in minified release builds, not only debug builds.
-keep class org.mediainfo.android.MediaInfo { *; }

-keep class io.github.supermonster003.autojs6.plugin.paddleocr.v5.** { *; }
-keep class org.autojs.plugin.** { *; }
-keep class com.paddle.ocr.** { *; }
-keep class ai.onnxruntime.** { *; }
-keep class org.opencv.** { *; }
