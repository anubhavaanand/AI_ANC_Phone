# System Architecture

## Overview

AI ANC Phone implements a real-time audio processing pipeline that augments existing Bluetooth earbud ANC with AI-powered noise suppression.

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Android Application                     │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐         ┌─────────────────┐               │
│  │  MainActivity │────────▶│  AudioService   │               │
│  │              │         │  (Foreground)   │               │
│  └──────────────┘         └─────────────────┘               │
│         │                          │                          │
│         │                          │                          │
│         ▼                          ▼                          │
│  ┌──────────────┐         ┌─────────────────┐               │
│  │ BootReceiver │         │   RNNoise JNI   │               │
│  │              │         │     Wrapper     │               │
│  └──────────────┘         └─────────────────┘               │
│                                    │                          │
└────────────────────────────────────┼──────────────────────────┘
                                     │
                         ┌───────────▼───────────┐
                         │   Native Layer (C++)   │
                         ├────────────────────────┤
                         │   RNNoise Library      │
                         │   (AI Model)           │
                         └────────────────────────┘
                                     │
                         ┌───────────▼───────────┐
                         │   Android Audio APIs   │
                         ├────────────────────────┤
                         │   AudioRecord (Input)  │
                         │   AudioTrack (Output)  │
                         └────────────────────────┘
                                     │
                                     ▼
                         ┌────────────────────────┐
                         │   Bluetooth Earbuds    │
                         │   + Hardware ANC       │
                         └────────────────────────┘
```

## Component Details

### 1. MainActivity

**Purpose**: User interface and app lifecycle management

**Responsibilities**:
- Request runtime permissions (microphone, Bluetooth, notifications)
- Display service status
- Toggle AI ANC on/off
- Manage auto-start preferences
- Check if AudioService is running

**Key Methods**:
- `checkPermissions()`: Verify all required permissions
- `startAudioService()`: Launch the foreground service
- `stopAudioService()`: Stop the service
- `isServiceRunning()`: Check service status
- `updateUI()`: Refresh UI based on service state

### 2. AudioService

**Purpose**: Foreground service for continuous audio processing

**Responsibilities**:
- Run as a foreground service to survive app backgrounding
- Capture microphone audio via AudioRecord
- Process audio frames through RNNoise
- Output processed audio via AudioTrack
- Maintain persistent notification
- Handle START_STICKY for automatic restart

**Audio Pipeline**:
```kotlin
AudioRecord (48kHz, MONO, FLOAT)
    ↓
Read 480 samples (10ms frame)
    ↓
RNNoise.process(frame)
    ↓
AudioTrack (48kHz, MONO, FLOAT)
    ↓
Bluetooth output
```

**Configuration**:
- Sample Rate: 48000 Hz
- Frame Size: 480 samples (10ms @ 48kHz)
- Audio Format: PCM Float
- Channel: Mono
- Buffer Strategy: Double buffering for low latency

### 3. RNNoise JNI Wrapper

**Purpose**: Bridge between Kotlin and native RNNoise library

**Responsibilities**:
- Load native library (`libaianc.so`)
- Create/destroy RNNoise state
- Process audio frames via JNI

**Native Methods**:
```kotlin
external fun create(): Long
external fun destroy(handle: Long)
external fun processFrame(handle: Long, inOut: FloatArray): Float
```

### 4. Native Layer (C++)

**File**: `native-lib.cpp`

**JNI Functions**:
- `Java_com_example_aianc_RNNoise_create`: Initialize RNNoise
- `Java_com_example_aianc_RNNoise_destroy`: Cleanup resources
- `Java_com_example_aianc_RNNoise_processFrame`: Process one frame

**RNNoise Integration**:
```cpp
DenoiseState *st = rnnoise_create(NULL);
float vad = rnnoise_process_frame(st, input, output);
rnnoise_destroy(st);
```

### 5. RNNoise Library

**Source**: Xiph.Org Foundation

**Model Details**:
- Size: 38KB
- Architecture: Recurrent Neural Network
- Input: 480 samples @ 48kHz (10ms)
- Output: Denoised audio + VAD probability
- Latency: <1ms per frame

**Files Used**:
- `denoise.c`: Main denoising logic
- `rnn.c`: RNN implementation
- `rnn_data.c`: Pre-trained weights
- `pitch.c`: Pitch detection
- `kiss_fft.c`: FFT implementation
- `celt_lpc.c`: LPC analysis

### 6. BootReceiver

**Purpose**: Auto-start service on device boot

**Responsibilities**:
- Listen for `BOOT_COMPLETED` broadcast
- Check user preferences for auto-start
- Launch AudioService if enabled

**SharedPreferences**:
- Key: `auto_start_enabled`
- File: `ai_anc_prefs`

## Data Flow

### Audio Processing Flow

1. **Capture** (AudioRecord Thread):
   ```
   Microphone → AudioRecord API → Float buffer [480 samples]
   ```

2. **Process** (Native Layer):
   ```
   Float buffer → JNI → RNNoise C++ → Denoised buffer → JNI → Kotlin
   ```

3. **Output** (AudioTrack Thread):
   ```
   Denoised buffer → AudioTrack API → Audio HAL → Bluetooth
   ```

### Service Lifecycle

```
User taps "Start AI ANC"
    ↓
