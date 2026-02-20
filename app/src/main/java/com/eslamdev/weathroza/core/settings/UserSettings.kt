package com.eslamdev.weathroza.core.settings

data class UserSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.MS,
)

enum class TemperatureUnit { CELSIUS, FAHRENHEIT, KELVIN }
enum class WindSpeedUnit { MS, MPH }