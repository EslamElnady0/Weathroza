package com.eslamdev.weathroza.presentaion.settings.view.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.settings.location.LocationPermissionHelper
import com.eslamdev.weathroza.core.settings.location.RequestLocationPermission

@Composable
fun LocationSelector(
    selectedOptionIndex: Int,
    onGpsSelected: () -> Unit,
    onMapSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var showMapDialog    by remember { mutableStateOf(false) }
    var showGpsDialog    by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf(false) }
    var showLocationDisabledDialog by remember { mutableStateOf(false) }

    // Map dialog
    if (showMapDialog) {
        PickFromMapDialog(
            onDismiss = { showMapDialog = false },
            onProceed = {
                showMapDialog = false
                onMapSelected()
            }
        )
    }

    // GPS dialog
    if (showGpsDialog) {
        PickFromGpsDialog(
            onDismiss = { showGpsDialog = false },
            onProceed = {
                showGpsDialog = false
                if (LocationPermissionHelper.hasPermission(context)) {
                    if (LocationPermissionHelper.isLocationEnabled(context)) {
                        onGpsSelected()
                    } else {
                        showLocationDisabledDialog = true
                    }
                } else {
                    requestPermission = true
                }
            }
        )
    }

    if (requestPermission) {
        RequestLocationPermission(
            onGranted = {
                requestPermission = false
                if (LocationPermissionHelper.isLocationEnabled(context)) {
                    onGpsSelected()
                } else {
                    showLocationDisabledDialog = true
                }
            },
            onDenied = {
                requestPermission = false
            }
        )
    }

    if (showLocationDisabledDialog) {
        LocationDisabledDialog(
            onDismiss = { showLocationDisabledDialog = false },
            onOpenSettings = {
                showLocationDisabledDialog = false
                LocationPermissionHelper.openLocationSettings(context)
            }
        )
    }

    SettingsSelector(horizontalPadding = 8.dp) {
        SettingSelectorItem(
            title = stringResource(R.string.gps_automatic),
            icon = Icons.Default.LocationOn,
            isSelected = selectedOptionIndex == 0,
            onClick = { showGpsDialog = true }
        )
        SettingSelectorItem(
            title = stringResource(R.string.map_manual),
            icon = Icons.Default.Map,
            isSelected = selectedOptionIndex == 1,
            onClick = { showMapDialog = true }
        )
    }
}

