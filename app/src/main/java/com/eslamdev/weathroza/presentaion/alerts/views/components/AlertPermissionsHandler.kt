package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.eslamdev.weathroza.core.components.ObserveOnResume
import com.eslamdev.weathroza.core.notification.AlarmPermissionHelper
import com.eslamdev.weathroza.core.notification.NotificationPermissionHelper
import com.eslamdev.weathroza.presentaion.settings.view.components.PermanentlyDeniedDialog

@Composable
fun AlertPermissionsHandler(
    onAllGranted: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var waitingForAlarmPermission by remember { mutableStateOf(false) }
    var showPermanentlyDeniedDialog by remember { mutableStateOf(false) }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        when {
            granted && AlarmPermissionHelper.hasPermission(context) -> onAllGranted()
            granted -> {
                waitingForAlarmPermission = true
                AlarmPermissionHelper.openSettings(context)
            }

            else -> showPermanentlyDeniedDialog = true
        }
    }

    LaunchedEffect(Unit) {
        when {
            !NotificationPermissionHelper.hasPermission(context) ->
                notificationLauncher.launch(NotificationPermissionHelper.PERMISSION)

            !AlarmPermissionHelper.hasPermission(context) -> {
                waitingForAlarmPermission = true
                AlarmPermissionHelper.openSettings(context)
            }

            else -> onAllGranted()
        }
    }

    ObserveOnResume(enabled = waitingForAlarmPermission) {
        if (AlarmPermissionHelper.hasPermission(context)) {
            waitingForAlarmPermission = false
            onAllGranted()
        }
    }

    if (showPermanentlyDeniedDialog) {
        PermanentlyDeniedDialog(
            onDismiss = {
                showPermanentlyDeniedDialog = false
                onDismiss()
            },
            onOpenSettings = {
                showPermanentlyDeniedDialog = false
                NotificationPermissionHelper.openAppSettings(context)
            }
        )
    }
}

