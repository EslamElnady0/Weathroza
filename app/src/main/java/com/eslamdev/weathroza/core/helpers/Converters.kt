package com.eslamdev.weathroza.core.helpers
import com.eslamdev.weathroza.core.langmanager.LanguageManager
import com.eslamdev.weathroza.core.settings.TemperatureUnit
import com.eslamdev.weathroza.core.settings.WindSpeedUnit
import kotlin.math.roundToInt


fun Double.toFahrenheit(): Double = this * 9.0 / 5.0 + 32
fun Double.toKelvin(): Double = this + 273.15
fun Double.toMph(): Double = this * 2.23694

fun Double.convertTemp(unit: TemperatureUnit): Double = when (unit) {
    TemperatureUnit.CELSIUS    -> this
    TemperatureUnit.FAHRENHEIT -> toFahrenheit()
    TemperatureUnit.KELVIN     -> toKelvin()
}

fun Double.convertWind(unit: WindSpeedUnit): Double = when (unit) {
    WindSpeedUnit.MS  -> this
    WindSpeedUnit.MPH -> toMph()
}

fun Double.toTwoDigitString(): String =
    String.format(LanguageManager.currentLocale, "%.2f", this)