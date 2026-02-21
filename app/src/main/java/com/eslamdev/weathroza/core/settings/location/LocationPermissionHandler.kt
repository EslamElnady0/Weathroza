package com.eslamdev.weathroza.core.settings.location


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun RequestLocationPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit = {}
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) onGranted() else onDenied()
    }

    LaunchedEffect(Unit) {
        launcher.launch(LocationPermissionHelper.permissions)
    }
}