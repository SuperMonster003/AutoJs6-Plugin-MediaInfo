/*
 * Copyright (c) 2026 SuperMonster003
 * SPDX-License-Identifier: MPL-2.0
 *
 * Compatibility JNI bridge for the AutoJs6 MediaInfo plugin. MediaInfoLib and
 * ZenLib are separate upstream projects distributed under their own licenses.
 */

#include <jni.h>

#include <MediaInfo/MediaInfo.h>
#include <ZenLib/Ztring.h>

#include <cerrno>
#include <cstdint>
#include <fcntl.h>
#include <limits>
#include <string>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <utility>
#include <vector>

namespace {

constexpr char kMediaInfoClassName[] = "org/mediainfo/android/MediaInfo";
constexpr std::size_t kReadBufferBytes = 1024U * 1024U;
constexpr char kOpenError[] = "Error opening file...";

jmethodID gGetIsCanceled = nullptr;

static_assert(sizeof(off_t) >= 8, "LARGE_FILES must provide 64-bit file offsets");
static_assert(sizeof(ZenLib::Char) >= 4, "ENABLE_UNICODE is required on Android");

class ScopedStringChars final {
public:
    ScopedStringChars(JNIEnv* environment, jstring value)
        : env(environment), source(value), chars(value == nullptr ? nullptr : env->GetStringChars(value, nullptr)) {}

    ~ScopedStringChars() {
        if (chars != nullptr) {
            env->ReleaseStringChars(source, chars);
        }
    }

    ScopedStringChars(const ScopedStringChars&) = delete;
    ScopedStringChars& operator=(const ScopedStringChars&) = delete;

    const jchar* get() const { return chars; }

private:
    JNIEnv* env;
    jstring source;
    const jchar* chars;
};

class ScopedFd final {
public:
    explicit ScopedFd(int descriptor) : fd(descriptor) {}
    ~ScopedFd() {
        if (fd >= 0) {
            close(fd);
        }
    }

    ScopedFd(const ScopedFd&) = delete;
    ScopedFd& operator=(const ScopedFd&) = delete;

