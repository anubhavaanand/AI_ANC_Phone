# Contributing to AI-Powered Real-Time Noise Suppression

Thank you for your interest in contributing to this project! 🎉

## How to Contribute

### Reporting Bugs

If you find a bug, please create an issue with:
- A clear, descriptive title
- Steps to reproduce the issue
- Expected vs actual behavior
- Your device model and Android version
- Logs if available (use `adb logcat`)

### Suggesting Enhancements

We welcome feature requests! Please include:
- Clear description of the feature
- Use cases and benefits
- Any implementation ideas you have

### Pull Requests

1. **Fork** the repository
2. **Create a branch** for your feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

#### PR Guidelines

- Keep changes focused and atomic
- Follow existing code style (Kotlin conventions)
- Test on real Android devices if possible
- Update documentation as needed
- Add comments for complex logic

## Development Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android NDK 25.0+
- Git

### Building the Project

```bash
# Clone your fork
git clone https://github.com/YOUR-USERNAME/AI_ANC_Phone.git
cd AI_ANC_Phone

# Ensure RNNoise is present
cd app/src/main/cpp
git clone https://github.com/xiph/rnnoise.git
cd ../../../

# Open in Android Studio
# Build → Make Project
```

### Testing

- Test on Android 10+ devices
- Test with different Bluetooth earbuds
- Verify foreground service behavior
- Check battery consumption
- Test auto-start on boot

## Code Style

- Use **Kotlin** for Android code
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use **4 spaces** for indentation
- Keep line length under 120 characters
- Write descriptive variable names

### Native Code (C++)

- Follow [Google C++ Style Guide](https://google.github.io/styleguide/cppguide.html)
- Keep JNI functions minimal and focused
- Free resources properly

## Areas for Contribution

### High Priority

- [ ] Battery optimization improvements
- [ ] Additional AI model support (DeepFilterNet, NSNet2)
- [ ] Adaptive noise suppression modes
- [ ] UI/UX enhancements
- [ ] Unit and integration tests

### Medium Priority

- [ ] Statistics dashboard
- [ ] Environment detection
- [ ] Custom noise profiles
- [ ] Performance benchmarking tools
- [ ] Documentation improvements

### Nice to Have

- [ ] Material Design 3 migration
- [ ] Dark mode support
- [ ] Accessibility improvements
- [ ] Multi-language support
- [ ] In-app tutorials

## Questions?

Feel free to:
- Open an issue for discussion
- Join community discussions
- Reach out via email

## Code of Conduct

Be respectful, inclusive, and professional. We're all here to learn and build something great together!

---

**Happy Coding!** 🎧✨
