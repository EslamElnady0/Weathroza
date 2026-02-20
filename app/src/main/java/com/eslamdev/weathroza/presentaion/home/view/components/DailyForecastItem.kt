package com.eslamdev.weathroza.presentaion.home.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.helpers.toTwoDigitString
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(
    forecast: DailyForecastEntity,
    settings: UserSettings,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val locale = settings.language.toLocale()
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = DateTimeHelper.formatDayName(forecast.dt, locale),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.gray
                )
                Text(
                    text = DateTimeHelper.formatShortDate(forecast.dt, locale),
                    fontSize = 12.sp,
                    color = AppColors.lightGray
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = forecast.iconUrl,
                    contentDescription = forecast.weatherDescription,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = forecast.weatherDescription,
                    fontSize = 13.sp,
                    color = AppColors.gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                DegreeText(
                    forecast.tempDay,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    settings = settings,
                    color = AppColors.gray
                )
                Text(
                    text = stringResource(R.string.feels_like,
                        forecast
                        .feelsLikeDay
                        .convertTemp(settings.temperatureUnit)) + settings.temperatureUnit.label(),
                    fontSize = 10.sp,
                    color = AppColors.lightGray
                )
            }
        }
    }
}
