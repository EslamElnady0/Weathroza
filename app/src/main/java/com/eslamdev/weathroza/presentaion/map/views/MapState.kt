package com.eslamdev.weathroza.presentaion.map.views

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
    val selectedLatLng: LatLng? = null,
    val showBottomSheet: Boolean = false,
    val searchQuery: String = ""
)