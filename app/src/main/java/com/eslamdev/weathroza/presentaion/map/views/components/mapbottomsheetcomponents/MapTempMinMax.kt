package com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@Composable
fun MapTempMinMax(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()

    val minLabel = "${weather.tempMin.convertTemp(settings.temperatureUnit).formatLocalized(locale, "%d")}°${settings.temperatureUnit.label()}"
    val maxLabel = "${weather.tempMax.convertTemp(settings.temperatureUnit).formatLocalized(locale, "%d")}°${settings.temperatureUnit.label()}"

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "↑ $maxLabel", fontSize = 13.sp, color = AppColors.tempMax, fontWeight = FontWeight.Medium)
        Text(text = "·", fontSize = 13.sp, color = AppColors.label)
        Text(text = "↓ $minLabel", fontSize = 13.sp, color = AppColors.tempMin, fontWeight = FontWeight.Medium)
    }
}