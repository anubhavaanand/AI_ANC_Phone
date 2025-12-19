# Build & Deploy Guide

## Prerequisites

### Required Software

1. **Android Studio** (Hedgehog 2023.1.1 or newer)
   - Download: https://developer.android.com/studio
   
2. **Android NDK** (25.0 or newer)
   - Install via Android Studio: SDK Manager → SDK Tools → NDK

3. **Git**
   - Linux: `sudo apt install git`
   - macOS: `brew install git`
   - Windows: https://git-scm.com/download/win

4. **Java Development Kit (JDK 17)**
   - Bundled with Android Studio, or
   - Download: https://adoptium.net/

### Hardware Requirements

- **Development Machine**: 8GB+ RAM, 10GB+ free disk space
- **Test Device**: Android 10+ smartphone or tablet

## Initial Setup

### 1. Clone the Repository

```bash
git clone --recurse-submodules https://github.com/yourusername/AI_ANC_Phone.git
cd AI_ANC_Phone
```

**Note:** The `--recurse-submodules` flag automatically clones the RNNoise library submodule.

If you already cloned without this flag, initialize the submodule:

```bash
git submodule update --init --recursive
```

**Troubleshooting:** If switching from a manual RNNoise clone to the submodule, clean your build first:
```bash
./gradlew clean
# or in Android Studio: Build → Clean Project
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to `AI_ANC_Phone` directory
4. Click **OK**

Android Studio will:
- Sync Gradle dependencies
- Configure NDK build
- Index project files

## Building the Project

### Debug Build (Development)

#### Via Android Studio

1. Select **Build → Make Project** (Ctrl+F9 / Cmd+F9)
2. Wait for Gradle sync and CMake build
3. Check **Build** panel for errors

#### Via Command Line

```bash
# Linux/macOS
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build (Production)

#### 1. Generate Signing Key

```bash
keytool -genkey -v -keystore ai-anc-release.keystore \
  -alias ai-anc -keyalg RSA -keysize 2048 -validity 10000
```

Save keystore in a secure location (NOT in the repository).

#### 2. Create keystore.properties

Create `keystore.properties` in project root:

```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=ai-anc
storeFile=/path/to/ai-anc-release.keystore
```

Add to `.gitignore`:
```bash
echo "keystore.properties" >> .gitignore
```

#### 3. Update app/build.gradle

Add signing config:

```gradle
android {
    ...
    signingConfigs {
        release {
            def keystorePropertiesFile = rootProject.file("keystore.properties")
            def keystoreProperties = new Properties()
            keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
            
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

#### 4. Build Release APK

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Installing on Device

### Via Android Studio

1. Connect device via USB
2. Enable **USB Debugging** on device:
   - Settings → About Phone → Tap "Build Number" 7 times
   - Settings → Developer Options → USB Debugging
3. Click **Run** button (Shift+F10 / Ctrl+R)

### Via ADB (Command Line)

```bash
# Install debug build
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release build
adb install app/build/outputs/apk/release/app-release.apk

# Reinstall (keep data)
adb install -r app-debug.apk
```

### Via File Transfer

1. Copy APK to device
2. Open file manager on device
3. Tap APK file
4. Allow "Install from Unknown Sources" if prompted
5. Tap **Install**

## Testing

### Manual Testing Checklist

- [ ] App launches successfully
- [ ] Permissions requested on first run
- [ ] "Start AI ANC" button works
- [ ] Service notification appears
- [ ] Audio processing audible via earbuds
- [ ] "Stop AI ANC" button works
- [ ] Auto-start toggle saves preference
- [ ] Service survives app backgrounding
- [ ] Service survives screen lock
- [ ] Boot receiver works (if enabled)

### Checking Logs

```bash
# View all app logs
adb logcat | grep "com.example.aianc"

# View specific tags
adb logcat AudioService:D RNNoise:D MainActivity:D *:S

# Clear logs
adb logcat -c
```

### Performance Testing

```bash
# CPU usage
adb shell top | grep aianc

# Memory usage
adb shell dumpsys meminfo com.example.aianc

# Service status
adb shell dumpsys activity services com.example.aianc

# Battery stats
adb shell dumpsys batterystats com.example.aianc
```

## Troubleshooting

### Common Build Errors

#### 1. NDK not found

**Error**: `No version of NDK matched the requested version`

**Solution**:
```bash
# Android Studio: SDK Manager → SDK Tools → NDK (check)
# Or set in local.properties:
ndk.dir=/path/to/Android/sdk/ndk/25.0.8775105
```

#### 2. RNNoise not found

**Error**: `rnnoise.h: No such file or directory`

**Solution**:
```bash
cd app/src/main/cpp
git clone https://github.com/xiph/rnnoise.git
```

#### 3. CMake version mismatch

**Error**: `CMake version X.X.X is different from specified`

**Solution**: Update `app/build.gradle`:
```gradle
externalNativeBuild {
    cmake {
        version "3.22.1"  // Match your installed version
    }
}
```

### Runtime Issues

#### 1. Service not starting

**Check**:
- Permissions granted?
- Microphone not in use by another app?
- Check logs: `adb logcat | grep AudioService`

#### 2. Audio playback issues

**Check**:
- Bluetooth earbuds connected?
- Volume not muted?
- Phone call in progress?

#### 3. Service killed

**Check**:
- Battery optimization disabled for app
- Auto-start permission granted (Xiaomi, Oppo)
- Foreground service notification visible

## Continuous Integration (Future)

### GitHub Actions Example

```yaml
name: Android CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Setup Android SDK
        uses: android-actions/setup-android@v2
      - name: Build with Gradle
        run: ./gradlew assembleDebug
```

## Deployment

### Google Play Store (Future)

1. **Create Google Play Console Account**
2. **Prepare Store Listing**:
   - App name, description
   - Screenshots (phone, tablet)
   - Feature graphic (1024x500)
   - App icon (512x512)
3. **Upload APK/AAB**
4. **Internal Testing** → **Closed Testing** → **Open Testing** → **Production**

### Firebase App Distribution (Beta Testing)

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Distribute
firebase appdistribution:distribute app-release.apk \
  --app YOUR_FIREBASE_APP_ID \
  --groups "beta-testers"
```

---

**Build System**: Gradle 8.0+  
**Last Updated**: December 19, 2025
