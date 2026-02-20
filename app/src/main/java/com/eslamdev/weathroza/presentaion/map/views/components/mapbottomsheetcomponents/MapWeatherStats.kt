package com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.convertWind
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.helpers.toLocalizedPercentage
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@Composable
fun MapWeatherStats(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.statBg)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MapWeatherStatItem(
            label = stringResource(R.string.humidity),
            value = weather.humidity.toLocalizedPercentage(locale)
        )
        MapVerticalDivider()
        MapWeatherStatItem(
            label = stringResource(R.string.wind),
            value = "${weather.windSpeed.convertWind(settings.windSpeedUnit).formatLocalized(locale, "%.1f")} ${settings.windSpeedUnit.label()}"
        )
        MapVerticalDivider()
        MapWeatherStatItem(
            label = stringResource(R.string.pressure),
            value = "${weather.pressure.formatLocalized(locale, "%d")} ${stringResource(R.string.pressure_unit)}"
        )
    }
}

@Composable
private fun MapWeatherStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = label.uppercase(), fontSize = 10.sp, color = AppColors.label, letterSpacing = 0.8.sp, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.value)
    }
}

@Composable
private fun MapVerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(AppColors.divider)
    )
}
