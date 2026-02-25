package com.eslamdev.weathroza.core.helpers

import android.content.Context
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit

fun TemperatureUnit.label(context: Context): String = when (this) {
    TemperatureUnit.CELSIUS -> context.getString(R.string.celsius_symbol)
    TemperatureUnit.FAHRENHEIT -> context.getString(R.string.fahrenheit_symbol)
    TemperatureUnit.KELVIN -> context.getString(R.string.kelvin_symbol)
}

fun WindSpeedUnit.label(context: Context): String = when (this) {
    WindSpeedUnit.MS -> context.getString(R.string.unit_ms)
    WindSpeedUnit.MPH -> context.getString(R.string.unit_mph)
}