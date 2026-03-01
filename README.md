# Weathroza 🌦️

## Project Brief Description 
**Weathroza** is an advanced Android Mobile Application that cleanly displays the weather status and temperature for your current or any chosen location. You can seamlessly pick a specific location on the map, search using auto-complete text, and manage a list of favorite locations. In addition, Weathroza keeps you safe and informed by allowing you to set personalized weather alerts (for rain, wind, extreme low/high temperatures, fog, snow, etc.).

This project also includes specialized **extra functionalities**, such as a **Home Screen Widget**, an **Automated Tests Workflow**, and a carefully crafted **Weather-Specific Custom Alarm**.

## Features ✨

### 🏠 Home Screen
Displays a beautiful dashboard containing:
- Current temperature, current date, and current time.
- Detailed metrics including Humidity, Wind speed, Pressure, and Cloud coverage.
- City name, suitable weather status icon, and weather description (e.g., clear sky, light rain).
- Hourly forecast for the current date.
- 5-day daily forecast details.

### ⚙️ Settings Screen
Allows the user to fully personalize their weather experience:
- **Location Strategy:** Choose between real-time GPS tracking or selecting a fixed location from the map.
- **Unit Preferences:** 
  - **Temperature:** Kelvin, Celsius, or Fahrenheit.
  - **Wind Speed:** meter/sec or miles/hour.
- **Localization:** Multi-language support (Arabic & English).

### 🔔 Weather Alerts Screen
Set up custom notifications to warn you of severe weather conditions:
- **Extra Work Implementation:** A robust custom weather-specific alarm system that goes beyond standard alarms.
- Set alarms with the following configurations:
  - **Duration:** The time frame through which the alarm is actively checking for conditions.
  - **Type of Alert:** Choose between a simple notification or a full-screen custom default alarm sound.
  - **Lifecycle Control:** Option to smoothly stop the notification or turn off the active alarm.

### 🗺️ Favorites Screen
Keep track of places you care about:
- List of your beautifully displayed favorite locations. Pressing an item opens a dedicated forecast screen with full weather details for that place.
- Floating Action Button (FAB) to add a new favorite place.
  - Clicking the FAB launches a unified screen featuring a **Map** and **Auto-complete Edit Text**.
  - Drop a pin/marker on the map directly or type the name of a city to save it to your favorites.
- Easily remove any saved place from the list.

### 🌟 Extra Work Highlights
- **Home Screen App Widget:** Quickly peek at the latest weather data right from your Android launcher without opening the app.
- **Automated Tests Workflow:** Integrated CI/CD workflows encompassing reliable automated unit and UI testing.
- **Custom Weather-Specific Alarm:** Specialized alarm execution allowing you to wake up or be alerted exclusively when certain weather phenomena occur.

## Technologies Used 💻
- **UI & Architecture:** MVVM Architecture, Jetpack Compose, Material Design 3, Navigation Compose
- **Asynchronous & Reactive:** Kotlin Coroutines & Kotlin Flows
- **Network & API:** Retrofit (with Gson), OpenWeatherMap API
- **Local Storage:** Room Database, Jetpack DataStore (Preferences)
- **Dependency Injection:** Koin (integrated with Compose and WorkManager)
- **Background Tasks:** AndroidX WorkManager
- **App Widgets:** Jetpack Glance
- **Maps & Location:** Google Maps Compose, FusedLocation Provider
- **Testing:** JUnit, MockK, Robolectric, Espresso, Coroutines Test

## API Integration 🌐
This project integrates with the OpenWeatherMap API:
[`https://api.openweathermap.org/data/2.5/`](https://api.openweathermap.org/data/2.5/forecast)
Data parsing rules are applied depending upon diverse user scenarios to yield perfectly accurate and optimal data fetching.

## Demo & References 🎥
- **Video Demo:** [Watch Weathroza in Action](https://drive.google.com/file/d/1f5zE1gK9OxHXLqsQIvQqPFL7wBxj5gC6/view?usp=sharing)
- **Similar Inspiration App:** [Weather Alarms on Google Play](https://play.google.com/store/apps/details?id=info.vazquezsoftware.weatheralarms&hl=en_US&gl=US)

---
*Developed with ❤️ as a comprehensive Android Weather Application.*
