# Testing and Stability Checklist

## Devices
- Test on Android 9, 10, 12, 14 devices where possible

## Functional Tests
- Source selection: Camera, Local Video, Network (RTSP/HLS/HTTP), Image
- Transformations: pinch zoom, rotate gesture, pan, flip, reset
- Overlays: logo selection + drag + opacity; text + drag + opacity; timestamp
- PiP: enable, select source, drag window
- Streaming: set RTMP URL, start, verify dashboard receives stream, stop
- Recording: start, run 5+ minutes, stop, play back MP4 (H.264+AAC)

## Stability
- ForegroundService runs with notification; app remains active 30+ minutes
- WakeLock + WifiLock acquired during streaming/recording

## Privacy
- Open another camera app while this app is idle → banner warns about camera in use

## Settings
- Change bitrate/resolution/fps; verify applied to stream/record
- Change storage directory and retention; verify files saved and retention applied

## Logs
- Use Logcat to monitor RTMP connection status and errors

## Known Issues
- Overlays are not composited into RTMP/recording output (on-screen only)
