package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.AlertDay

@Composable
fun AlertDaySelector(
    selectedDays: Set<AlertDay>,
    onDayToggle: (AlertDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AlertDay.entries.forEach { day ->
            DayButton(
                day = day,
                isSelected = day in selectedDays,
                onClick = { onDayToggle(day) },
            )
        }
    }
}

@Composable
private fun DayButton(
    day: AlertDay,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isSelected)
        AppColors.primary
    else
        AppColors.primary.copy(alpha = 0.1f)

    val borderColor = if (isSelected)
        AppColors.primary
    else
        AppColors.primary.copy(alpha = 0.2f)

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        AppColors.primary

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier
            .size(40.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = CircleShape,
            ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(day.labelRes),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 10.sp,
                ),
            )
        }
    }
}