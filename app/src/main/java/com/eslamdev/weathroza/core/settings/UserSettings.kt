package com.eslamdev.weathroza.core.settings

import androidx.annotation.StringRes
import com.eslamdev.weathroza.R
import com.google.android.gms.maps.model.LatLng
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
    val language: AppLanguage = AppLanguage.SYSTEM,
    val userLat: Double? = null,
    val userLng: Double? = null,
    val locationType: LocationType = LocationType.NONE
) {
    val userLatLng: LatLng?
        get() = if (userLat != null && userLng != null) LatLng(userLat, userLng) else null

    val hasLocation: Boolean
        get() = locationType != LocationType.NONE && userLat != null && userLng != null
}

enum class TemperatureUnit { CELSIUS, FAHRENHEIT, KELVIN }
enum class WindSpeedUnit { MS, MPH }

enum class LocationType {
    NONE,
    GPS,
    MANUAL
}