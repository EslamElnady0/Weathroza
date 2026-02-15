package com.eslamdev.weathroza.core.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeightSpacer(height: Double, modifier: Modifier = Modifier) {
    Spacer(modifier.height(height.dp))
}

@Composable
fun WidthSpacer(width: Double, modifier: Modifier = Modifier) {
    Spacer(modifier.width(width.dp))
}

@Composable
fun ColumnScope.WeightSpacer(weight: Float, modifier: Modifier = Modifier) {
    Spacer(modifier.weight(weight))
}

@Composable
fun RowScope.WeightSpacer(weight: Float, modifier: Modifier = Modifier) {
    Spacer(modifier.weight(weight))

}