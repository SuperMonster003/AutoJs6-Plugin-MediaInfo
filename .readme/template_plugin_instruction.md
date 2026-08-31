{{ p_introduction_what }}

### {{ h3_usage }}

{{ placeholder_usage_steps }}

{{ p_usage_note }}

### {{ h3_script_api }}

{{ p_script_api_node }}:

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

{{ p_script_api_node_note }}

{{ p_script_api_rhino }}:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

{{ p_script_api_rhino_note }}

### {{ h3_stream_kinds }}

{{ p_stream_kinds }}:

```text
{{ stream_kinds }}
```

{{ p_stream_kinds_note }}

{{ p_instruction_docs }}
