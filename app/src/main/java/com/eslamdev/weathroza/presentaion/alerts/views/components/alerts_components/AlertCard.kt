package com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
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
import com.eslamdev.weathroza.core.helpers.resolveDisplayThreshold
import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.alert.resolveConfig
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    AlertCardIcon(parameter = item.parameter)
                    AlertCardInfo(
                        title = item.name,
                        threshold = if (item.parameter.hasThreshold) {
                            val context = LocalContext.current
                            val config = item.parameter.resolveConfig(settings, context)
                            val sign = if (item.isAbove) ">" else "<"
                            val displayValue = item.resolveDisplayThreshold(settings)
                            "$sign $displayValue${config.unit}"
                        } else null,
                        timeRange = formatTimeRange(item, settings),
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

                if (item.frequency == AlertFrequency.TIME_BASED && item.repeatDays.isNotEmpty()) {
                    AlertDayRow(
                        allDays = AlertDay.entries,
                        selectedDays = item.repeatDays,
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertDayRow(
    allDays: List<AlertDay>,
    selectedDays: Set<AlertDay>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        allDays
            .forEach { day ->
                val isSelected = day in selectedDays
                val containerColor =
                    if (isSelected) AppColors.primary else AppColors.primary.copy(alpha = 0.1f)
                val borderColor =
                    if (isSelected) AppColors.primary else AppColors.primary.copy(alpha = 0.2f)
                val contentColor = if (isSelected) AppColors.white else AppColors.primary

                Surface(
                    shape = CircleShape,
                    color = containerColor,
                    contentColor = contentColor,
                    modifier = Modifier
                        .size(32.dp)
                        .border(width = 1.dp, color = borderColor, shape = CircleShape),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(day.labelRes),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 9.sp,
                            ),
                        )
                    }
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
    threshold: String?,
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
        threshold?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AppColors.primary,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
        Text(
            text = timeRange,
            style = MaterialTheme.typography.bodySmall.copy(color = AppColors.lightGray),
        )
    }
}

@Composable
private fun formatTimeRange(
    item: AlertEntity,
    settings: UserSettings,
): String {
    if (item.frequency == AlertFrequency.CONTINUOUS) return stringResource(R.string.continuous)
    val startMillis = item.startTimeMillis ?: return ""
    val start = DateTimeHelper.formatTime(startMillis / 1000, settings.language.toLocale())
    val end = item.endTimeMillis?.let {
        DateTimeHelper.formatTime(it / 1000, settings.language.toLocale())
    }
    return if (end != null) "$start - $end" else start
}