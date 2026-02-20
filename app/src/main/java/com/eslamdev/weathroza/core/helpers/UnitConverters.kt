package com.eslamdev.weathroza.core.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.settings.TemperatureUnit
import com.eslamdev.weathroza.core.settings.WindSpeedUnit

@Composable
fun TemperatureUnit.label(): String = when (this) {
    TemperatureUnit.CELSIUS    -> stringResource(R.string.celsius_symbol)
    TemperatureUnit.FAHRENHEIT -> stringResource(R.string.fahrenheit_symbol)
    TemperatureUnit.KELVIN     -> stringResource(R.string.kelvin_symbol)
}

@Composable
fun WindSpeedUnit.label(): String = when (this) {
    WindSpeedUnit.MS  -> stringResource(R.string.unit_ms)
    WindSpeedUnit.MPH -> stringResource(R.string.unit_mph)
}