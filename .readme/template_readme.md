<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="{{ repo_url }}/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="{{ repo_url }}/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="{{ icon_alt }}" border="0" width="128" />
    </picture>
  </p>

  <p>{{ text_plugin_synopsis }}</p>

  <p>
    <a href="{{ repo_url }}/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/{{ repo_slug }}?label=Release"/></a>
    <a href="{{ repo_url }}/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/{{ repo_slug }}?color=A24232&label=Issues"/></a>
    <a href="{{ repo_url }}/commit/9319767358b7e53d1c401bfa4f1d818ceb65df38"><img alt="Created" src="https://img.shields.io/date/1783211498?color=2e7d32&label=Created"/></a>
    <a href="{{ license_url }}"><img alt="GitHub License" src="https://img.shields.io/github/license/{{ repo_slug }}?color=534BAE&label=License"/></a>
  </p>
</div>

******

### {{ h3_languages_with_ascii }}

******

{{ p_languages_all_supported_for_readme }}:

{{ placeholder_ul_languages_all_supported }}

******

### {{ h3_introduction }}

******

{{ p_introduction_what }}

{{ p_introduction_how }}

******

### {{ h3_features }}

******

{{ placeholder_features }}

******

### {{ h3_usage }}

******

{{ placeholder_usage_steps }}

> {{ p_usage_note }}

******

### {{ h3_choose_apk }}

******

{{ p_choose_apk_intro }}:

| {{ th_apk_variant }} | {{ th_apk_target }} |
|---|---|
| `arm64-v8a` | {{ td_abi_arm64 }} |
| `armeabi-v7a` | {{ td_abi_arm32 }} |
| `x86_64` | {{ td_abi_x86_64 }} |
| `x86` | {{ td_abi_x86 }} |
| `universal` | {{ td_abi_universal }} |

{{ p_choose_apk_note }}

******

### {{ h3_script_api }}

******

{{ p_script_api_node }}:

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

{{ p_script_api_node_note }}

{{ p_script_api_rhino }}:

```javascript
const mi = mediainfo("/sdcard/Download/sample.mp4");

console.log(mi.general.format);
console.log(mi.video.width);
console.log(mi.audio("BitRate"));
```

{{ p_script_api_rhino_note }}

******

### {{ h3_snapshot }}

******

{{ p_snapshot_intro }}:

```json
{
  "schema": "{{ snapshot_schema_node }}",
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

{{ placeholder_snapshot_options }}

{{ p_snapshot_sections_note }}

******

### {{ h3_stream_kinds }}

******

{{ p_stream_kinds }}:

```text
{{ stream_kinds }}
```

{{ p_stream_kinds_note }}

******

### {{ h3_faq }}

******

{{ placeholder_faq }}

******

### {{ h3_security }}

******

{{ p_security_intro }}

{{ placeholder_security_points }}

{{ p_security_permission }}

******

### {{ h3_plugin_interface }}

******

{{ p_plugin_interface }}:

```text
application id: {{ plugin_application_id }}
plugin id: {{ plugin_id }}
engine: {{ plugin_engine }}
variant: {{ plugin_variant }}
discovery action: {{ discovery_action }}
discovery category: {{ discovery_category }}
wake action: {{ wake_action }}
binder interface: {{ binder_interface }}
minimum host build: {{ required_host_version_code }}
native library: {{ native_library_file }}
snapshot schema: {{ snapshot_schema_plugin }}
```

{{ p_contract_service }}

{{ p_abi_reporting }}

******

### {{ h3_roadmap }}

******

{{ p_roadmap }}

- [{{ text_link_roadmap }}]({{ roadmap_url }})

******

### {{ h3_release_history }}

******

{{ placeholder_latest_release_history }}

##### {{ h5_for_more_release_history }}

* {{ placeholder_read_more_in_changelog_md }}

******

### {{ h3_build }}

******

{{ p_build_intro }}

{{ p_build_debug }}:

```powershell
.\gradlew.bat :app:assembleDebug
```

{{ p_build_release }}:

```powershell
.\gradlew.bat :app:assembleRelease
```

{{ p_build_artifacts }}

{{ p_build_params }}

******

### {{ h3_resource_layout }}

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

{{ p_resource_layout }}

******

### {{ h3_license }}

******

{{ p_license }}

******

### {{ h3_links }}

******

- {{ text_link_autojs6_docs_mediainfo }}: {{ docs_mediainfo_url }}
- {{ text_link_mediainfo_official }}: {{ mediainfo_official_url }}
- {{ text_link_mediainfolib_official }}: {{ mediainfolib_url }}
- {{ text_link_mediainfo_android }}: {{ mediainfo_android_url }}
