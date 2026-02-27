package com.eslamdev.weathroza.core.helpers

import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import java.util.Locale
import kotlin.math.roundToInt


fun Double.toFahrenheit(): Double = this * 9.0 / 5.0 + 32
fun Double.toKelvin(): Double = this + 273.15
fun Double.toMph(): Double = this * 2.23694

fun Float.convertTemp(unit: TemperatureUnit): Int =
    this.toDouble().convertTemp(unit)

fun Float.convertWind(unit: WindSpeedUnit): Double =
    this.toDouble().convertWind(unit)

fun Double.convertTemp(unit: TemperatureUnit): Int = when (unit) {
    TemperatureUnit.CELSIUS -> this.roundToInt()
    TemperatureUnit.FAHRENHEIT -> toFahrenheit().roundToInt()
    TemperatureUnit.KELVIN -> toKelvin().roundToInt()
}

fun Double.convertWind(unit: WindSpeedUnit): Double = when (unit) {
    WindSpeedUnit.MS -> this
    WindSpeedUnit.MPH -> toMph()
}

fun Double.toTwoDigitString(locale: Locale): String =
    String.format(locale, "%.2f", this)

fun Int.toLocalizedPercentage(locale: Locale): String =
    String.format(locale, "%d%%", this)

fun Number.formatLocalized(locale: Locale, pattern: String): String =
    String.format(locale, pattern, this)


fun AlertEntity.resolveDisplayThreshold(currentSettings: UserSettings): Int {
    return when (parameter) {
        WeatherParameter.TEMP -> {
            val storedUnit = thresholdTempUnit ?: TemperatureUnit.CELSIUS
            val currentUnit = currentSettings.temperatureUnit
            if (storedUnit == currentUnit) {
                threshold.toInt()
            } else {
                val asCelsius = threshold.toDouble().toCelsius(storedUnit)
                asCelsius.convertTemp(currentUnit)
            }
        }

        WeatherParameter.WIND -> {
            val storedUnit = thresholdWindUnit ?: WindSpeedUnit.MS
            val currentUnit = currentSettings.windSpeedUnit
            if (storedUnit == currentUnit) {
                threshold.toInt()
            } else {
                val asMs = threshold.toDouble().toMs(storedUnit)
                asMs.convertWind(currentUnit).roundToInt()
            }
        }

        else -> threshold.toInt()
    }
}

fun Double.toCelsius(from: TemperatureUnit): Double = when (from) {
    TemperatureUnit.CELSIUS -> this
    TemperatureUnit.FAHRENHEIT -> (this - 32) * 5.0 / 9.0
    TemperatureUnit.KELVIN -> this - 273.15
}

fun Double.toMs(from: WindSpeedUnit): Double = when (from) {
    WindSpeedUnit.MS -> this
    WindSpeedUnit.MPH -> this / 2.23694
}
