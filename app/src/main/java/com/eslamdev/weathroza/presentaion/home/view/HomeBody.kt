package com.eslamdev.weathroza.presentaion.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.components.WidthSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.core.helpers.convertWind
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.helpers.toLocalizedPercentage
import com.eslamdev.weathroza.core.settings.LocationType
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.location.LocationPermissionHelper
import com.eslamdev.weathroza.core.settings.location.RequestLocationPermission
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData
import com.eslamdev.weathroza.presentaion.home.view.components.DailyForecastItem
import com.eslamdev.weathroza.presentaion.home.view.components.HourlyForecastList
import com.eslamdev.weathroza.presentaion.home.view.components.RefreshBanner
import com.eslamdev.weathroza.presentaion.home.view.components.StatsRow
import com.eslamdev.weathroza.presentaion.home.view.components.StatusItem
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel

@Composable
fun HomeBody(
    bottomController: NavController,
    viewModel: HomeViewModel
) {

    val state by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val shouldRequestGps = settings.locationType == LocationType.GPS
            || settings.locationType == LocationType.NONE

    if (shouldRequestGps) {
        if (LocationPermissionHelper.hasPermission(context)) {
            LaunchedEffect(Unit) {
                viewModel.fetchAndSaveGpsLocation()
            }
        } else {
            RequestLocationPermission(
                onGranted = { viewModel.fetchAndSaveGpsLocation() },
                onDenied  = { /* optionally show rationale */ }
            )
        }
    }
    when (state) {

        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = AppColors.primary
                )
            }
        }

        is UiState.Error -> {
            Text(
                text = (state as UiState.Error).message
            )
        }

        is UiState.Success -> {
            val data =
                (state as UiState.Success<HomeViewData>).data

            HomeBodyImpl(
                weather = data.weather,
                hourly = data.hourlyForecast,
                daily = data.dailyForecast,
                settings = settings,
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refresh
            )
        }

        UiState.Idle -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBodyImpl(
    weather: WeatherEntity,
    hourly: List<HourlyForecastEntity>,
    daily: List<DailyForecastEntity>,
    settings: UserSettings,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    ) {
    val pullToRefreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize(),
        indicator = {
            val pullProgress = pullToRefreshState.distanceFraction
            RefreshBanner(
                isRefreshing = isRefreshing,
                pullProgress = pullProgress,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = (pullProgress * 80.dp.toPx()) - size.height
                    }
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                HeightSpacer(30.0)

                HeaderSection(weather, settings)

                HeightSpacer(16.0)

                WeatherDetailsSection(weather, settings)

                HeightSpacer(12.0)

                SunCycleSection(weather, settings)

                HeightSpacer(20.0)

                HourlyForecastList(hourly, settings)

                HeightSpacer(24.0)

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.seven_day_forecast),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.gray,
                    textAlign = TextAlign.Start
                )

                HeightSpacer(12.0)
            }

            items(daily) { forecast ->
                DailyForecastItem(forecast, settings)
                HeightSpacer(12.0)
            }

            item {
                HeightSpacer(24.0)
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun HeaderSection(weather: WeatherEntity,settings: UserSettings) {
    Text(
        "${weather.name}, ${weather.country}",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )

    HeightSpacer(2.0)

    Text(
        DateTimeHelper.formatFullDateTime(weather.dt, settings.language.toLocale()),
        color = AppColors.lightGray
    )

    GlideImage(
        model = weather.iconUrl,
        contentDescription = stringResource(R.string.weather_icon_desc),
        modifier = Modifier
            .height(130.dp)
            .fillMaxSize()
    )

    Text(
        weather.weatherDescription,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = AppColors.gray
    )

    DegreeText(weather.temp,
        fontSize = 65.sp,
        color = AppColors.primary,
        settings = settings,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
private fun WeatherDetailsSection(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()
    Row {
        StatusItem(
            icon = R.drawable.humidity_ic,
            label = stringResource(R.string.humidity),
            value = weather.humidity.toLocalizedPercentage(locale),
            contentDesc = stringResource(R.string.humidity_icon_desc),
            modifier = Modifier.weight(1f)
        )
        WidthSpacer(8.0)
        StatusItem(
            icon = R.drawable.wind_ic,
            label = stringResource(R.string.wind),
            value = "${
                weather.windSpeed.convertWind(settings.windSpeedUnit).formatLocalized(locale, "%.2f")
            } ${settings.windSpeedUnit.label()}",
            contentDesc = stringResource(R.string.wind_icon_desc),
            modifier = Modifier.weight(1f)
        )
    }

    HeightSpacer(8.0)

    Row {
        StatusItem(
            icon = R.drawable.pressure,
            label = stringResource(R.string.pressure),
            value = "${
                weather.pressure.formatLocalized(locale, "%d")
            } ${stringResource(R.string.pressure_unit)}",
            contentDesc = stringResource(R.string.pressure_icon_desc),
            modifier = Modifier.weight(1f)
        )
        WidthSpacer(8.0)
        StatusItem(
            icon = R.drawable.cloud_ic,
            label = stringResource(R.string.clouds),
            value = weather.cloudsAll.toLocalizedPercentage(locale),
            contentDesc = stringResource(R.string.clouds_icon_desc),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SunCycleSection(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()
    CardWithBoarder {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            StatsRow(
                icon = R.drawable.sunrise_ic,
                label = stringResource(R.string.sunrise),
                value = DateTimeHelper.formatTime(weather.sunrise, locale),
                contentDesc = stringResource(R.string.sunrise_icon_desc)
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
                value = DateTimeHelper.formatTime(weather.sunset, locale),
                contentDesc = stringResource(R.string.sunset_icon_desc)
            )
        }
    }
}



