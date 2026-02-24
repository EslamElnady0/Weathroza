package com.eslamdev.weathroza.presentaion.favourite.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.weathercomps.HomeErrorState
import com.eslamdev.weathroza.core.components.weathercomps.SharedWeatherBody
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModel
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData

@Composable
fun FavWeatherDisplayView(
    viewModel: FavWeatherDisplayViewModel,
    bottomController: NavController,
    lat: Double,
    lng: Double,
    cityId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(lat, lng, cityId) {
        viewModel.loadData(lat, lng, cityId)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        when (state) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.primary)
                }
            }

            is UiState.Error -> {
                HomeErrorState(
                    message = stringResource(
                        (state as UiState.Error).messageRes ?: R.string.error_unknown
                    ),
                    onNavigateToSettings = onNavigateBack
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
                    onRefresh = { viewModel.refresh(lat, lng, cityId) },
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            UiState.Idle -> {}
        }
    }
}
