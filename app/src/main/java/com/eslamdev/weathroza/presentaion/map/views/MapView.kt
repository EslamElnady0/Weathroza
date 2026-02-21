package com.eslamdev.weathroza.presentaion.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.views.components.MapContent
import com.eslamdev.weathroza.presentaion.map.views.components.MapLocationBottomSheet
import com.eslamdev.weathroza.presentaion.map.views.components.MapTopBar
import com.eslamdev.weathroza.presentaion.map.views.components.maptoolbarcomponents.MapCitySearchResults



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    navController: NavController,
    viewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val mapState = rememberMapStateHolder()

    val weatherState by viewModel.weatherState.collectAsState()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val citiesState by viewModel.citiesState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        MapContent(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = mapState.cameraPositionState,
            selectedLatLng = mapState.uiState.selectedLatLng,
            onMapClick = { latLng ->
                mapState.onLocationSelected(latLng)
                viewModel.onSearchQueryChange("")
                viewModel.loadWeather(latLng)
            }
        )

        MapTopBar(
            query = mapState.uiState.searchQuery,
            onQueryChange = {
                mapState.onSearchQueryChange(it)
                viewModel.onSearchQueryChange(it)
            },
            onBackClick = { navController.popBackStack() }
        )

        MapCitySearchResults(
            citiesState = citiesState,
            language = settings.language,
            onCityClick = { latLng ->
                mapState.onLocationSelected(latLng)
                viewModel.onSearchQueryChange("")
                viewModel.loadWeather(latLng)
            },
            onRetry = { viewModel.getPossibleCities(mapState.uiState.searchQuery) },
            modifier = Modifier
                .padding(top = 80.dp)
                .statusBarsPadding()
        )

        if (mapState.uiState.showBottomSheet) {
            MapLocationBottomSheet(
                weatherState = weatherState,
                settings = settings,
                sheetState = mapState.sheetState,
                selectedLatLng = mapState.uiState.selectedLatLng,
                onDismiss = {
                    mapState.onBottomSheetDismiss()
                    viewModel.resetWeather()
                },
                onConfirm = { latLng,cityId ->
                    viewModel.confirmLocation(latLng, cityId)
                    navController.popBackStack() },
                onRetry = {
                    mapState.uiState.selectedLatLng?.let { viewModel.loadWeather(it) }
                }
            )
        }
    }
}