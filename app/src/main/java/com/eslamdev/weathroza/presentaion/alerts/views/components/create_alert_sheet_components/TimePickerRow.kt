package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun TimePickerRow(
    startTime: String?,
    endTime: String?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TimePickerCard(
            label = stringResource(R.string.start_time),
            value = startTime,
            onClick = onStartClick,
            modifier = Modifier.weight(1f),
        )
        TimePickerCard(
            label = stringResource(R.string.end_time),
            value = endTime,
            onClick = onEndClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TimePickerCard(
    label: String,
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(
        modifier = modifier.clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = AppColors.primary,
                    modifier = Modifier.size(14.dp),
                )
                Text(text = label, fontSize = 11.sp, color = AppColors.lightGray)
            }
            Text(
                text = value ?: stringResource(R.string.pick_time),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (value != null) AppColors.white else AppColors.lightGray,
            )
        }
    }
}
