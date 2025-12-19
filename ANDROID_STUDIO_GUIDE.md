# Android Studio Quick Start Guide

## 🚀 Opening Your Project in Android Studio

Once Android Studio finishes downloading and installing, follow these steps:

---

## 📂 **Step 1: Open the Project**

### Launch Android Studio:
```bash
# If installed via yay/AUR:
android-studio

# Or find it in your applications menu
```

### First Time Setup:
1. **Welcome Screen** will appear
2. Click **"Open"** (not "New Project")
3. Navigate to: `/home/anubhavanand/AI_ANC_Phone`
4. Click **"OK"**

---

## ⏳ **Step 2: Wait for Initial Sync (Important!)**

Android Studio will now:

### 1️⃣ **Gradle Sync** (2-5 minutes)
- Downloads Gradle wrapper
- Downloads project dependencies
- Syncs build configuration
- **Status**: Watch bottom right corner

### 2️⃣ **Index Project** (1-3 minutes)
- Analyzes your code
- Builds code intelligence
- **Status**: Bottom status bar

### 3️⃣ **Download SDK Components** (5-10 minutes, first time only)
- Android SDK Platform 34
- Build Tools
- Android NDK 25.0+
- **Status**: SDK Manager will prompt you

### ⚠️ **IMPORTANT**: 
- **Don't click anything** during this process!
- Let it complete fully
- You'll see "Ready" in bottom status bar when done
- Total time: ~10-20 minutes for first project

---

## 🔧 **Step 3: Install Required Components**

If prompted, install these (click "Install" when asked):

### Required:
- ✅ **Android SDK Platform 34** (API 34)
- ✅ **Android SDK Build-Tools 34.0.0**
- ✅ **Android SDK Platform-Tools**
- ✅ **Android NDK 25.0.8775105** (or newer)
- ✅ **CMake 3.22.1**

### How to Install SDK Components:
1. **Tools → SDK Manager** (or Ctrl+Alt+S)
2. **SDK Platforms** tab:
   - ✅ Check "Android 14.0 (API 34)"
   - ✅ Check "Android 10.0 (API 29)"
3. **SDK Tools** tab:
   - ✅ Check "Android SDK Build-Tools"
   - ✅ Check "NDK (Side by side)"
   - ✅ Check "CMake"
4. Click **"Apply"** → **"OK"**
5. Wait for download

---

## 🏗️ **Step 4: Build Your APK**

### Method 1: Using Menu (Easiest)
1. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Wait for build (3-10 minutes first time)
3. Click **"locate"** when notification appears
4. Your APK is ready! 🎉

### Method 2: Using Build Variants
1. **View → Tool Windows → Build Variants**
2. Select **"debug"** (default)
3. **Build → Build APK**

### Method 3: Using Gradle Panel
1. **View → Tool Windows → Gradle**
2. **AI_ANC_Phone → app → Tasks → build → assembleDebug**
3. Double-click to run

---

## 📱 **Step 5: Find Your APK**

After build completes:

### Location:
```
/home/anubhavanand/AI_ANC_Phone/app/build/outputs/apk/debug/app-debug.apk
```

### File Details:
- **Name**: `app-debug.apk`
- **Size**: ~10-15 MB
- **Type**: Android Package (APK)
- **Ready to install**: YES ✅

### Quick Access:
- Click "locate" in build notification, OR
- Navigate in Project view: `app/build/outputs/apk/debug/`

---

## 🔌 **Step 6: Install on Your Phone**

### Option A: Direct Install (If Phone Connected)
1. Connect Android phone via USB
2. Enable **Developer Options** on phone:
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back → Developer Options
   - Enable **USB Debugging**
3. In Android Studio:
   - **Run → Run 'app'** (Shift+F10)
   - Select your device
   - App installs and launches! 🚀

### Option B: Manual Install
1. Copy `app-debug.apk` to your phone
2. Open file on phone
3. Allow "Install from Unknown Sources"
4. Tap "Install"

---

## 🎯 **What to Expect During Build**