    int get() const { return fd; }
    explicit operator bool() const { return fd >= 0; }

private:
    int fd;
};

ZenLib::Ztring FromJString(JNIEnv* env, jstring value) {
    ZenLib::Ztring result;
    if (value == nullptr) {
        return result;
    }

    const jsize length = env->GetStringLength(value);
    ScopedStringChars scopedChars(env, value);
    const jchar* chars = scopedChars.get();
    if (chars == nullptr) {
        return result;
    }

    result.reserve(static_cast<std::size_t>(length));
    for (jsize index = 0; index < length; ++index) {
        std::uint32_t codePoint = chars[index];
        if (codePoint >= 0xD800U && codePoint <= 0xDBFFU) {
            if (index + 1 < length) {
                const std::uint32_t low = chars[index + 1];
                if (low >= 0xDC00U && low <= 0xDFFFU) {
                    codePoint = 0x10000U + ((codePoint - 0xD800U) << 10U) + (low - 0xDC00U);
                    ++index;
                } else {
                    codePoint = 0xFFFDU;
                }
            } else {
                codePoint = 0xFFFDU;
            }
        } else if (codePoint >= 0xDC00U && codePoint <= 0xDFFFU) {
            codePoint = 0xFFFDU;
        }
        result.push_back(static_cast<ZenLib::Char>(codePoint));
    }
    return result;
}

jstring ToJString(JNIEnv* env, const ZenLib::Ztring& value) {
    std::vector<jchar> utf16;
    utf16.reserve(value.size());
    for (const ZenLib::Char character : value) {
        std::uint32_t codePoint = static_cast<std::uint32_t>(character);
        if (codePoint > 0x10FFFFU || (codePoint >= 0xD800U && codePoint <= 0xDFFFU)) {
            codePoint = 0xFFFDU;
        }
        if (codePoint <= 0xFFFFU) {
            utf16.push_back(static_cast<jchar>(codePoint));
        } else {
            codePoint -= 0x10000U;
            utf16.push_back(static_cast<jchar>(0xD800U + (codePoint >> 10U)));
            utf16.push_back(static_cast<jchar>(0xDC00U + (codePoint & 0x3FFU)));
        }
    }

    if (utf16.size() > static_cast<std::size_t>(std::numeric_limits<jsize>::max())) {
        return env->NewStringUTF("");
    }
    if (utf16.empty()) {
        return env->NewStringUTF("");
    }
    return env->NewString(utf16.data(), static_cast<jsize>(utf16.size()));
}

jstring EmptyJString(JNIEnv* env) {
    return env->NewStringUTF("");
}

bool IsCanceled(JNIEnv* env, jobject self) {
    const jint value = env->CallIntMethod(self, gGetIsCanceled);
    return env->ExceptionCheck() == JNI_TRUE || value != 0;
}

bool IsValidStreamKind(jint value) {
    return value >= 0 && value < static_cast<jint>(MediaInfoLib::Stream_Max);
}

bool IsValidInfoKind(jint value) {
    return value >= 0 && value < static_cast<jint>(MediaInfoLib::Info_Max);
}

bool ParseFile(
    JNIEnv* env,
    jobject self,
    jstring filename,
    MediaInfoLib::MediaInfo& mediaInfo,
    bool& canceled
) {
    canceled = false;
    const ZenLib::Ztring filenameValue = FromJString(env, filename);
    if (env->ExceptionCheck() == JNI_TRUE) {
        return false;
    }

    const std::string path = filenameValue.To_UTF8();
    ScopedFd file(open(path.c_str(), O_RDONLY | O_CLOEXEC));
    if (!file) {
        return false;
    }

    ZenLib::int64u fileSize = std::numeric_limits<ZenLib::int64u>::max();
    struct stat fileStats {};
    if (fstat(file.get(), &fileStats) == 0 && fileStats.st_size >= 0) {
        fileSize = static_cast<ZenLib::int64u>(fileStats.st_size);
    }

    // A direct /proc/self/fd source may inherit a non-zero descriptor offset.
    // Parsing always starts from the beginning, matching the legacy bridge.
    static_cast<void>(lseek(file.get(), 0, SEEK_SET));

    std::vector<ZenLib::int8u> buffer(kReadBufferBytes);
    mediaInfo.Open_Buffer_Init(fileSize, 0);

    while (true) {
        if (IsCanceled(env, self)) {
            canceled = true;
            break;
        }

        ssize_t bytesRead;
        do {
            bytesRead = read(file.get(), buffer.data(), buffer.size());
        } while (bytesRead < 0 && errno == EINTR);

        if (bytesRead < 0) {
            break;
        }

        const std::size_t state = mediaInfo.Open_Buffer_Continue(
            buffer.data(),
            static_cast<std::size_t>(bytesRead)
        );
        if ((state & 0x08U) != 0U) {
            break;
        }

        if (IsCanceled(env, self)) {
            canceled = true;
            break;
        }

        const ZenLib::int64u seekTo = mediaInfo.Open_Buffer_Continue_GoTo_Get();
        if (
            seekTo != std::numeric_limits<ZenLib::int64u>::max() &&
            seekTo <= static_cast<ZenLib::int64u>(std::numeric_limits<off_t>::max())
        ) {
            const off_t offset = lseek(file.get(), static_cast<off_t>(seekTo), SEEK_SET);
            if (offset >= 0) {
                mediaInfo.Open_Buffer_Init(fileSize, static_cast<ZenLib::int64u>(offset));
                continue;
            }
        }

        if (bytesRead == 0) {
            break;
        }
    }

    mediaInfo.Open_Buffer_Finalize();
    return true;
}

template <typename Callback>
jstring StringCall(JNIEnv* env, Callback&& callback) noexcept {
    try {
        return std::forward<Callback>(callback)();
    } catch (...) {
        if (env->ExceptionCheck() == JNI_TRUE) {
            return nullptr;
        }
        return EmptyJString(env);
    }
}

jstring MediaInfoGetById(
    JNIEnv* env,
    jobject self,
    jstring filename,
    jint streamKind,
    jint streamNumber,
    jint parameter
) {
    return StringCall(env, [&]() -> jstring {
        if (!IsValidStreamKind(streamKind) || streamNumber < 0 || parameter < 0) {
            return EmptyJString(env);
        }
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return env->ExceptionCheck() == JNI_TRUE ? nullptr : env->NewStringUTF(kOpenError);
        }
        return ToJString(
            env,
            mediaInfo.Get(
                static_cast<MediaInfoLib::stream_t>(streamKind),
                static_cast<std::size_t>(streamNumber),
                static_cast<std::size_t>(parameter)
            )
        );
    });
}

