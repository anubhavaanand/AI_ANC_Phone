# ✅ First Build Checklist

## Before Opening in Android Studio

- [ ] Android Studio installation completed
- [ ] Project location: `/home/anubhavanand/AI_ANC_Phone`
- [ ] Internet connection active (for downloads)
- [ ] At least 15GB free disk space

---

## Opening the Project

- [ ] Launch Android Studio
- [ ] Click "Open" (not "New Project")
- [ ] Navigate to `/home/anubhavanand/AI_ANC_Phone`
- [ ] Click OK

---

## Initial Sync (Wait for completion!)

- [ ] Gradle sync started (bottom right notification)
- [ ] Gradle sync completed (may take 2-5 minutes)
- [ ] No sync errors (check "Build" tab at bottom)
- [ ] Indexing completed (bottom status bar shows "Ready")

---

## Install Required SDK Components

Open: **Tools → SDK Manager** (or Ctrl+Alt+S)

### SDK Platforms Tab:
- [ ] Android 14.0 (API Level 34) - TIRAMISU
- [ ] Android 10.0 (API Level 29) - Q

### SDK Tools Tab:
- [ ] Android SDK Build-Tools
- [ ] NDK (Side by side) version 25.0+
- [ ] CMake version 3.22.1+
- [ ] Android SDK Platform-Tools

Click **Apply** and wait for all downloads to complete!

---

## Verify Project Structure

Check that these files exist (Project view on left):

- [ ] `app/src/main/java/com/example/aianc/MainActivity.kt`
- [ ] `app/src/main/java/com/example/aianc/AudioService.kt`
- [ ] `app/src/main/java/com/example/aianc/RNNoise.kt`
- [ ] `app/src/main/java/com/example/aianc/BootReceiver.kt`
- [ ] `app/src/main/cpp/native-lib.cpp`
- [ ] `app/src/main/cpp/CMakeLists.txt`
- [ ] `app/src/main/cpp/rnnoise/` (directory)
- [ ] `app/build.gradle`

---

## Build the APK

### Method: Using Menu
- [ ] Click: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
- [ ] Wait for build to complete (3-15 minutes first time)
- [ ] Watch progress in "Build" tab at bottom
- [ ] Build successful notification appears
- [ ] Click "locate" in notification

### Verify APK Created:
- [ ] File exists: `app/build/outputs/apk/debug/app-debug.apk`
- [ ] File size: ~10-15 MB
- [ ] File is ready to install

---

## Install on Android Device

### Prepare Phone:
- [ ] Enable Developer Options (Settings → About → Tap Build Number 7 times)
- [ ] Enable USB Debugging (Settings → Developer Options)
- [ ] Connect phone via USB cable
- [ ] Phone shows up in Android Studio (top toolbar)

### Install APK:
Choose ONE method:

#### Option A: Direct Run from Android Studio
- [ ] Click Run → Run 'app' (or Shift+F10)
- [ ] Select your device from list
- [ ] App installs and launches automatically

#### Option B: Manual Install
- [ ] Copy `app-debug.apk` to phone
- [ ] Open APK file on phone
- [ ] Enable "Install from Unknown Sources" if prompted
- [ ] Tap "Install"
- [ ] Tap "Open" to launch app

---

## Test the App

- [ ] App launches successfully
- [ ] Permissions dialog appears
- [ ] Grant Microphone permission
- [ ] Grant Bluetooth permission
- [ ] Grant Notifications permission
- [ ] Connect Bluetooth earbuds
- [ ] Tap "Start AI ANC" button
- [ ] Status changes to "Active ✓" (green)
- [ ] Persistent notification appears
- [ ] Make a test call to verify noise reduction

---

## Troubleshooting (If needed)

### If Gradle Sync Fails:
- [ ] File → Invalidate Caches / Restart
- [ ] Tools → SDK Manager → Install missing components
- [ ] File → Sync Project with Gradle Files

### If Build Fails:
- [ ] Check "Build" tab for error messages
- [ ] Build → Clean Project
- [ ] Build → Rebuild Project
- [ ] Verify NDK and CMake are installed

### If App Crashes:
- [ ] View → Tool Windows → Logcat
- [ ] Filter by "com.example.aianc"
- [ ] Look for error messages (red lines)

---

## Success Indicators ✅

You're successful when you see:

- ✅ "BUILD SUCCESSFUL" in Build tab
- ✅ APK file exists in `app/build/outputs/apk/debug/`
- ✅ App installs on phone without errors
- ✅ App launches and shows UI
- ✅ Permissions can be granted
- ✅ "Start AI ANC" button works
- ✅ Service notification appears
- ✅ Audio processing works (reduced noise on calls)

---

## Timeline Expectations

| Task | First Time | Subsequent |
|------|-----------|------------|
| Opening Project | 30 sec | 10 sec |
| Gradle Sync | 2-5 min | 10-30 sec |
| SDK Download | 5-10 min | - |
| First Build | 5-15 min | 1-3 min |
| Install APK | 30 sec | 30 sec |
| **TOTAL** | **~20-35 min** | **~3-5 min** |

---

## Quick Reference

### Build Command:
**Build → Build Bundle(s) / APK(s) → Build APK(s)**

### APK Location:
`/home/anubhavanand/AI_ANC_Phone/app/build/outputs/apk/debug/app-debug.apk`

### Install Command (Terminal):
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

**You're ready to build!** 🚀

Follow each checkbox in order, and you'll have your APK ready to use! ✨
