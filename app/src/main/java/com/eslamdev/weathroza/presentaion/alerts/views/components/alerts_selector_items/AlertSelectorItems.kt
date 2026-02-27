package com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_selector_items

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.SettingSelectorItem

@Composable
fun RowScope.TimeBasedSelectorItem(isSelected: Boolean, onClick: () -> Unit) {
    SettingSelectorItem(
        title = stringResource(R.string.time_based),
        icon = Icons.Default.CalendarMonth,
        isSelected = isSelected,
        onClick = onClick,
    )
}

@Composable
fun RowScope.ContinuousSelectorItem(isSelected: Boolean, onClick: () -> Unit) {
    SettingSelectorItem(
        title = stringResource(R.string.continuous),
        icon = Icons.Default.Autorenew,
        isSelected = isSelected,
        onClick = onClick,
    )
}