package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eslamdev.weathroza.presentaion.alerts.model.AlertItem

@Composable
fun ScheduledTabBody(
    alerts: List<AlertItem>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (alerts.isEmpty()) {
        EmptyAlertsState(modifier = modifier)
    } else {
        AlertsList(alerts = alerts, onToggle = onToggle, modifier = modifier)
    }
}

@Composable
fun WeatherTabBody(
    alerts: List<AlertItem>,
    onToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (alerts.isEmpty()) {
        EmptyAlertsState(modifier = modifier)
    } else {
        AlertsList(alerts = alerts, onToggle = onToggle, modifier = modifier)
    }
}