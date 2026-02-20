package com.eslamdev.weathroza.presentaion.map.views.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    selectedLatLng: LatLng?,
    onMapClick: (LatLng) -> Unit
) {
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick
    ) {
        selectedLatLng?.let { latLng ->
            Marker(
                state = MarkerState(position = latLng)
            )
        }
    }
}
