package com.eslamdev.weathroza.core.components.weathercomps

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eslamdev.weathroza.core.components.CardWithBoarder

@Composable
fun StatusItem(
    @DrawableRes icon: Int,
    label: String,
    value: String,
    contentDesc: String,
    modifier: Modifier = Modifier
) {
    CardWithBoarder(modifier) {
        StatsRow(
            icon = icon,
            label = label,
            value = value,
            contentDesc = contentDesc
        )
    }
}

