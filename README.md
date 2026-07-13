# Battery Reminder - Android

## Overview
A Kotlin-based Android utility app that monitors device battery level 
in real time and alerts users before unexpected shutdowns occur. 
Features a color-coded UI and persistent background monitoring.

## Core Workflow

1. **21% and above**
   - UI shows green background.
   - Status: Battery Normal — Monitoring in background.

2. **20% Battery Level**
   - UI shifts to amber background.
   - Notification fires: "Battery Warning — Please charge soon."
   - Purpose: early reminder to charge the device.

3. **5% Battery Level**
   - UI shifts to dark red background.
   - Notification fires: "⚠️ DANGER ZONE — Charge immediately."
   - Purpose: critical alert to prevent unexpected shutdown.

4. **Persistence**
   - Monitoring continues after device reboot via BootReceiver.
   - Monitoring resumes after app update via InstallReceiver.

## Tech Stack
- **Language**: Kotlin
- **Platform**: Android SDK (minSdk 30)
- **System Features**:
  - Foreground service for persistent background monitoring
  - Two notification channels (LOW for service, HIGH for alerts)
  - Boot and install receivers for persistence
  - Battery optimization exemption

## Impact
- Alerts users at 20% and 5% battery thresholds.
- Prevents unexpected device shutdown during work.
- Color-coded UI provides instant visual feedback on battery status.
- No ads. No data collected. Lightweight.

## Project Structure
- `BatteryService.kt` — background battery monitoring service
- `BootReceiver.kt` — restarts service after device reboot
- `InstallReceiver.kt` — restarts service after app update
- `MainActivity.kt` — real-time battery UI display

## Setup Instructions
1. Install the application on the device.
2. On first launch:
   - Allow **Notification Permission**
   - Allow **Battery Optimization Exemption**
3. The app will automatically start monitoring battery levels.

## Privacy Policy
https://egocedrick.github.io/Battery-Reminder---Android/privacy-policy
## Developer
John Cedrick Asad (toyomansi)  
github.com/egocedrick
