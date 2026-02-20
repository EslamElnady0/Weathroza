package com.eslamdev.weathroza.core.helpers

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

suspend fun CameraPositionState.animateTo(latLng: LatLng, zoom: Float = 12f) {
    animate(
        update = CameraUpdateFactory.newLatLngZoom(latLng, zoom),
        durationMs = 600
    )
}