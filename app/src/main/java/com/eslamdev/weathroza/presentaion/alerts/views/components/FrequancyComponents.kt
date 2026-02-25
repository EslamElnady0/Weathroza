package com.eslamdev.weathroza.presentaion.alerts.views.components

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
import com.eslamdev.weathroza.core.components.SettingSelectorItem
import com.eslamdev.weathroza.core.components.SettingsSelector
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.alerts.model.AlertFrequency

@Composable
fun FrequencySelector(
    selected: AlertFrequency,
    onSelect: (AlertFrequency) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsSelector(
        modifier = modifier,
        horizontalPadding = 8.dp,
    ) {
        SettingSelectorItem(
            title = stringResource(R.string.one_time),
            isSelected = selected == AlertFrequency.ONE_TIME,
            onClick = { onSelect(AlertFrequency.ONE_TIME) },
        )
        SettingSelectorItem(
            title = stringResource(R.string.periodic),
            isSelected = selected == AlertFrequency.PERIODIC,
            onClick = { onSelect(AlertFrequency.PERIODIC) },
        )
    }
}

@Composable
fun OneTimeTimeSection(
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
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
fun PeriodicInfoLabel(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.periodic_info),
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

@Composable
fun FrequencyBody(
    frequency: AlertFrequency,
    startTime: String?,
    endTime: String?,
    startError: Int?,
    endError: Int?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = frequency == AlertFrequency.ONE_TIME,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier,
    ) {
        OneTimeTimeSection(
            startTime = startTime,
            endTime = endTime,
            startError = startError,
            endError = endError,
            onStartClick = onStartClick,
            onEndClick = onEndClick,
        )
    }

    AnimatedVisibility(
        visible = frequency == AlertFrequency.PERIODIC,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier,
    ) {
        PeriodicInfoLabel()
    }
}