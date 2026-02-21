package com.eslamdev.weathroza.core.helpers
import com.eslamdev.weathroza.core.settings.TemperatureUnit
import com.eslamdev.weathroza.core.settings.WindSpeedUnit
import java.util.Locale
import kotlin.math.roundToInt


fun Double.toFahrenheit(): Double = this * 9.0 / 5.0 + 32
fun Double.toKelvin(): Double = this + 273.15
fun Double.toMph(): Double = this * 2.23694

fun Double.convertTemp(unit: TemperatureUnit): Int = when (unit) {
    TemperatureUnit.CELSIUS    -> this.roundToInt()
    TemperatureUnit.FAHRENHEIT -> toFahrenheit().roundToInt()
    TemperatureUnit.KELVIN     -> toKelvin().roundToInt()
}

fun Double.convertWind(unit: WindSpeedUnit): Double = when (unit) {
    WindSpeedUnit.MS  -> this
    WindSpeedUnit.MPH -> toMph()
}
fun Double.toTwoDigitString(locale: Locale): String =
    String.format(locale, "%.2f", this)

fun Int.toLocalizedPercentage(locale: Locale): String =
    String.format(locale, "%d%%", this)

fun Number.formatLocalized(locale: Locale, pattern: String): String =
    String.format(locale, pattern, this)