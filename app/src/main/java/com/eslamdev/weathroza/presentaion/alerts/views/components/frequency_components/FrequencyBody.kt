package com.eslamdev.weathroza.presentaion.alerts.views.components.frequency_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.AlertDay
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.AlertDaySelector
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.SheetSectionLabel
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.TimePickerRow

@Composable
fun FrequencyBody(
    frequency: AlertFrequency,
    selectedDays: Set<AlertDay>,
    onDayToggle: (AlertDay) -> Unit,
    startTime: String?,
    endTime: String?,
    startError: Int?,
    endError: Int?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = frequency == AlertFrequency.TIME_BASED,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier,
    ) {
        TimeBasedSection(
            selectedDays = selectedDays,
            onDayToggle = onDayToggle,
            startTime = startTime,
            endTime = endTime,
            startError = startError,
            endError = endError,
            onStartClick = onStartClick,
            onEndClick = onEndClick,
        )
    }

    AnimatedVisibility(
        visible = frequency == AlertFrequency.CONTINUOUS,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier,
    ) {
        ContinuousInfoLabel()
    }
}

@Composable
private fun TimeBasedSection(
    selectedDays: Set<AlertDay>,
    onDayToggle: (AlertDay) -> Unit,
    startTime: String?,
    endTime: String?,
    startError: Int?,
    endError: Int?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SheetSectionLabel(text = stringResource(R.string.repeat_on))

        AlertDaySelector(
            selectedDays = selectedDays,
            onDayToggle = onDayToggle,
        )

        TimePickerRow(
            startTime = startTime,
            endTime = endTime,
            onStartClick = onStartClick,
            onEndClick = onEndClick,
        )

        startError?.let { TimeErrorLabel(messageRes = it) }
        endError?.let { TimeErrorLabel(messageRes = it) }
    }
}

@Composable
private fun ContinuousInfoLabel(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.continuous_info),
        style = MaterialTheme.typography.bodySmall,
        color = AppColors.lightGray,
        modifier = modifier,
    )
}

@Composable
private fun TimeErrorLabel(messageRes: Int) {
    val message = if (messageRes == R.string.error_time_too_far) {
        stringResource(messageRes, 6)
    } else {
        stringResource(messageRes)
    }
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = Color.Red,
    )
}