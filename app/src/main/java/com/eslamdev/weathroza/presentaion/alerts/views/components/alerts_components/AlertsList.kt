package com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.usersettings.UserSettings

@Composable
fun AlertsList(
    alerts: List<AlertEntity>,
    settings: UserSettings,
    onToggle: (Long, Boolean) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        items(alerts, key = { it.id }) { alert ->
            AlertCard(
                item = alert,
                onToggle = { enabled -> onToggle(alert.id, enabled) },
                onDelete = { onDelete(alert.id) },
                settings = settings
            )
        }
    }
}
