package com.eslamdev.weathroza.presentaion.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.map.views.components.MapContent
import com.eslamdev.weathroza.presentaion.map.views.components.MapLocationBottomSheet
import com.eslamdev.weathroza.presentaion.map.views.components.MapTopBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

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

    val weatherState by viewModel.weatherState.collectAsState()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {

        // 1. The map
        MapContent(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            selectedLatLng = selectedLatLng,
            onMapClick = { latLng ->
                selectedLatLng = latLng
                showBottomSheet = true
                viewModel.loadWeather(latLng)
            }
        )

        // 2. The header (back button + search bar overlay)
        MapTopBar(
            onBackClick = {
                // navController.popBackStack()
            }
        )

        // 3. The bottom sheet
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
                    // TODO: pass latLng back to settings / home
                }
            )
        }
    }
}