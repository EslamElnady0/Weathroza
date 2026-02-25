package com.eslamdev.weathroza.presentaion.alerts.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.components.AddFab
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertsViewModel
import com.eslamdev.weathroza.presentaion.alerts.views.components.AlertsHeader
import com.eslamdev.weathroza.presentaion.alerts.views.components.CreateAlertBottomSheet
import com.eslamdev.weathroza.presentaion.alerts.views.components.ScheduledTabBody

@Composable
fun AlertsView(
    bottomController: NavController,
    viewModel: AlertsViewModel,
    modifier: Modifier = Modifier,
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var showCreateSheet by remember { mutableStateOf(false) }
    var scheduledAlerts by remember { mutableStateOf(viewModel.scheduledAlerts) }

    if (showCreateSheet) {
        CreateAlertBottomSheet(
            settings = settings,
            onDismiss = { showCreateSheet = false },
            onCreateAlert = { name, param, threshold, isAbove, frequency, start, end ->
                viewModel.createAlert(name, param, threshold, isAbove, frequency, start, end)
            },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            AddFab(onClick = { showCreateSheet = true })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeightSpacer(16.0)
            AlertsHeader()
            HeightSpacer(24.0)
            ScheduledTabBody(
                alerts = scheduledAlerts,
                onToggle = { id, enabled ->
                    scheduledAlerts = scheduledAlerts.map {
                        if (it.id == id) it.copy(isEnabled = enabled) else it
                    }
                    viewModel.toggleAlert(id, enabled)
                },
            )
        }

    }
}