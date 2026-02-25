package com.eslamdev.weathroza.presentaion.alerts.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.components.SettingsSelector
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertsViewModel
import com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_components.AlertsHeader
import com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_selector_items.AlertTab
import com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_selector_items.ScheduledSelectorItem
import com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_selector_items.WeatherSelectorItem
import com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_tab_bodies.AlertsBody
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_bottom_sheet.CreateAlertBottomSheet

@Composable
fun AlertsView(
    bottomController: NavController,
    viewModel: AlertsViewModel,
    modifier: Modifier = Modifier,
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val uiState by viewModel.alerts.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(AlertTab.SCHEDULED) }
    var showCreateSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = AppColors.primary,
                contentColor = Color.Black,
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AlertsHeader()

            SettingsSelector(modifier = Modifier.padding(horizontal = 16.dp)) {
                ScheduledSelectorItem(
                    isSelected = selectedTab == AlertTab.SCHEDULED,
                    onClick = { selectedTab = AlertTab.SCHEDULED }
                )
                WeatherSelectorItem(
                    isSelected = selectedTab == AlertTab.WEATHER,
                    onClick = { selectedTab = AlertTab.WEATHER }
                )
            }

            HeightSpacer(16.0)

            val filteredState = remember(uiState, selectedTab) {
                if (uiState is UiState.Success) {
                    val filtered = (uiState as UiState.Success).data.filter {
                        if (selectedTab == AlertTab.SCHEDULED) {
                            it.frequency == AlertFrequency.ONE_TIME
                        } else {
                            it.frequency == AlertFrequency.PERIODIC
                        }
                    }
                    UiState.Success(filtered)
                } else {
                    uiState
                }
            }

            AlertsBody(
                uiState = filteredState,
                onToggle = viewModel::toggleAlert,
                onDelete = viewModel::deleteAlert,
                settings = settings,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showCreateSheet) {
        CreateAlertBottomSheet(
            settings = settings,
            onDismiss = { showCreateSheet = false },
            onCreateAlert = viewModel::createAlert
        )
    }
}