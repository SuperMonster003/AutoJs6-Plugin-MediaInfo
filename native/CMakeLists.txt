cmake_minimum_required(VERSION 3.22.1)

project(autojs6_mediainfo_native LANGUAGES C CXX)

if(NOT ANDROID)
    message(FATAL_ERROR "The AutoJs6 MediaInfo native library is supported only for Android")
endif()

# MediaInfoLib temporarily overrides BUILD_SHARED_LIBS while entering ZenLib.
# CMP0077=NEW makes ZenLib honor that normal variable and stay statically linked
# into the single libmediainfo.so packaged by the plugin.
set(CMAKE_POLICY_DEFAULT_CMP0077 NEW)
set(CMAKE_POLICY_DEFAULT_CMP0077 NEW CACHE STRING "Honor parent option values" FORCE)

set(BUILD_SHARED_LIBS ON CACHE BOOL "Build MediaInfoLib as libmediainfo.so" FORCE)
set(BUILD_ZENLIB ON CACHE BOOL "Build the pinned sibling ZenLib source" FORCE)
set(BUILD_ZLIB OFF CACHE BOOL "Use Android's platform zlib" FORCE)
set(ENABLE_UNICODE ON CACHE BOOL "Enable Unicode in ZenLib" FORCE)
set(LARGE_FILES ON CACHE BOOL "Use 64-bit file offsets on every ABI" FORCE)

# The plugin owns its compatibility JNI surface. The upstream JNI source stays
# in the source list but compiles to an empty translation unit with this flag.
set(MEDIAINFO_JNI OFF CACHE STRING "Use the AutoJs6 compatibility JNI bridge" FORCE)

# Keep the Android-oriented feature profile small and deterministic. Container,
# codec, image, audio, text, and metadata parsers remain at upstream defaults.
set(_autojs6_disabled_mediainfo_features
    ADVANCED
    AES
    DEMUX
    DIRECTORY
    DUPLICATE
    DVDIF_ANALYZE
    EVENTS
    FILTER
    FIXITY
    GRAPH
    IBI
    LIBCURL
    LIBMMS
    MACROBLOCKS
    MD5
    NEXTPACKET
    READER
    READTHREAD
    REFERENCES
    SHA1
    SHA2
    TRACE
    TRACE_FFV1CONTENT
)
foreach(_feature IN LISTS _autojs6_disabled_mediainfo_features)
    set("MEDIAINFO_${_feature}" OFF CACHE STRING "Disabled by the AutoJs6 Android profile" FORCE)
endforeach()
unset(_feature)
unset(_autojs6_disabled_mediainfo_features)

add_subdirectory(
    "${CMAKE_CURRENT_SOURCE_DIR}/upstream/MediaInfoLib/Project/CMake"
    "${CMAKE_CURRENT_BINARY_DIR}/MediaInfoLib"
)

if(NOT TARGET mediainfo)
    message(FATAL_ERROR "Pinned MediaInfoLib source did not define the mediainfo target")
endif()

target_sources(mediainfo PRIVATE
    "${CMAKE_CURRENT_SOURCE_DIR}/bridge/mediainfo_jni.cpp"
)

target_compile_definitions(mediainfo PRIVATE
    AUTOJS6_MEDIAINFO_COMPAT_JNI=1
)

set(_autojs6_version_script "${CMAKE_CURRENT_SOURCE_DIR}/bridge/libmediainfo.map.txt")
target_link_options(mediainfo PRIVATE
    "-Wl,--version-script=${_autojs6_version_script}"
    "-Wl,-z,max-page-size=16384"
)
set_property(TARGET mediainfo APPEND PROPERTY LINK_DEPENDS "${_autojs6_version_script}")

set_target_properties(mediainfo PROPERTIES
    OUTPUT_NAME "mediainfo"
)
