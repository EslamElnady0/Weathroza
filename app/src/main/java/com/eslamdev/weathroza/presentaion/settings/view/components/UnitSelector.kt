package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UnitSelector(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = 0,
    onOptionSelected: (Int) -> Unit = {}
) {
    SettingsSelector {
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