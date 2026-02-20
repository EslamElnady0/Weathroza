package com.eslamdev.weathroza.presentaion.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.views.components.MapContent
import com.eslamdev.weathroza.presentaion.map.views.components.MapLocationBottomSheet
import com.eslamdev.weathroza.presentaion.map.views.components.MapTopBar
import com.eslamdev.weathroza.presentaion.map.views.components.maptoolbarcomponents.MapCitySearchResults
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.*
import com.eslamdev.weathroza.core.helpers.rememberDebounced
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    navController: NavController,
    viewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0444, 31.2357), 10f)
    }
    val coroutineScope = rememberCoroutineScope()

    fun animateCameraTo(latLng: LatLng) {
        coroutineScope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(latLng, 10f),
                durationMs = 600
            )
        }
    }

    val weatherState by viewModel.weatherState.collectAsState()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val citiesState by viewModel.citiesState.collectAsStateWithLifecycle()

    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val debouncedQuery by rememberDebounced(value = searchQuery, delayMillis = 400L)

    LaunchedEffect(debouncedQuery) {
        if (debouncedQuery.length >= 2) {
            viewModel.getPossibleCities(debouncedQuery)
        } else {
            viewModel.resetCitiesState()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        MapContent(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            selectedLatLng = selectedLatLng,
            onMapClick = { latLng ->
                selectedLatLng = latLng
                showBottomSheet = true
                searchQuery = ""
                animateCameraTo(latLng)
                viewModel.loadWeather(latLng)
            }
        )

        MapTopBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onBackClick = { navController.popBackStack() }
        )

        MapCitySearchResults(
            citiesState = citiesState,
            language = settings.language,
            onCityClick = { latLng ->
                selectedLatLng = latLng
                searchQuery = ""
                viewModel.resetCitiesState()
                animateCameraTo(latLng)
                viewModel.loadWeather(latLng)
                showBottomSheet = true
            },
            modifier = Modifier
                .padding(top = 80.dp)
                .statusBarsPadding()
        )

        if (showBottomSheet) {
            MapLocationBottomSheet(
                weatherState = weatherState,
                settings = settings,
                sheetState = sheetState,
                selectedLatLng = selectedLatLng,
                onDismiss = {
                    showBottomSheet = false
                    viewModel.resetWeather()
                },
                onConfirm = { latLng ->
                    // TODO: pass latLng to settings / home
                }
            )
        }
    }
}