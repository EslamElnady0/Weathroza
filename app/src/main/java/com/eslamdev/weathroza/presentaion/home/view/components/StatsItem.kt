package com.eslamdev.weathroza.presentaion.home.view.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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

