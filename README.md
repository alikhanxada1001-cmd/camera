# Virtual Camera: Live Assist

Android app (minSdk 28, compileSdk 34) built with Kotlin, MVVM + StateFlow, and Jetpack Compose. It lets you select different video sources (CameraX, local video, network stream, image), apply live transformations, add overlays and PiP, and output to RTMP or local MP4 recording. A ForegroundService with WakeLock/WifiLock provides stability, and a Privacy Assist warns when another app may be using the camera.

## Features
- Sources: CameraX preview, local video (MP4), network streams (RTSP/HLS/HTTP via ExoPlayer), image
- Transforms: zoom (pinch), rotate (gesture or button), pan, flip H/V, reset
- Overlays: logo (draggable, opacity), custom text (draggable, opacity), timestamp
- PiP: render a secondary source as draggable Picture-in-Picture layer
- Output: RTMP streaming (YouTube/Twitch/custom) and local MP4 recording (H.264 + AAC)
- Stability: ForegroundService with WakeLock + WifiLock; simple auto-start/stop with actions
- Privacy Assist: monitors camera availability via CameraManager and shows warning banner
- Settings: bitrate, resolution, fps, encoder choice placeholder, storage path + retention policy

## Requirements
- Android Studio Hedgehog+ (AGP 8.6+), JDK 17
- Android 9+ device (API 28+)

## Permissions
- CAMERA, RECORD_AUDIO, INTERNET, FOREGROUND_SERVICE, WAKE_LOCK, ACCESS_NETWORK_STATE
- Storage: READ/WRITE_EXTERNAL_STORAGE on older devices; scoped storage is used via public Movies directory path by default

## Getting Started
1. Open this folder in Android Studio and let Gradle sync.
2. Connect a real Android device (USB debugging enabled) and Run.
3. On first launch, grant Camera and Microphone permissions.

## Usage
1. Source selection
   - Camera: starts CameraX preview
   - Local Video: tap Local Video and choose an MP4 from storage
   - Network: paste RTSP/HLS/HTTP URL and press Play
   - Image: choose any image to display
2. Transformations
   - Pinch to zoom, rotate gesture to rotate, drag to pan; use buttons to flip or reset
3. Overlays
   - Pick a logo image and adjust opacity; type custom text; toggle timestamp; drag to reposition
   - Enable PiP and choose a PiP source (e.g., Camera)
4. Streaming (RTMP)
   - Enter RTMP URL (e.g., `rtmp://a.rtmp.youtube.com/live2/STREAM_KEY`) and press Set URL
   - Press Start Stream to begin; Stop Stream to end
5. Recording (MP4)
   - Press Start Record to begin; Stop Record to end
   - Files are saved under `Movies/VirtualCamera` by default with retention applied
6. Settings
   - Open Settings, adjust bitrate (kbps), fps, width/height, storage dir, retention count; Save Settings

## RTMP Targets
- YouTube Live: `rtmp://a.rtmp.youtube.com/live2/<stream_key>`
- Twitch: `rtmp://live.twitch.tv/app/<stream_key>`
- Custom: e.g., Nginx-RTMP or other servers

## Recording
- Container: MP4
- Video: H.264 (hardware MediaCodec via library)
- Audio: AAC
- Retention: keeps most recent N files in the storage directory (default 20)

## Stability
- A ForegroundService with persistent notification runs during streaming/recording, holding a partial WakeLock and high-perf WifiLock
- Simple reconnection behavior is provided by start/stop controls; you can extend with retries as needed

## Privacy Assist
- Uses CameraManager availability callbacks to detect when the camera becomes unavailable, and shows a red banner warning that another app may be using the camera

## Signing & Release
- Debug builds run out-of-the-box
- For a release APK:
  1. In Android Studio: Build > Generate Signed Bundle/APK…
  2. Create or choose a keystore and key, remember passwords
  3. Select `app` module and build `APK (Release)`
  4. Upload/sign as per store requirements

## Building From CLI
- Assemble Debug: `./gradlew :app:assembleDebug`
- Assemble Release: `./gradlew :app:assembleRelease`

## Known Limitations
- Software encoder fallback (FFmpeg) is not wired; MediaCodec hardware path is used via the streaming library
- Overlay rendering for the encoded output path is not yet composited into the RTMP/recording pipeline (shown on-screen). Integrating overlays into the OpenGL pipeline is the next step if needed
- Auto-reconnect logic is basic; extend in `StreamingController`

## Dependencies and Licenses
See `THIRD_PARTY_NOTICES.md` for a list of third-party libraries and licenses.

## Troubleshooting
- Black screen on Camera: ensure Camera permission granted and no other app holds the camera
- RTMP fails to connect: verify URL and stream key; check firewall/network; try lower bitrate
- Recording path: check `Movies/VirtualCamera` (or your configured storage path)

## Roadmap Ideas
- Composite overlays and transforms directly into OpenGL pipeline for encoded output
- Add auto-reconnect with backoff for network streams and RTMP
- Add software encoding fallback via Mobile-FFmpeg if hardware fails
