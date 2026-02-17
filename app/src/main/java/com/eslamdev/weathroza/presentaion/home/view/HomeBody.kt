package com.eslamdev.weathroza.presentaion.home.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.components.WidthSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.presentaion.home.view.components.DailyForecast
import com.eslamdev.weathroza.presentaion.home.view.components.HourlyForecast
import com.eslamdev.weathroza.presentaion.home.view.components.HourlyForecastList
import com.eslamdev.weathroza.presentaion.home.view.components.SevenDayForecastList
import com.eslamdev.weathroza.presentaion.home.view.components.StatsRow
import com.eslamdev.weathroza.presentaion.home.view.components.StatusItem

@Composable
fun HomeBody(bottomController: NavController, modifier: Modifier = Modifier) {
   HomeBodyImpl()
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeBodyPreview(modifier: Modifier = Modifier) {

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold { innerPadding ->
            HomeBodyImpl(modifier.padding(innerPadding))
        }
    }

}

@Composable
fun HomeBodyImpl(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HeightSpacer(30.0)
            Text(
                stringResource(R.string.miami),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            HeightSpacer(2.0)
            Text(
                stringResource(R.string.date_placeholder) + " • " + stringResource(R.string.time_placeholder),
                color = AppColors.lightGray
            )
            Image(
                painter = painterResource(id = R.drawable.dummy_sun_image),
                contentDescription = stringResource(R.string.weather_icon_desc),
                modifier = Modifier.height(130.dp)
            )
            Text(
                stringResource(R.string.clear_sky),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.gray
            )
            DegreeText(
                stringResource(R.string.temp_placeholder), fontSize = 96.sp,
                color = AppColors.primary,
                fontWeight = FontWeight.ExtraBold
            )
            HeightSpacer(16.0)
            Row {
                StatusItem(
                    icon = R.drawable.humidity_ic,
                    label = stringResource(R.string.humidity),
                    value = stringResource(R.string.humidity_value_placeholder),
                    contentDesc = stringResource(R.string.humidity_icon_desc),
                    modifier = Modifier.weight(1f)
                )
                WidthSpacer(8.0)
                StatusItem(
                    icon = R.drawable.wind_ic,
                    label = stringResource(R.string.wind),
                    value = stringResource(R.string.wind_value_placeholder),
                    contentDesc = stringResource(R.string.wind_icon_desc),
                    modifier = Modifier.weight(1f)
                )
            }
            HeightSpacer(8.0)
            Row {
                StatusItem(
                    icon = R.drawable.pressure,
                    label = stringResource(R.string.pressure),
                    value = stringResource(R.string.pressure_value_placeholder),
                    contentDesc = stringResource(R.string.pressure_icon_desc),
                    modifier = Modifier.weight(1f)
                )
                WidthSpacer(8.0)
                StatusItem(
                    icon = R.drawable.cloud_ic,
                    label = stringResource(R.string.clouds),
                    value = stringResource(R.string.clouds_value_placeholder),
                    contentDesc = stringResource(R.string.clouds_icon_desc),
                    modifier = Modifier.weight(1f)
                )
            }
            HeightSpacer(12.0)
            CardWithBoarder {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    StatsRow(
                        icon = R.drawable.sunrise_ic,
                        label = stringResource(R.string.sunrise),
                        value = stringResource(R.string.sunrise_time_placeholder),
                        contentDesc = stringResource(R.string.sunrise_icon_desc),
                    )
                    WidthSpacer(8.0)
                    VerticalDivider(
                        Modifier.height(40.dp),
                        DividerDefaults.Thickness,
                        AppColors.primary.copy(alpha = 0.2f)
                    )
                    WidthSpacer(8.0)
                    StatsRow(
                        icon = R.drawable.sunset,
                        label = stringResource(R.string.sunset),
                        value = stringResource(R.string.sunset_time_placeholder),
                        contentDesc = stringResource(R.string.sunset_icon_desc),
                    )
                }
            }
            HeightSpacer(20.0)

            val sampleHourlyForecasts = listOf(
                HourlyForecast("10 AM", R.drawable.dummy_sun_image, "62", "Sunny"),
                HourlyForecast("11 AM", R.drawable.dummy_sun_image, "60", "Sunny"),
                HourlyForecast("12 PM", R.drawable.dummy_sun_image, "59", "Sunny"),
                HourlyForecast("01 PM", R.drawable.dummy_sun_image, "61", "Sunny"),
                HourlyForecast("02 PM", R.drawable.dummy_sun_image, "63", "Sunny")
            )
            HourlyForecastList(forecasts = sampleHourlyForecasts)

            HeightSpacer(24.0)
        }

        val sampleDailyForecasts = listOf(
            DailyForecast(
                "Tuesday",
                "Oct 24",
                R.drawable.dummy_sun_image,
                "Sunny",
                "19.56",
                "17.83"
            ),
            DailyForecast(
                "Wednesday",
                "Oct 25",
                R.drawable.dummy_sun_image,
                "Rainy",
                "16.20",
                "14.56"
            ),
            DailyForecast(
                "Thursday",
                "Oct 26",
                R.drawable.dummy_sun_image,
                "Stormy",
                "15.45",
                "13.02"
            ),
            DailyForecast(
                "Friday",
                "Oct 27",
                R.drawable.dummy_sun_image,
                "Cloudy",
                "18.12",
                "16.85"
            ),
            DailyForecast(
                "Saturday",
                "Oct 28",
                R.drawable.dummy_sun_image,
                "Sunny",
                "21.80",
                "20.50"
            ),
            DailyForecast(
                "Sunday",
                "Oct 29",
                R.drawable.dummy_sun_image,
                "Sunny",
                "22.30",
                "21.10"
            ),
            DailyForecast(
                "Monday",
                "Oct 30",
                R.drawable.dummy_sun_image,
                "Cloudy",
                "18.90",
                "17.20"
            )
        )
        item {
            SevenDayForecastList(forecasts = sampleDailyForecasts)
            HeightSpacer(24.0)
        }
    }
}

