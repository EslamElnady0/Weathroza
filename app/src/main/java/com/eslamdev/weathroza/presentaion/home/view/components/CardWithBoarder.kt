package com.eslamdev.weathroza.presentaion.home.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun CardWithBoarder(modifier: Modifier = Modifier, content: @Composable (ColumnScope.() -> Unit)) {
    Card(
        colors = cardColors(containerColor = AppColors.primary.copy(alpha = 0.1f)),
        modifier = modifier.border(
            width = 1.dp, AppColors.primary.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        content()
    }
}