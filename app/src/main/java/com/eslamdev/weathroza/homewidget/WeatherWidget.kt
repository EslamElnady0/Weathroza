package com.eslamdev.weathroza.homewidget

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.eslamdev.weathroza.MainActivity
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import java.util.Locale

class WeatherWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = WeatherWidgetStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = currentState<WeatherWidgetState>()
            GlanceTheme {
                WidgetContent(state)
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun WidgetContent(state: WeatherWidgetState) {
    val primary = ColorProvider(Color(0xFF6CB4E4))
    val white = ColorProvider(Color.White)
    val lightGray = ColorProvider(Color(0xFFB0BEC5))
    val bgColor = Color(0xFF0F1923)
    val cardColor = Color(0xFF1A2433)
    val dividerColor = Color(0xFF6CB4E4).copy(alpha = 0.15f)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(R.drawable.widget_bg))
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>()),
    ) {
        // ── Top row ──────────────────────────────────────────────
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: city + description + temp
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = "${state.cityName}, ${state.country}",
                    style = TextStyle(
                        color = lightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    maxLines = 1,
                )
                Spacer(GlanceModifier.height(2.dp))
                Text(
                    text = formatTemp(state.temp, state.temperatureUnit),
                    style = TextStyle(
                        color = primary,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(GlanceModifier.height(1.dp))
                Text(
                    text = state.weatherDescription.replaceFirstChar { it.uppercase() },
                    style = TextStyle(color = lightGray, fontSize = 10.sp),
                    maxLines = 1,
                )
            }

            // Right: humidity + wind + toggle
            Column(
                horizontalAlignment = Alignment.End,
                modifier = GlanceModifier.padding(start = 8.dp),
            ) {
                // Toggle pill
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = GlanceModifier
                        .background(ImageProvider(R.drawable.widget_toggle_bg))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable(actionRunCallback<ToggleForecastAction>()),
                ) {
                    Text(
                        text = if (state.showHourly) "Hourly ↓" else "Daily ↓",
                        style = TextStyle(
                            color = primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Spacer(GlanceModifier.height(8.dp))

                // Humidity
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "💧 ${state.humidity}%",
                        style = TextStyle(color = lightGray, fontSize = 10.sp),
                    )
                }

                Spacer(GlanceModifier.height(3.dp))

                // Wind
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🌬 ${formatWind(state.windSpeed, state.windSpeedUnit)}",
                        style = TextStyle(color = lightGray, fontSize = 10.sp),
                    )
                }
            }
        }

        Spacer(GlanceModifier.height(10.dp))

        // ── Divider ───────────────────────────────────────────────
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ImageProvider(R.drawable.widget_toggle_bg)),
        ) {}

        Spacer(GlanceModifier.height(10.dp))

        // ── Forecast row ─────────────────────────────────────────
        if (state.showHourly) {
            HourlyRow(
                state.hourlyForecasts,
                state.temperatureUnit,
                primary,
                white,
                lightGray,
                cardColor
            )
        } else {
            DailyRow(
                state.dailyForecasts,
                state.temperatureUnit,
                primary,
                white,
                lightGray,
                cardColor
            )
        }
    }
}

@Composable
private fun HourlyRow(
    items: List<HourlyWidgetItem>,
    tempUnit: String,
    primary: ColorProvider,
    white: ColorProvider,
    lightGray: ColorProvider,
    cardColor: Color,
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        items.take(5).forEachIndexed { index, item ->
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .background(ImageProvider(R.drawable.widget_card_bg))
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            ) {
                Text(
                    text = DateTimeHelper.formatHour(item.dt, Locale.getDefault()),
                    style = TextStyle(color = lightGray, fontSize = 9.sp),
                    maxLines = 1,
                )
                Spacer(GlanceModifier.height(4.dp))
                Text(
                    text = formatTemp(item.temp, tempUnit),
                    style = TextStyle(
                        color = white,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            if (index < 4) Spacer(GlanceModifier.width(4.dp))
        }
    }
}

@Composable
private fun DailyRow(
    items: List<DailyWidgetItem>,
    tempUnit: String,
    primary: ColorProvider,
    white: ColorProvider,
    lightGray: ColorProvider,
    cardColor: Color,
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
    ) {
        items.take(5).forEachIndexed { index, item ->
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .background(ImageProvider(R.drawable.widget_card_bg))
                    .cornerRadius(12.dp)
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            ) {
                Text(
                    text = DateTimeHelper.formatDayName(item.dt, Locale.getDefault()).take(3),
                    style = TextStyle(color = lightGray, fontSize = 9.sp),
                    maxLines = 1,
                )
                Spacer(GlanceModifier.height(4.dp))
                Text(
                    text = formatTemp(item.tempDay, tempUnit),
                    style = TextStyle(
                        color = white,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            if (index < 4) Spacer(GlanceModifier.width(4.dp))
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────

private fun formatTemp(temp: Double, unitName: String): String {
    val converted = when (unitName) {
        TemperatureUnit.FAHRENHEIT.name -> temp * 9.0 / 5.0 + 32.0
        TemperatureUnit.KELVIN.name -> temp + 273.15
        else -> temp
    }
    val symbol = when (unitName) {
        TemperatureUnit.FAHRENHEIT.name -> "°F"
        TemperatureUnit.KELVIN.name -> "K"
        else -> "°C"
    }
    return "${converted.toInt()}$symbol"
}

private fun formatWind(speed: Double, unitName: String): String {
    val converted = if (unitName == WindSpeedUnit.MPH.name) speed * 2.23694 else speed
    val label = if (unitName == WindSpeedUnit.MPH.name) "mph" else "m/s"
    return "${"%.1f".format(converted)} $label"
}
// ── Toggle Action ─────────────────────────────────────────────

class ToggleForecastAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        updateAppWidgetState(
            context, WeatherWidgetStateDefinition, glanceId,
        ) { it.copy(showHourly = !it.showHourly) }
        WeatherWidget().update(context, glanceId)
    }
}
