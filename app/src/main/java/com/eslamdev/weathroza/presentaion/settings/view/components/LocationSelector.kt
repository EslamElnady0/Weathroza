package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LocationSelector(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = 0,
    onOptionSelected: (Int) -> Unit = {}
) {
    SettingsSelector(horizontalPadding = 8.dp) {
        SettingSelectorItem(
            title = stringResource(R.string.gps_automatic),
            icon = Icons.Default.LocationOn,
            isSelected = selectedOptionIndex == 0,
            onClick = { onOptionSelected(0) }
        )
        SettingSelectorItem(
            title = stringResource(R.string.map_manual),
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