jstring MediaInfoGetByIdDetail(
    JNIEnv* env,
    jobject self,
    jstring filename,
    jint streamKind,
    jint streamNumber,
    jint parameter,
    jint infoKind
) {
    return StringCall(env, [&]() -> jstring {
        if (
            !IsValidStreamKind(streamKind) || !IsValidInfoKind(infoKind) ||
            streamNumber < 0 || parameter < 0
        ) {
            return EmptyJString(env);
        }
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return env->ExceptionCheck() == JNI_TRUE ? nullptr : env->NewStringUTF(kOpenError);
        }
        return ToJString(
            env,
            mediaInfo.Get(
                static_cast<MediaInfoLib::stream_t>(streamKind),
                static_cast<std::size_t>(streamNumber),
                static_cast<std::size_t>(parameter),
                static_cast<MediaInfoLib::info_t>(infoKind)
            )
        );
    });
}

jstring MediaInfoGetByName(
    JNIEnv* env,
    jobject self,
    jstring filename,
    jint streamKind,
    jint streamNumber,
    jstring parameter
) {
    return StringCall(env, [&]() -> jstring {
        if (!IsValidStreamKind(streamKind) || streamNumber < 0) {
            return EmptyJString(env);
        }
        const ZenLib::Ztring parameterValue = FromJString(env, parameter);
        if (env->ExceptionCheck() == JNI_TRUE) {
            return nullptr;
        }
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return env->ExceptionCheck() == JNI_TRUE ? nullptr : env->NewStringUTF(kOpenError);
        }
        return ToJString(
            env,
            mediaInfo.Get(
                static_cast<MediaInfoLib::stream_t>(streamKind),
                static_cast<std::size_t>(streamNumber),
                parameterValue
            )
        );
    });
}

jstring MediaInfoGetByNameDetail(
    JNIEnv* env,
    jobject self,
    jstring filename,
    jint streamKind,
    jint streamNumber,
    jstring parameter,
    jint infoKind,
    jint searchKind
) {
    return StringCall(env, [&]() -> jstring {
        if (
            !IsValidStreamKind(streamKind) || !IsValidInfoKind(infoKind) ||
            !IsValidInfoKind(searchKind) || streamNumber < 0
        ) {
            return EmptyJString(env);
        }
        const ZenLib::Ztring parameterValue = FromJString(env, parameter);
        if (env->ExceptionCheck() == JNI_TRUE) {
            return nullptr;
        }
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return env->ExceptionCheck() == JNI_TRUE ? nullptr : env->NewStringUTF(kOpenError);
        }
        return ToJString(
            env,
            mediaInfo.Get(
                static_cast<MediaInfoLib::stream_t>(streamKind),
                static_cast<std::size_t>(streamNumber),
                parameterValue,
                static_cast<MediaInfoLib::info_t>(infoKind),
                static_cast<MediaInfoLib::info_t>(searchKind)
            )
        );
    });
}

jint MediaInfoCountGet(
    JNIEnv* env,
    jobject self,
    jstring filename,
    jint streamKind,
    jint streamNumber
) noexcept {
    try {
        if (!IsValidStreamKind(streamKind) || streamNumber < -1) {
            return -1;
        }
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return -1;
        }
        const std::size_t resolvedStreamNumber = streamNumber == -1
            ? std::numeric_limits<std::size_t>::max()
            : static_cast<std::size_t>(streamNumber);
        return static_cast<jint>(mediaInfo.Count_Get(
            static_cast<MediaInfoLib::stream_t>(streamKind),
            resolvedStreamNumber
        ));
    } catch (...) {
        return -1;
    }
}

