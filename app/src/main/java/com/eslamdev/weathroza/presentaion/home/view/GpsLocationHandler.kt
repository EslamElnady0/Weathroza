package com.eslamdev.weathroza.presentaion.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.eslamdev.weathroza.core.components.ObserveOnResume
import com.eslamdev.weathroza.core.settings.location.LocationPermissionHelper
import com.eslamdev.weathroza.core.settings.location.RequestLocationPermission
import com.eslamdev.weathroza.presentaion.settings.view.components.LocationDisabledDialog

@Composable
fun GpsLocationHandler(
    onFetchLocation: () -> Unit
) {
    val context = LocalContext.current

    var showLocationDisabledDialog by remember { mutableStateOf(false) }
    var waitingForLocationSettings by remember { mutableStateOf(false) }

    ObserveOnResume(
        enabled = waitingForLocationSettings,
        onResume = {
            if (LocationPermissionHelper.isLocationEnabled(context)) {
                waitingForLocationSettings = false
                onFetchLocation()
            }
        }
    )

    val hasPermission = LocationPermissionHelper.hasPermission(context)

    if (hasPermission) {
        if (LocationPermissionHelper.isLocationEnabled(context)) {
            LaunchedEffect(Unit) { onFetchLocation() }
        } else {
            LaunchedEffect(Unit) { showLocationDisabledDialog = true }
        }
    } else {
        RequestLocationPermission(
            onGranted = {
                if (LocationPermissionHelper.isLocationEnabled(context)) {
                    onFetchLocation()
                } else {
                    showLocationDisabledDialog = true
                }
            },
            onDenied = {},
            onPermanentlyDenied = {}
        )
    }

    if (showLocationDisabledDialog) {
        LocationDisabledDialog(
            onDismiss = { showLocationDisabledDialog = false },
            onOpenSettings = {
                showLocationDisabledDialog = false
                waitingForLocationSettings = true
                LocationPermissionHelper.openLocationSettings(context)
            }
        )
    }
}