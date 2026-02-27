package com.eslamdev.weathroza.presentaion.alerts.views.components.frequency_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.core.components.SettingSelectorItem
import com.eslamdev.weathroza.core.components.SettingsSelector
import com.eslamdev.weathroza.data.models.alert.AlertFrequency

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
            title = stringResource(AlertFrequency.TIME_BASED.labelRes),
            isSelected = selected == AlertFrequency.TIME_BASED,
            onClick = { onSelect(AlertFrequency.TIME_BASED) },
        )
        SettingSelectorItem(
            title = stringResource(AlertFrequency.CONTINUOUS.labelRes),
            isSelected = selected == AlertFrequency.CONTINUOUS,
            onClick = { onSelect(AlertFrequency.CONTINUOUS) },
        )
    }
}