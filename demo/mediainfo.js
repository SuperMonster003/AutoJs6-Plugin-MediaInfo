// Run in AutoJs6 with the Rhino engine and the MediaInfo plugin installed.
// Copy app/src/androidTest/assets/mediainfo-two-audio.mka to the path below,
// or change the path to a media file that AutoJs6 has permission to read.
let path = "/sdcard/two-audio.mka";

console.clear();
console.launch();
console.info("MediaInfo | Rhino");
console.log("Complete name:");
console.log(mediainfo.get(path, "general", "CompleteName"));
console.log("Format: " + mediainfo.get(path, "general", "Format"));

let count = mediainfo.countGet(path, "audio");
console.log("Audio streams: " + count);
for (let index = 0; index < count; index++) {
    let options = { streamNumber: index };
    let rate = mediainfo.get(path, "audio", "SamplingRate", options);
    let unit = mediainfo.get(path, "audio", "SamplingRate", {
        streamNumber: index,
        infoKind: "MEASURE",
    }).trim();
    console.info("Audio #" + (index + 1));
    console.log("Sampling rate: " + rate + " " + unit);
    console.log("Channels: " + mediainfo.get(path, "audio", "Channels", options));
}

console.log("Engine: " + mediainfo.capabilities().engineVersion);
