package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LocationSelector(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = 0,
    onOptionSelected: (Int) -> Unit = {}
) {
    SettingsSelector {
        SettingSelectorItem(
            title = "GPS (Automatic)",
            icon = Icons.Default.LocationOn,
            isSelected = selectedOptionIndex == 0,
            onClick = { onOptionSelected(0) }
        )
        SettingSelectorItem(
            title = "Map (Manual)",
            icon = Icons.Default.Map,
            isSelected = selectedOptionIndex == 1,
            onClick = { onOptionSelected(1) }
        )
    }
}

@Preview
@Composable
fun LocationSelectorPreview() {
    LocationSelector { }
}