package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.SettingSelectorItem

enum class AlertTab { SCHEDULED, WEATHER }

@Composable
fun RowScope.ScheduledSelectorItem(isSelected: Boolean, onClick: () -> Unit) {
    SettingSelectorItem(
        title = stringResource(R.string.scheduled),
        icon = Icons.Default.CalendarMonth,
        isSelected = isSelected,
        onClick = onClick,
    )
}

@Composable
fun RowScope.WeatherSelectorItem(isSelected: Boolean, onClick: () -> Unit) {
    SettingSelectorItem(
        title = stringResource(R.string.weather),
        icon = Icons.Default.Thunderstorm,
        isSelected = isSelected,
        onClick = onClick,
    )
}