MainActivity.startAudioService()
    ↓
AudioService.onCreate()
    ↓
AudioService.startForegroundService()
    ↓
Show notification
    ↓
AudioService.startAudioPipeline()
    ↓
Create AudioRecord + AudioTrack
    ↓
Initialize RNNoise
    ↓
Start processing loop
    ↓
[Processing until stopped]
    ↓
User taps "Stop AI ANC"
    ↓
AudioService receives STOP action
    ↓
Stop AudioRecord/AudioTrack
    ↓
Release resources
    ↓
Service terminates
```

## Threading Model

### Main Thread
- UI updates
- User interactions
- Service lifecycle

### Audio Processing Thread
```kotlin
thread {
    while (isRunning) {
        audioRecord.read(buffer)  // Blocking
        rnNoise.process(buffer)
        audioTrack.write(buffer)
    }
}
```

**Critical**: Processing must complete in <10ms to avoid audio glitches

## Performance Characteristics

### Latency Budget

| Component | Latency |
|-----------|---------|
| AudioRecord capture | 10-15ms |
| Buffer transfer | 1-2ms |
| JNI overhead | <1ms |
| RNNoise processing | <1ms |
| AudioTrack output | 10-20ms |
| **Total** | **~35-50ms** |

### Memory Usage

- RNNoise state: ~500KB
- Audio buffers: 480 × 4 bytes × 2 = ~4KB
- Heap: <80MB
- Native: <2MB

### CPU Usage

- Single core: 6-10% (Snapdragon 888)
- Primarily in native processing
- Frame processing: ~0.5-0.8ms

## Security & Privacy

### On-Device Processing
- 100% local computation
- No network requests
- No cloud dependencies
- Audio never leaves device

### Permissions

| Permission | Purpose | Required? |
|------------|---------|-----------|
| RECORD_AUDIO | Microphone access | Yes |
| FOREGROUND_SERVICE_MICROPHONE | Continuous capture (API 34+) | Yes |
| POST_NOTIFICATIONS | Show persistent notification (API 33+) | Yes |
| BLUETOOTH_CONNECT | Bluetooth routing (API 31+) | Yes |
| RECEIVE_BOOT_COMPLETED | Auto-start | Optional |

## Build System

### CMake Configuration

```cmake
- Compile native-lib.cpp
- Include RNNoise sources
- Link math library (-lm)
- Link Android log library
- Output: libaianc.so
```

### Gradle Build

1. Compile Kotlin sources
2. Trigger CMake build for native
3. Package APK with native libraries
4. Sign APK

## Deployment

### Supported Configurations

- **Minimum SDK**: API 29 (Android 10)
- **Target SDK**: API 34 (Android 14)
- **Architectures**: arm64-v8a, armeabi-v7a, x86, x86_64

### Distribution

- Direct APK installation
- Future: Google Play Store
- Beta testing via Firebase App Distribution

## Future Enhancements

### Planned Improvements

1. **Adaptive Processing**: Adjust suppression based on environment
2. **Multi-Model Support**: Switch between RNNoise, DeepFilterNet, NSNet2
3. **Profile Modes**: Presets for calls, music, study, travel
4. **Stats Dashboard**: Real-time CPU, latency, noise level
5. **Internal Audio**: Capture app audio (requires Android 10+)

### Research Areas

- Lower latency (<20ms)
- Reduced battery consumption
- On-device model training
- Stereo support
- Multi-microphone fusion

---

**Last Updated**: December 2025  
**Version**: 1.0.0-MVP