### First Build (~5-15 minutes):
```
[1/6] Gradle sync                    ⏱️ 2-5 min
[2/6] Download dependencies          ⏱️ 3-5 min
[3/6] Compile Kotlin code            ⏱️ 1-2 min
[4/6] Build native C++ (RNNoise)     ⏱️ 2-5 min
[5/6] Package APK                    ⏱️ 1-2 min
[6/6] Sign APK (debug keystore)      ⏱️ <1 min
```

### Subsequent Builds (~1-3 minutes):
- Much faster!
- Only rebuilds changed files
- Gradle cache speeds up process

---

## 🐛 **Troubleshooting Common Issues**

### ❌ "SDK not found"
**Fix**: 
- Tools → SDK Manager
- Install Android SDK Platform 34
- Install Android SDK Build-Tools

### ❌ "NDK not found"
**Fix**:
- Tools → SDK Manager → SDK Tools
- Check "NDK (Side by side)"
- Click Apply

### ❌ "CMake not found"
**Fix**:
- Tools → SDK Manager → SDK Tools
- Check "CMake"
- Click Apply

### ❌ "Gradle sync failed"
**Fix**:
- File → Invalidate Caches / Restart
- Or: Delete `.gradle` folder and resync

### ❌ "Build failed" (general)
**Fix**:
1. View → Tool Windows → Build
2. Read error message
3. Common fixes:
   - Clean: Build → Clean Project
   - Rebuild: Build → Rebuild Project
   - Sync: File → Sync Project with Gradle Files

### ❌ Slow build
**Fix**:
- File → Settings → Build → Compiler
- Check "Compile independent modules in parallel"
- Increase "Heap size" to 2048 MB

---

## 💡 **Useful Android Studio Shortcuts**

| Action | Shortcut |
|--------|----------|
| Build APK | Build → Build APK |
| Run on device | Shift + F10 |
| Open SDK Manager | Ctrl + Alt + S |
| Sync Gradle | File → Sync |
| Clean Project | Build → Clean |
| Project Structure | Ctrl + Alt + Shift + S |
| Search Everywhere | Double Shift |
| Terminal | Alt + F12 |

---

## 📊 **Project Structure Overview**

When you open the project, you'll see:

```
AI_ANC_Phone/
├── 📱 app/
│   ├── src/main/
│   │   ├── java/com/example/aianc/    ← Your Kotlin code
│   │   ├── cpp/                        ← Native C++ code
│   │   ├── res/                        ← UI layouts, strings
│   │   └── AndroidManifest.xml         ← App config
│   └── build.gradle                    ← App dependencies
├── 📄 Documentation files (.md)
├── build.gradle                        ← Project config
└── settings.gradle                     ← Project settings
```

---

## ✅ **Checklist for First Build**

Before clicking "Build APK", ensure:

- [x] ✅ Android Studio fully synced (no errors in sync)
- [x] ✅ Android SDK Platform 34 installed
- [x] ✅ NDK installed (check SDK Manager)
- [x] ✅ CMake installed (check SDK Manager)
- [x] ✅ No red underlines in code
- [x] ✅ Gradle sync successful
- [x] ✅ Internet connection active (for dependencies)

---

## 🎓 **After First Successful Build**

You can:

1. **Run on Device**: Shift+F10
2. **Debug**: View logs in Logcat
3. **Edit Code**: Make changes and rebuild
4. **Build Variants**: Switch between debug/release
5. **Generate Signed APK**: For Google Play (later)

---

## 🚀 **Quick Build Command**

Once setup is complete, future builds are just:

1. Open Android Studio
2. Build → Build APK
3. Wait ~1-3 minutes
4. Click "locate"
5. Done! 🎉

---

## 📞 **Need Help?**

If you encounter issues:

1. **Build Output**: View → Tool Windows → Build
2. **Gradle Console**: View → Tool Windows → Build (Console tab)
3. **Event Log**: View → Tool Windows → Event Log
4. **Logcat**: View → Tool Windows → Logcat (when device connected)

---

## 🎯 **Expected Result**

After successful build:

```
✅ BUILD SUCCESSFUL in 2m 15s
✅ 45 actionable tasks: 45 executed
✅ APK generated at: app/build/outputs/apk/debug/app-debug.apk
```

**Your app is ready to install and test!** 🎊

---

**Good luck with your build!** 🌟

*Estimated total time for first build: 15-25 minutes (including downloads)*
