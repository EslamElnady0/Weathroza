package com.eslamdev.weathroza.presentaion.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.core.settings.UserSettings

@Composable
fun HourlyForecastList(
    forecasts: List<HourlyForecastEntity>,
    settings: UserSettings,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hourly_forecast),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.gray
            )
            Text(
                text = stringResource(R.string.next_24h),
                fontSize = 12.sp,
                color = AppColors.lightGray
            )
        }
        
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(forecasts) { forecast ->
                HourlyForecastItem(forecast, settings = settings)
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(
    forecast: HourlyForecastEntity,
    settings: UserSettings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = AppColors.primary.copy(alpha = 0.2f),
            shape = RoundedCornerShape(16.dp)
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = DateTimeHelper.formatHour(forecast.dt),
                fontSize = 12.sp,
                color = AppColors.lightGray,
                fontWeight = FontWeight.Medium
            )
            GlideImage(
                model = forecast.iconUrl,
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            DegreeText(
                forecast.temp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                settings = settings,
                color = AppColors.gray
            )
        }
    }
}
