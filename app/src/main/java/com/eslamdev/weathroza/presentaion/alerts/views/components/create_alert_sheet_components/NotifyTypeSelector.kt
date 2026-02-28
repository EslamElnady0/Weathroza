package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.AlertNotifyType

@Composable
fun NotifyTypeSelector(
    selected: AlertNotifyType,
    onSelect: (AlertNotifyType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AlertNotifyType.entries.forEach { type ->
            NotifyTypeItem(
                type = type,
                isSelected = type == selected,
                onClick = { onSelect(type) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun NotifyTypeItem(
    type: AlertNotifyType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) AppColors.primary else AppColors.primary.copy(alpha = 0.1f)
    val borderColor = if (isSelected) AppColors.primary else AppColors.primary.copy(alpha = 0.2f)
    val contentColor = if (isSelected) AppColors.white else AppColors.primary
    val shape = RoundedCornerShape(12.dp)

    Surface(
        onClick = onClick,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.border(1.dp, borderColor, shape),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = when (type) {
                    AlertNotifyType.ALARM -> Icons.Default.Alarm
                    AlertNotifyType.NOTIFICATION_SOUND -> Icons.Default.Notifications
                },
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(
                        when (type) {
                            AlertNotifyType.ALARM -> R.string.notify_type_alarm
                            AlertNotifyType.NOTIFICATION_SOUND -> R.string.notify_type_notification
                        }
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    ),
                )
                Text(
                    text = stringResource(
                        when (type) {
                            AlertNotifyType.ALARM -> R.string.notify_type_alarm_desc
                            AlertNotifyType.NOTIFICATION_SOUND -> R.string.notify_type_notification_desc
                        }
                    ),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = contentColor.copy(alpha = 0.7f),
                    ),
                )
            }
        }
    }
}