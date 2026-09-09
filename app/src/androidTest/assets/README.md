# MediaInfo query fixture

`mediainfo-two-audio.mka` contains 100 ms of silent PCM in two Matroska audio tracks:
8 kHz mono (English) and 16 kHz stereo (Chinese). It contains no third-party media.

Regenerate with FFmpeg:

```shell
ffmpeg -f lavfi -i anullsrc=r=8000:cl=mono -f lavfi -i anullsrc=r=16000:cl=stereo -map 0:a -map 1:a -t 0.1 -c:a pcm_s16le -metadata:s:a:0 language=eng -metadata:s:a:1 language=zho mediainfo-two-audio.mka
```
