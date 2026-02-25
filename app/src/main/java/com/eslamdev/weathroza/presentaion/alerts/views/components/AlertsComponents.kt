package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.DeleteDialog
import com.eslamdev.weathroza.core.components.SwipeToDeleteBox
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale

@Composable
fun AlertsHeader(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.alerts),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = AppColors.white,
            fontSize = 28.sp,
        ),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
fun AlertCard(
    item: AlertEntity,
    settings: UserSettings,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteDialog(
            title = stringResource(R.string.delete_alert_title),
            description = stringResource(R.string.delete_alert_desc, item.name),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
        )
    }

    SwipeToDeleteBox(
        onSwipedToDelete = { showDeleteDialog = true },
        modifier = modifier,
    ) {
        CardWithBoarder(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AlertCardIcon(parameter = item.parameter)
                AlertCardInfo(
                    title = item.name,
                    timeRange = formatTimeRange(item.startTimeMillis, item.endTimeMillis, settings),
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = item.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = AppColors.primary,
                        checkedThumbColor = AppColors.white,
                        uncheckedTrackColor = AppColors.primary.copy(alpha = 0.2f),
                    ),
                )
            }
        }
    }
}

@Composable
private fun AlertCardIcon(parameter: WeatherParameter) {
    Box(
        modifier = Modifier.size(44.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = parameter.icon,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun AlertCardInfo(
    title: String,
    timeRange: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = AppColors.white,
            ),
        )
        Text(
            text = timeRange,
            style = MaterialTheme.typography.bodySmall.copy(color = AppColors.lightGray),
        )
    }
}

@Composable
private fun AlertCardActions(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedTrackColor = AppColors.primary,
                uncheckedTrackColor = AppColors.primary.copy(alpha = 0.2f),
            ),
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_alert),
                tint = AppColors.lightGray,
            )
        }
    }
}

@Composable
private fun formatTimeRange(
    startMillis: Long?,
    endMillis: Long?,
    settings: UserSettings,
): String {
    if (startMillis == null) return stringResource(R.string.periodic)
    val start = DateTimeHelper.formatTime(startMillis / 1000, settings.language.toLocale())
    val end = endMillis?.let { DateTimeHelper.formatTime(it / 1000, settings.language.toLocale()) }
    return if (end != null) "$start - $end" else start
}

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

@Composable
fun EmptyAlertsState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_alerts),
            style = MaterialTheme.typography.bodyLarge.copy(color = AppColors.lightGray),
        )
    }
}