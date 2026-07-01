# MovieJetpack 🎬

A cinematic movie ticket booking application built with modern Android development practices using **Jetpack Compose**. This project focuses on high-quality UI/UX, seamless transitions, and immersive media integration.

## 📱 Demo
https://github.com/user-attachments/assets/a1ca0279-cb80-4133-9417-abd908281810

## 🚀 Features

### 1. Home Screen (Now Showing)
*   **Dynamic Poster UI**: A large movie poster with a smooth vertical gradient overlay.
*   **Shared Element Transitions**: The movie poster seamlessly animates and scales when transitioning between the Home and Booking screens.
*   **Immersive Mode**: Fullscreen experience with hidden system bars for a cinematic feel.
*   **Action Buttons**: Interactive "Buy Tickets" and "Play" buttons.

### 2. Booking Screen
*   **Date & Time Selection**: A stylized row of dates and time slots for movie scheduling.
*   **Interactive Ticket Counter**: Select up to 3 tickets with playful **Lottie Animations** that pop in and out based on the selection count.
*   **Multi-step Flow**: Smooth horizontal transitions between scheduling and ticket count selection.

### 3. Seat Selection Screen
*   **Virtual Theater View**: A custom-shaped curved screen playing a live video trailer.
*   **Realistic Reflection**: Real-time video floor reflection with a fading gradient effect.
*   **Smart Grid Logic**: 
    *   Interactive seat grid using custom graphics.
    *   **Auto-selection**: Clicking a seat automatically selects the required number of consecutive available seats.
    *   **Greedy Matching**: If consecutive seats aren't available, it selects the maximum possible and notifies the user via Toast.

## 🛠 Technology Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
*   **Architecture**: Single Activity with State-driven navigation.
*   **Transitions**: [Shared Element Transitions](https://developer.android.com/develop/ui/compose/animation/shared-elements) (Experimental API).
*   **Animations**: Lottie Compose for character animations.
*   **Media**: [Media3 ExoPlayer](https://developer.android.com/guide/topics/media/exoplayer) for high-performance video playback.
*   **Graphics**: Custom `GenericShape` and `graphicsLayer` for advanced UI effects.

## 📸 Screenshots

| Home Screen | Booking Screen | Seat Selection |
| :---: | :---: | :---: |
| Immserive Movie UI | Lottie Animations | Video Theater View |

## ⚙️ Setup

1.  Clone the repository.
2.  Open in **Android Studio Ladybug** or newer.
3.  Ensure you have an active internet connection (required for video trailer playback).
4.  Build and Run on an emulator or physical device.

---
*Created with ❤️ using Jetpack Compose.*
