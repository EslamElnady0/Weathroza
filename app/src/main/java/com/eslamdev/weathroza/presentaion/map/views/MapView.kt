package com.eslamdev.weathroza.presentaion.map.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.eslamdev.weathroza.presentaion.map.views.components.MapContent
import com.eslamdev.weathroza.presentaion.map.views.components.MapLocationBottomSheet
import com.eslamdev.weathroza.presentaion.map.views.components.MapTopBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(navController: NavController, modifier: Modifier = Modifier) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0444, 31.2357), 10f)
    }

    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {

        MapContent(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            selectedLatLng = selectedLatLng,
            onMapClick = { latLng ->
                selectedLatLng = latLng
                showBottomSheet = true
            }
        )

        MapTopBar(
            onBackClick = {
               // navController.popBackStack()
            }
        )

        if (showBottomSheet) {
            MapLocationBottomSheet(
                selectedLatLng = selectedLatLng,
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false },
                onConfirm = { latLng ->
                    // TODO: pass latLng to ViewModel
                }
            )
        }
    }
}