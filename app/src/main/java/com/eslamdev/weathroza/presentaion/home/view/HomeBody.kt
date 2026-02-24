package com.eslamdev.weathroza.presentaion.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.weathercomps.HomeErrorState
import com.eslamdev.weathroza.core.components.weathercomps.SharedWeatherBody
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.location.LocationPermissionHelper
import com.eslamdev.weathroza.core.settings.location.RequestLocationPermission
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel

@Composable
fun HomeBody(
    bottomController: NavController,
    viewModel: HomeViewModel,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
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
                onDenied = {},
                onPermanentlyDenied = {}
            )
        }
    }

    when (state) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.primary)
            }
        }

        is UiState.Error -> {
            HomeErrorState(
                messageRes =
                    (state as UiState.Error).messageRes ?: R.string.error_unknown,
                onNavigateToSettings = onNavigateToSettings
            )
        }

        is UiState.Success -> {
            val data = (state as UiState.Success<HomeViewData>).data
            SharedWeatherBody(
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
