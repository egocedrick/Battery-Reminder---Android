# Battery Reminder - Android

## Overview
A Kotlin-based Android system application that provides persistent battery alerts at critical levels. Designed to remind users to charge their device immediately, the app leverages overlay dialogs and device admin permissions to ensure visibility and reliability.

## Core Workflow
1. **20% Battery Level**
   - A dialog box appears with a warning sign.
   - Includes an **OK button** to dismiss the dialog.
   - Purpose: gentle reminder to charge the device.

2. **5% Battery Level**
   - A dialog box appears with a warning sign.
   - **No OK button** is provided — dialog persists until the device is plugged in.
   - Purpose: enforce immediate charging action.

3. **Persistence**
   - Dialogs appear regardless of which app or background state the device is in.
   - Logic remains intact even after device restart.

## Tech Stack
- **Language**: Kotlin
- **Platform**: Android SDK
- **System Features**:
  - Overlay permissions
  - Device admin integration
  - Battery state monitoring

## Project Structure
- `/ui` – Dialog layouts and activities
- `/service` – Background services monitoring battery state
- `/security` – Device admin receiver and permission handling

## Setup Instructions
1. Install the application on the device.
2. On first launch, enable:
   - **Overlay Permission**
   - **Device Admin Access**
3. The app will automatically start monitoring battery levels.

## Notes
- This project demonstrates **system-level alerting** and **persistent logic** for mobile security and utility applications.
- Built with defensive coding principles to ensure reliability across reboots and background states.
