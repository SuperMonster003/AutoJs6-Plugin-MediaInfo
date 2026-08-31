The MediaInfo Plugin brings media file inspection to AutoJs6. Once installed, a single line of script fetches hundreds of technical parameters of video, audio, and image files, such as container format, codec, duration, resolution, bit rate, and channels, and the media info dialog in the AutoJs6 file list shows the full parsed report powered by this plugin. Parsing comes from MediaInfoLib, the same open-source library behind the desktop MediaInfo application.

### Usage

1. Download the plugin APK matching the device from the [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo/releases) page and install it on the device running AutoJs6; when unsure, pick the `universal` package or see `Choosing an APK` below.
2. Open the AutoJs6 plugin center and confirm that the `MediaInfo` plugin is recognized and enabled.
3. Call the `mediainfo` module in scripts as shown in `Script API` below, or open the media info dialog of any media file in the AutoJs6 file list to view the full report directly.

If the plugin does not show up in the plugin center, upgrade AutoJs6 to a recent version first (internal build 3923 or above). The plugin itself supports devices running Android 7.0 (API 24) and above.

### Script API

In the Node environment (scripts starting with the `"nodejs"` directive), obtain the module via `require("mediainfo")`; every method returns a Promise:

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

`read(path, options?)` returns a structured snapshot object (see `Snapshot Structure and Options` below); `get(path, streamKind?, parameter)` returns the raw parameter text, with `streamKind` defaulting to `general`. For safety, Node scripts can only access files inside the project directory, and relative paths resolve against the project root.

In the Rhino environment (the default AutoJs6 script engine), `mediainfo` is a global module; `mediainfo(path)` and `mediainfo.read(path)` are equivalent and return a parsed object synchronously:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

On the returned object, `path` and `inform` hold the resolved path and the full text report; each stream kind (such as `general`, `video`, `audio`) works both as a property exposing parsed fields (such as `mi.video.width`, field names in camelCase) and as a function for live raw parameter queries (such as `mi.audio("BitRate")`). Rhino scripts may access any path the host is allowed to read.

### Stream Kinds

The `streamKind` parameter of `get()` supports the following stream kinds:

```text
general, video, audio, text, other, image, menu
```

`streamKind` is case-insensitive and maps to native MediaInfo stream kinds; querying a stream that does not exist or a parameter with no value returns an empty string.

For more usage details and field references, see the [AutoJs6 MediaInfo documentation](https://docs.autojs6.com/#/mediainfo) and the [project homepage](https://github.com/SuperMonster003/AutoJs6-Plugin-MediaInfo).
