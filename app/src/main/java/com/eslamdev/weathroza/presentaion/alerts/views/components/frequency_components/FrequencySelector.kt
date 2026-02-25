package com.eslamdev.weathroza.presentaion.alerts.views.components.frequency_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
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
