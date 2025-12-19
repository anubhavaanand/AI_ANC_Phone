# Changelog

All notable changes to AI-Powered Real-Time Noise Suppression will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-12-19

### Added - MVP Release ✅

#### Core Features
- Real-time AI noise suppression using RNNoise model
- Foreground service with `FOREGROUND_SERVICE_MICROPHONE` type
- Low-latency audio pipeline (<50ms end-to-end)
- 24/7 operation support with screen-off handling
- Universal Bluetooth earbud compatibility

#### User Interface
- Main activity with service toggle button
- Real-time service status indicator
- Auto-start on boot preference toggle
- Modern card-based UI design
- Feature highlights display

#### Permissions & Compatibility
- Runtime permission handling for Android 10+
- Android 14+ foreground service compliance
- Android 13+ notification permissions
- Android 12+ Bluetooth connect permissions

#### Auto-Start & Persistence
- Boot receiver for automatic service restart
- User-configurable auto-start preference
- `START_STICKY` service flag for memory resilience
- SharedPreferences for settings persistence

#### Native Integration
- JNI wrapper for RNNoise library
- CMake build configuration
- Android NDK integration
- Efficient float array passing between Kotlin and C++

#### Documentation
- Comprehensive README.md
- Architecture documentation (ARCHITECTURE.md)
- Contributing guidelines (CONTRIBUTING.md)
- MIT License

### Technical Specifications

- **Language**: Kotlin + C/C++
- **Min SDK**: API 29 (Android 10)
- **Target SDK**: API 34 (Android 14)
- **AI Model**: RNNoise (38KB)
- **Sample Rate**: 48kHz
- **Frame Size**: 480 samples (10ms)
- **Latency**: 35-50ms
- **CPU Usage**: 6-10% (mid-range devices)

### Dependencies

- androidx.core:core-ktx:1.12.0
- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.11.0
- androidx.constraintlayout:constraintlayout:2.1.4
- androidx.cardview:cardview:1.0.0

## [Unreleased]

### Planned Features

#### High Priority
- [ ] Battery optimization improvements
- [ ] Statistics dashboard (CPU, latency, noise metrics)
- [ ] Environment-based adaptive suppression
- [ ] Multiple AI model support (DeepFilterNet, NSNet2)

#### Medium Priority
- [ ] Mode presets (Call/Study/Travel)
- [ ] Dark mode support
- [ ] Material Design 3 migration
- [ ] Internal audio routing

#### Low Priority
- [ ] Multi-language support
- [ ] In-app tutorials
- [ ] Custom noise profiles
- [ ] Accessibility improvements

### Known Issues

- Audio pipeline may have slight delay on older devices (<Android 12)
- Battery drain varies based on device manufacturer optimizations
- Some devices may kill foreground service aggressively (Xiaomi, Oppo)

### Compatibility Notes

- Tested on Android 10, 12, 13, 14
- Works with AirPods, Sony WH-1000XM series, Samsung Galaxy Buds
- Requires Bluetooth connection for optimal experience

---

## Version History

### Version Format
- **Major.Minor.Patch** (Semantic Versioning)
- Major: Breaking changes or major feature additions
- Minor: New features, backward compatible
- Patch: Bug fixes, minor improvements

### Release Tags
- `v1.0.0`: MVP Release (Current)
- Future releases will follow semantic versioning

---

**Project Status**: ✅ MVP Complete  
**Next Milestone**: Google Play Store Alpha Release  
**Last Updated**: December 19, 2025
