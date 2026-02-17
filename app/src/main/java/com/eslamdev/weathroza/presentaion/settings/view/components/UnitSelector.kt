package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R

@Composable
fun UnitSelector(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = 0,
    horizontalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    onOptionSelected: (Int) -> Unit = {}
) {
    SettingsSelector(
        modifier = modifier,
        horizontalPadding = horizontalPadding
    ) {
        SettingSelectorItem(
            title = stringResource(R.string.celsius),
            isSelected = selectedOptionIndex == 0,
            contentPadding = 4.dp,
            onClick = { onOptionSelected(0) }
        )
        SettingSelectorItem(
            title = stringResource(R.string.fahrenheit),
            isSelected = selectedOptionIndex == 1,
            contentPadding = 4.dp,
            onClick = { onOptionSelected(1) }
        )
        SettingSelectorItem(
            title = stringResource(R.string.kelvin),
            isSelected = selectedOptionIndex == 2,
            contentPadding = 4.dp,
            onClick = { onOptionSelected(2) }
        )
    }
}