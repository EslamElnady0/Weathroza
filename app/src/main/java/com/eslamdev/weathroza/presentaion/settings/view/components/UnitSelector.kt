package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            title = "Celsius",
            isSelected = selectedOptionIndex == 0,
            onClick = { onOptionSelected(0) }
        )
        SettingSelectorItem(
            title = "Fahrenheit",
            isSelected = selectedOptionIndex == 1,
            onClick = { onOptionSelected(1) }
        )
        SettingSelectorItem(
            title = "Kelvin",
            isSelected = selectedOptionIndex == 2,
            onClick = { onOptionSelected(2) }
        )
    }
}