# BlueCoLab Water Monitor

![BlueCoLab Banner](Blue_CoLab_circle_on_blue_600_copy.png)

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/Min%20API-26-blue.svg)](https://android-arsenal.com/api?level=26)
[![License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red.svg)](LICENSE)

> Real-time water level monitoring app for environmental awareness and flood safety.

---

## Overview

BlueCoLab Water Monitor is an Android application that provides real-time access to water level data from monitoring sensors. Originally developed to track water quality metrics from the BCL (Blue Collaborative Lab) sensor network, this app delivers instant visibility into environmental water conditions.

**Key Purpose**: Enable communities to monitor local water levels for flood awareness, environmental research, and public safety.

---

## Features

- **Real-Time Data** - Live water level readings from sensor network
- **Multiple Sensors** - Monitor different sensor locations
- **Visual Display** - Clean UI showing current water levels
- **Auto-Refresh** - Continuous data updates
- **Native Android** - Built with Kotlin for optimal performance
- **Material Design** - Modern, intuitive interface

---

## Screenshots

| Main Dashboard | Sensor Details | Data View |
|---------------|---------------|-----------|
| ![Screenshot 1](Screen_Shot_2021-12-10_at_1.41.13_PM.png) | ![Screenshot 2](Screen_Shot_2021-12-10_at_1.41.20_PM.png) | *Coming soon* |

---

## Download

### Option 1: Install APK (Recommended)
1. Download the latest APK from [Releases](https://github.com/salvatore-ardisi/bluecolab-water-monitor/releases)
2. Enable "Install from unknown sources" in Android settings
3. Install the APK
4. Grant necessary permissions when prompted

### Option 2: Build from Source
See [Building from Source](#-building-from-source) section below.

---

## Future Features

The following features are planned for future releases:

### Push Notifications (Coming Soon!)
- **Alert Thresholds** - Get notified when water levels exceed safe thresholds
- **Rapid Change Detection** - Alerts for sudden water level spikes (flash flood warning)
- **Location-Based Alerts** - Notifications for sensors near your location
- **Severity Levels** - Watch, Warning, and Emergency alerts
- **Multi-Channel** - Push notifications, SMS, and email options

### Enhanced Features
- **Interactive Map** - View all sensors on a map
- **Historical Trends** - Charts showing water level changes over time
- **Offline Mode** - Access cached data without internet
- **Data Export** - Download sensor data as CSV
- **Favorites** - Quick access to frequently monitored sensors

### iOS Version
- Native iOS app in development
- React Native cross-platform version being considered

---

## Tech Stack

- **Language**: Kotlin 1.9
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Build System**: Gradle (Kotlin DSL) with Version Catalog
- **Min SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)

**Dependencies**:
- AndroidX Core & Lifecycle
- Jetpack Compose (BOM 2024.02)
- Hilt (Dependency Injection)
- Retrofit + OkHttp (Networking)
- Moshi (JSON parsing)
- Coil (Image loading)
- Coroutines (Async operations)
- Detekt + ktlint (Code quality)

---

## Building from Source

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK** 17 or newer
- **Android SDK** API 34+

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/salvatore-ardisi/bluecolab-water-monitor.git
   cd bluecolab-water-monitor
   ```

2. **Set up local configuration**
   ```bash
   cp local.properties.example local.properties
   # Edit local.properties and set your Android SDK path
   ```

3. **Set up Firebase (optional)**
   ```bash
   cp app/google-services.json.example app/google-services.json
   # Replace placeholder values with your Firebase project credentials
   ```

4. **Open in Android Studio**
   - File → Open → Select the project folder
   - Wait for Gradle sync to complete

5. **Build the APK**
   ```bash
   ./gradlew assembleDebug
   ```
   APK will be in `app/build/outputs/apk/debug/`

6. **Run on device/emulator**
   - Connect Android device via USB (enable USB debugging)
   - OR start an Android emulator
   - Click Run in Android Studio

---

## API

This app connects to water monitoring sensors that provide real-time data. The API is read-only and requires no authentication.

**Data Format**: JSON  
**Update Frequency**: Real-time (varies by sensor)  
**Coverage**: Multiple sensor locations

**Note**: API endpoints are configured for the BCL sensor network. The app can be adapted to work with other water monitoring APIs by modifying the network layer.

---

## Project Structure

```
app/src/main/java/com/bluecolab/watermonitor/
├── BlueColabApp.kt                          # Application class (Hilt entry point)
├── MainActivity.kt                          # Main activity (Compose host)
├── data/
│   ├── remote/
│   │   ├── api/BlueColabApi.kt              # Retrofit API interface
│   │   └── dto/WaterQualityDto.kt           # Data transfer objects
│   └── repository/WaterQualityRepositoryImpl.kt
├── di/
│   ├── NetworkModule.kt                     # Hilt network dependencies
│   └── RepositoryModule.kt                  # Hilt repository bindings
├── domain/
│   ├── model/WaterQualityReading.kt         # Domain models
│   ├── repository/WaterQualityRepository.kt # Repository interface
│   └── usecase/                             # Business logic use cases
├── presentation/
│   ├── component/                           # Reusable Compose components
│   ├── screen/home/                         # Home screen + ViewModel
│   └── theme/                               # Material 3 theme
└── util/
    ├── Constants.kt                         # App-wide constants
    └── Resource.kt                          # Result wrapper

gradle/libs.versions.toml                    # Version catalog
build.gradle.kts                             # Project-level build config (Kotlin DSL)
app/build.gradle.kts                         # App-level build config (Kotlin DSL)
```

---

## Changelog

### v2.0.0 (Current)
- Complete rewrite with Jetpack Compose and Material 3
- MVVM + Clean Architecture
- Hilt dependency injection
- Real-time water quality monitoring
- Multiple sensor support
- Auto-refresh functionality

### Upcoming
- Push notifications for threshold alerts
- Interactive sensor map
- Historical data charts
- Offline mode

---

## Known Issues

- None reported yet! Please [open an issue](https://github.com/salvatore-ardisi/bluecolab-water-monitor/issues) if you find any bugs.

---

### Copyright

Copyright (c) 2024 Blue CoLab - Pace University

All rights reserved. This project is provided for educational and reference purposes only.

For permissions or inquiries, please contact Blue CoLab at Pace University.

---

## Team

**Blue CoLab - Pace University**
- GitHub: [@salvatore-ardisi](https://github.com/salvatore-ardisi)
- Project: Water Level Monitoring Research

---

## Acknowledgments

- Blue Collaborative Lab at Pace University
- Pace University Computer Science Department
- Material Design team for design guidelines
- Android developer community

---

## Support

If you have questions or need help:
- [Open an issue](https://github.com/salvatore-ardisi/bluecolab-water-monitor/issues)
- See [SECURITY_REVIEW.md](SECURITY_REVIEW.md) for security documentation

---

**Built with ☕ and 💧 for environmental awareness**

---

## Disclaimer

This application is for informational purposes only. Water level data should not be the sole source for flood safety decisions. Always follow official weather alerts and emergency management guidance for flood warnings and evacuations.
