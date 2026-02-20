package com.eslamdev.weathroza.core.settings

import androidx.annotation.StringRes
import com.eslamdev.weathroza.R
import java.util.Locale

enum class AppLanguage(val code: String, @StringRes val titleResId: Int) {
    SYSTEM("system", R.string.system_default),
    ENGLISH("en", R.string.english),
    ARABIC("ar", R.string.arabic)
}

fun AppLanguage.toLocale(): Locale = when (this) {
    AppLanguage.SYSTEM -> Locale.getDefault()
    else -> Locale(code)
}

data class UserSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windSpeedUnit: WindSpeedUnit = WindSpeedUnit.MS,
    val language: AppLanguage = AppLanguage.SYSTEM
)

enum class TemperatureUnit { CELSIUS, FAHRENHEIT, KELVIN }
enum class WindSpeedUnit { MS, MPH }