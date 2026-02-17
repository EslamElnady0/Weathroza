package com.eslamdev.weathroza.presentaion.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors

data class DailyForecast(
    val dayName: String,
    val date: String,
    val icon: Int,
    val weatherDescription: String,
    val temperature: String,
    val feelsLike: String
)

@Composable
fun SevenDayForecastList(
    forecasts: List<DailyForecast>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.seven_day_forecast),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.gray
        )
        
        HeightSpacer(12.0)
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            forecasts.forEach { forecast ->
                DailyForecastItem(forecast = forecast)
            }
        }
    }
}

@Composable
fun DailyForecastItem(
    forecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = forecast.dayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.gray
                )
                Text(
                    text = forecast.date,
                    fontSize = 12.sp,
                    color = AppColors.lightGray
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = forecast.icon),
                    contentDescription = forecast.weatherDescription,
                    modifier = Modifier.size(32.dp)
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
                    forecast.temperature,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.gray
                )
                Text(
                    text = stringResource(R.string.feels_like, forecast.feelsLike),
                    fontSize = 11.sp,
                    color = AppColors.lightGray
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SevenDayForecastListPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        val sampleForecasts = listOf(
            DailyForecast("Tuesday", "Oct 24", R.drawable.dummy_sun_image, "Sunny", "19.56", "17.83"),
            DailyForecast("Wednesday", "Oct 25", R.drawable.dummy_sun_image, "Rainy", "16.20", "14.56"),
            DailyForecast("Thursday", "Oct 26", R.drawable.dummy_sun_image, "Stormy", "15.45", "13.02"),
            DailyForecast("Friday", "Oct 27", R.drawable.dummy_sun_image, "Cloudy", "18.12", "16.85"),
            DailyForecast("Saturday", "Oct 28", R.drawable.dummy_sun_image, "Sunny", "21.80", "20.50"),
            DailyForecast("Sunday", "Oct 29", R.drawable.dummy_sun_image, "Sunny", "22.30", "21.10"),
            DailyForecast("Monday", "Oct 30", R.drawable.dummy_sun_image, "Cloudy", "18.90", "17.20")
        )
        
        SevenDayForecastList(
            forecasts = sampleForecasts,
            modifier = Modifier.padding(16.dp)
        )
    }
}
