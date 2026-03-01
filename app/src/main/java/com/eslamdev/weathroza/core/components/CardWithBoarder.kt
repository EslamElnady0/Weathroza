package com.eslamdev.weathroza.core.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun CardWithBoarder(
    modifier: Modifier = Modifier,
    containerColor: Color = AppColors.primary.copy(alpha = 0.1f),
    borderColor: Color = AppColors.primary.copy(alpha = 0.2f),
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        colors = cardColors(containerColor = containerColor),
        modifier = modifier.border(
            width = 1.dp,
            color = borderColor,
            shape = RoundedCornerShape(12.dp)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        content()
    }
}