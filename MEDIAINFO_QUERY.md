# MediaInfo multi-stream queries and source identity

The query extension keeps the original AIDL transaction order (`getInfo`,
`inform`, `get`, `snapshot`) and appends `getDetail` and `countGet`. The original
`get` already carries a zero-based stream number. Missing script options keep
stream 0 and `TEXT`. The native bridge and read-only descriptor transport are
unchanged.

| Operation | Rhino | Node |
| --- | --- | --- |
| Field value | `mediainfo.get(path, "audio", "SamplingRate", {streamNumber: 1})` | `await mi.get(path, "audio", "SamplingRate", {streamNumber: 1})` |
| Unit | `mediainfo.get(path, "audio", "SamplingRate", {streamNumber: 1, infoKind: "MEASURE"})` | `await mi.get(path, "audio", "SamplingRate", {streamNumber: 1, infoKind: "MEASURE"})` |
| Number of streams | `mediainfo.countGet(path, "audio")` | `await mi.countGet(path, "audio")` |

Node uses `const mi = require("mediainfo")`. The compat facade exposes the same
async methods. Existing Rhino stream accessors also accept query options, for
example `mediainfo(path).audio("SamplingRate", {streamNumber: 1})`.

`streamNumber` must be an integer in 0..2147483647. Fractions, negative values,
non-finite numbers and numeric strings are rejected. Supported info kinds are
`NAME`, `TEXT`, `MEASURE`, `OPTIONS`, `NAME_TEXT`, `MEASURE_TEXT`, `INFO`, `HOWTO`
and `DOMAIN`, case-insensitively. Unknown kinds and `MAX` are rejected.

`countGet` returns the number of streams, including zero when a kind is absent;
native open failure returns -1. It does not expose the separate native field-count
overload. Missing streams and fields queried through `get` return an empty string.

## Compatibility

- Plugin capability `streamCount=true` enables `countGet`; `infoKinds` lists
  supported detailed query kinds. Old plugins retain `TEXT` and the original
  indexed `get` transaction.
- Updated host script capabilities include `streamNumber=true`. Node checks this
  together with `infoKinds` before sending a non-default query, so an older host
  cannot silently ignore the requested stream or unit.
- Unsupported extensions fail with `MEDIAINFO_QUERY_UNSUPPORTED`. New AIDL
  transactions are sent only after negotiation. The minimum host build remains
  3923 because original transactions and default snapshot behavior still work.
- Query cache keys distinguish stream number and info kind. Count queries use a
  separate internal key. Existing size, TTL, timeout and descriptor cleanup
  limits apply to every new query.
- `Info_Parameters` remains deferred and is not included in discovery Bundles.

## Original source identity

The native report used to put the parser's `/proc/self/fd/...` or private copy
path into `Complete name`. The plugin now replaces that field with the caller's
source identity before caching or creating snapshot-v1 sections. It never uses
this display identity to open a file; media bytes still come only from the FD.

`inform` and field queries receive the absolute source path as their display
identity. Snapshot requests keep the original display filename and separately
send the optional `sourceName` Bundle key. Snapshot cache identity includes both
names, so different source paths cannot share a report containing the wrong path.
The `fileName` (v1) and `file.name` (v2) contracts are unchanged.

The host also repairs `inform` and the `sections.file/general.completeName`
fields returned by an older plugin that ignores `sourceName`. This covers the
media dialog's neutral-button details page. Line breaks in a filename are escaped
inside the text report; other metadata and line endings are preserved.

## Validation

- Shared API tests cover strict option parsing and report replacement with
  Unicode, colon, replacement metacharacters and embedded line breaks.
- Plugin tests cover direct and pipe inputs, both snapshot schemas, path cache
  isolation, descriptor cleanup on invalid options, absent streams and two
  distinct PCM audio tracks with cold and cached queries.
- Host tests cover the production Rhino engine, real Node provider, direct and
  compat facades, old-host negotiation and old-plugin snapshot normalization.

The two-audio Matroska fixture and its generation command are stored in
`app/src/androidTest/assets/`.

The 2026-09-10 validation record is
`benchmark/results/2026-09-10-mediainfo-m2-validation.json`. It binds the plugin
source, companion repository commits, fixture hash and five signed APK hashes.
The final plugin passed 9 service tests on each of ARM64/API 31 and x86_64/API 36,
plus public-contract minified Release checks on both. The host passed all 7 tests
in the MediaInfo Rhino and Node media integration classes; Node Runtime passed
the full 12-test NPM and Android conformance group.

A supplementary Samsung SM-A566B / API 36 ARM64 run used a physical 16 KiB-page
system and the same signed v2.1.0 (11) APK. All 9 service tests, the explicitly
enabled stalled-pipe timeout test and the minified Release smoke test passed.
The debug test process's three native-library mappings each reported 16 KiB
kernel and MMU pages. Exact APK hashes, native alignment, cases and raw logs are
recorded in `benchmark/results/2026-09-10-api36-arm64-v8a-16k-samsung.json`.

That earlier host baseline needed an external init script to exclude two stale
console tests. The integrated host now updates both tests to the current themed
input bar API and layout, compiles the complete Android test source set, and
passes all seven console cases. No exclusion remains in the integrated build.

The final integration record is
`benchmark/results/2026-09-10-mediainfo-main-integration.json`. On each of the new
Samsung ARM64 rental and an x86_64 emulator, both running API 36 with 16 KiB
kernel pages, all 17 selected host cases passed: eight Rhino/Node media cases,
seven console cases and two light/dark UI cases. The UI cases inject an actual
touch at the neutral button and assert the rendered report's source path.
The new Samsung also passed all 13 current NPM/conformance cases. The final
v2.1.0 (12) minified plugin passed its six-method public AIDL smoke test on each
architecture. README screenshots and the runnable two-track demo are in
`.readme/images/` and `demo/mediainfo.js`.

The Samsung showed the host's first-launch page-size compatibility warning:
three legacy terminal libraries in the host APK still have 4 KiB ELF alignment.
The warning was acknowledged before the touch tests; host compatibility mode
was not disabled. This limits the host's native 16 KiB readiness claim. The
MediaInfo plugin has independently audited 16 KiB alignment and uses the same
native library bytes as the earlier physical-device validation.

## Coordinated integration

The companion changes have been integrated into the main working branches of
AutoJs6, Node Runtime, Documentation, TypeScript Declarations, Ace Editor and
Offline Documentation. Host commit `b3b3b29dc` and Node commit `0789b35` preserve
the newer Android file-path behavior while adding query negotiation. The other
four repositories were fast-forwarded to their validated companion commits.
Exact revisions are in the integration JSON. Original isolated worktrees remain
available; pre-existing Offline Documentation platform edits were preserved.

The full source path fix includes the host change: an older host that sends only
a basename cannot supply the original full path to a new plugin. The updated
host repairs reports from both old and new plugins. Install or integrate the
host update together with the plugin when validating the media details page.

Companion versions are Node Runtime 1.4.0 (154), declarations 4.9.0, Ace Editor
1.1.24 (35), documentation 6.8.0 (60) and Offline Documentation 6.8.0 (19).
