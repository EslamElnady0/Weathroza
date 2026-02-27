package com.eslamdev.weathroza.data.models.alert

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit

enum class WeatherParameter(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val hasThreshold: Boolean,
) {
    TEMP(R.string.temp, Icons.Default.Thermostat, hasThreshold = true),
    WIND(R.string.wind, Icons.Default.Air, hasThreshold = true),
    HUMIDITY(R.string.humidity, Icons.Default.WaterDrop, hasThreshold = true),
    RAIN(R.string.rain, Icons.Default.Umbrella, hasThreshold = false),
}

data class ParameterConfig(
    val unit: String,
    val minValue: Float,
    val maxValue: Float,
    val expectedMin: Float,
    val expectedMax: Float,
)

fun WeatherParameter.resolveConfig(settings: UserSettings, context: Context): ParameterConfig =
    when (this) {
        WeatherParameter.TEMP -> when (settings.temperatureUnit) {
            TemperatureUnit.CELSIUS -> ParameterConfig(
                settings.temperatureUnit.label(context),
                -20f,
                50f,
                20f,
                35f
            )

            TemperatureUnit.FAHRENHEIT -> ParameterConfig(
                settings.temperatureUnit.label(context),
                0f,
                120f,
                68f,
                95f
            )

            TemperatureUnit.KELVIN -> ParameterConfig(
                settings.temperatureUnit.label(context),
                253f,
                323f,
                293f,
                308f
            )
        }

        WeatherParameter.WIND -> when (settings.windSpeedUnit) {
            WindSpeedUnit.MS -> ParameterConfig(
                settings.windSpeedUnit.label(context),
                0f,
                40f,
                2f,
                10f
            )

            WindSpeedUnit.MPH -> ParameterConfig(
                settings.windSpeedUnit.label(context),
                0f,
                90f,
                4f,
                22f
            )
        }

        WeatherParameter.RAIN -> ParameterConfig(
            context.getString(R.string.unit_mm),
            0f,
            50f,
            0f,
            10f
        )

        WeatherParameter.HUMIDITY -> ParameterConfig(
            context.getString(R.string.unit_percent),
            0f,
            100f,
            40f,
            70f
        )
    }