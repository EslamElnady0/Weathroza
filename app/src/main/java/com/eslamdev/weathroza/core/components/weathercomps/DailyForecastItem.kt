package com.eslamdev.weathroza.core.components.weathercomps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(
    forecast: DailyForecastEntity,
    settings: UserSettings,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isToday = remember(forecast.dt) { DateTimeHelper.isToday(forecast.dt) }

    CardWithBoarder(
        modifier = modifier.fillMaxWidth(),
        containerColor = if (isToday) AppColors.primary.copy(alpha = 0.15f) else AppColors.cardBg,
        borderColor = if (isToday) AppColors.primary else AppColors.primary.copy(alpha = 0.2f)
    ) {
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
                    degree = forecast.tempDay,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    settings = settings,
                    color = AppColors.gray
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DegreeText(
                        degree = forecast.tempMax,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        settings = settings,
                        color = AppColors.tempMax
                    )
                    DegreeText(
                        degree = forecast.tempMin,
                        fontSize = 11.sp,
                        settings = settings,
                        color = AppColors.tempMin
                    )
                }
            }
        }
    }
}
