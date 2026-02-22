package com.eslamdev.weathroza.core.settings.location

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.eslamdev.weathroza.core.settings.location.LocationPermissionHelper.findActivity

@Composable
fun RequestLocationPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit = {},
    onPermanentlyDenied: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.any { it }
        if (isGranted) {
            onGranted()
        } else {
            val showRationale = activity?.let { act ->
                LocationPermissionHelper.permissions.any {
                    ActivityCompat.shouldShowRequestPermissionRationale(act, it)
                }
            } ?: false

            if (!showRationale) {
                onPermanentlyDenied()
            } else {
                onDenied()
            }
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(LocationPermissionHelper.permissions)
    }
}