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

data class HourlyForecast(
    val time: String,
    val icon: Int,
    val temperature: String,
    val description: String
)

@Composable
fun HourlyForecastList(
    forecasts: List<HourlyForecast>,
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
                HourlyForecastItem(forecast = forecast)
            }
        }
    }
}

@Composable
fun HourlyForecastItem(
    forecast: HourlyForecast,
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
                text = forecast.time,
                fontSize = 12.sp,
                color = AppColors.lightGray,
                fontWeight = FontWeight.Medium
            )
            Image(
                painter = painterResource(id = forecast.icon),
                contentDescription = forecast.description,
                modifier = Modifier.size(40.dp)
            )
            DegreeText(
                forecast.temperature,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.gray
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HourlyForecastListPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        val sampleForecasts = listOf(
            HourlyForecast("10 AM", R.drawable.dummy_sun_image, "62", "Sunny"),
            HourlyForecast("11 AM", R.drawable.dummy_sun_image, "60", "Sunny"),
            HourlyForecast("12 PM", R.drawable.dummy_sun_image, "59", "Sunny"),
            HourlyForecast("01 PM", R.drawable.dummy_sun_image, "61", "Sunny"),
            HourlyForecast("02 PM", R.drawable.dummy_sun_image, "63", "Sunny")
        )
        
        HourlyForecastList(
            forecasts = sampleForecasts,
            modifier = Modifier.padding(16.dp)
        )
    }
}
