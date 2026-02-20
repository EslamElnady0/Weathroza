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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MapWeatherCard(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.cardBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = weather.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppColors.value)
                Text(text = weather.country, fontSize = 13.sp, color = AppColors.label, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(6.dp))
                DegreeText(
                    degree = weather.temp,
                    fontSize = 38.sp,
                    color = AppColors.value,
                    settings = settings,
                    fontWeight = FontWeight.Light
                )
                MapTempMinMax(weather = weather, settings = settings)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                GlideImage(
                    model = weather.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = weather.weatherDescription,
                    fontSize = 12.sp,
                    color = AppColors.label,
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColors.divider))

        val feelsLikeLabel = "${
            weather.feelsLike.convertTemp(settings.temperatureUnit).formatLocalized(locale, "%d")
        }°${settings.temperatureUnit.label()}"

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = stringResource(R.string.feels_like_minimal), fontSize = 13.sp, color = AppColors.label)
            Text(text = feelsLikeLabel, fontSize = 13.sp, color = AppColors.value, fontWeight = FontWeight.Medium)
        }

        MapWeatherStats(weather = weather, settings = settings)
    }
}