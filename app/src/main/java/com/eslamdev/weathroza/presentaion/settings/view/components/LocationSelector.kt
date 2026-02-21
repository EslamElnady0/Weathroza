package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LocationSelector(
    selectedOptionIndex: Int,
    onGpsSelected: () -> Unit,
    onMapSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMapDialog by remember { mutableStateOf(false) }

    if (showMapDialog) {
        PickFromMapDialog(
            onDismiss = { showMapDialog = false },
            onProceed = {
                showMapDialog = false
                onMapSelected()
            }
        )
    }

    SettingsSelector(horizontalPadding = 8.dp) {
        SettingSelectorItem(
            title = stringResource(R.string.gps_automatic),
            icon = Icons.Default.LocationOn,
            isSelected = selectedOptionIndex == 0,
            onClick = onGpsSelected
        )
        SettingSelectorItem(
            title = stringResource(R.string.map_manual),
            icon = Icons.Default.Map,
            isSelected = selectedOptionIndex == 1,
            onClick = { showMapDialog = true }
        )
    }
}