jstring MediaInfoGetReport(JNIEnv* env, jobject self, jstring filename) {
    return StringCall(env, [&]() -> jstring {
        MediaInfoLib::MediaInfo mediaInfo;
        bool canceled = false;
        if (!ParseFile(env, self, filename, mediaInfo, canceled)) {
            return env->ExceptionCheck() == JNI_TRUE ? nullptr : env->NewStringUTF(kOpenError);
        }

        const ZenLib::Ztring filenameValue = FromJString(env, filename);
        if (env->ExceptionCheck() == JNI_TRUE) {
            return nullptr;
        }
        ZenLib::Ztring report = __T("File\r\n");
        report += __T("Complete name                            : ");
        report += filenameValue;
        report += __T("\r\n\r\n");

        static_cast<void>(mediaInfo.Option(__T("Complete")));
        report += mediaInfo.Inform();
        if (canceled) {
            report += __T("Getting MediaInfo for '");
            report += filenameValue;
            report += __T("' has been terminated!");
            report += __T("\r\nThe data obtained are not fully!");
        }
        return ToJString(env, report);
    });
}

jstring MediaInfoGetOption(JNIEnv* env, jobject, jstring parameter) {
    return StringCall(env, [&]() -> jstring {
        const ZenLib::Ztring parameterValue = FromJString(env, parameter);
        if (env->ExceptionCheck() == JNI_TRUE) {
            return nullptr;
        }
        MediaInfoLib::MediaInfo mediaInfo;
        return ToJString(env, mediaInfo.Option(parameterValue));
    });
}

#define AUTOJS6_JNI_METHOD(methodName, signature, function) \
    {const_cast<char*>(methodName), const_cast<char*>(signature), reinterpret_cast<void*>(function)}

JNINativeMethod kMethods[] = {
    AUTOJS6_JNI_METHOD("getById", "(Ljava/lang/String;III)Ljava/lang/String;", MediaInfoGetById),
    AUTOJS6_JNI_METHOD(
        "getByName",
        "(Ljava/lang/String;IILjava/lang/String;)Ljava/lang/String;",
        MediaInfoGetByName
    ),
    AUTOJS6_JNI_METHOD(
        "getByIdDetail",
        "(Ljava/lang/String;IIII)Ljava/lang/String;",
        MediaInfoGetByIdDetail
    ),
    AUTOJS6_JNI_METHOD(
        "getByNameDetail",
        "(Ljava/lang/String;IILjava/lang/String;II)Ljava/lang/String;",
        MediaInfoGetByNameDetail
    ),
    AUTOJS6_JNI_METHOD("countGet", "(Ljava/lang/String;II)I", MediaInfoCountGet),
    AUTOJS6_JNI_METHOD(
        "getMediaInfo",
        "(Ljava/lang/String;)Ljava/lang/String;",
        MediaInfoGetReport
    ),
    AUTOJS6_JNI_METHOD(
        "getMediaInfoOption",
        "(Ljava/lang/String;)Ljava/lang/String;",
        MediaInfoGetOption
    ),
};

#undef AUTOJS6_JNI_METHOD

}  // namespace

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
    void* rawEnvironment = nullptr;
    if (vm->GetEnv(&rawEnvironment, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    auto* env = static_cast<JNIEnv*>(rawEnvironment);

    jclass mediaInfoClass = env->FindClass(kMediaInfoClassName);
    if (mediaInfoClass == nullptr) {
        return JNI_ERR;
    }

    gGetIsCanceled = env->GetMethodID(mediaInfoClass, "getIsCanceled", "()I");
    if (gGetIsCanceled == nullptr) {
        env->DeleteLocalRef(mediaInfoClass);
        return JNI_ERR;
    }

    const jint registrationResult = env->RegisterNatives(
        mediaInfoClass,
        kMethods,
        static_cast<jint>(sizeof(kMethods) / sizeof(kMethods[0]))
    );
    env->DeleteLocalRef(mediaInfoClass);
    if (registrationResult != JNI_OK) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}
