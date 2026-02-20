package com.eslamdev.weathroza.presentaion.map.views

import com.eslamdev.weathroza.core.helpers.animateTo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MapStateHolder(
    val cameraPositionState: CameraPositionState,
    val sheetState: SheetState,
    private val coroutineScope: CoroutineScope
) {
    var uiState by mutableStateOf(MapUiState())
        private set

    fun onLocationSelected(latLng: LatLng) {
        uiState = uiState.copy(
            selectedLatLng = latLng,
            showBottomSheet = true,
            searchQuery = ""
        )
        animateCameraTo(latLng)
    }

    fun onSearchQueryChange(query: String) {
        uiState = uiState.copy(searchQuery = query)
    }

    fun onBottomSheetDismiss() {
        uiState = uiState.copy(showBottomSheet = false)
    }

    private fun animateCameraTo(latLng: LatLng) {
        coroutineScope.launch {
            cameraPositionState.animateTo(latLng)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberMapStateHolder(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): MapStateHolder {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0444, 31.2357), 10f)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    return remember {
        MapStateHolder(
            cameraPositionState = cameraPositionState,
            sheetState = sheetState,
            coroutineScope = coroutineScope
        )
    }
}