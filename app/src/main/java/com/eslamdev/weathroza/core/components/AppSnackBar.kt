package com.eslamdev.weathroza.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.SnackbarEvent
import com.eslamdev.weathroza.core.helpers.SnackbarType
import com.eslamdev.weathroza.core.helpers.resolve

@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    currentEvent: SnackbarEvent?,
    type: SnackbarType
) {
    val backgroundColor = when (type) {
        SnackbarType.SUCCESS -> AppColors.primary
        SnackbarType.ERROR -> AppColors.error
    }

    val icon = when (type) {
        SnackbarType.SUCCESS -> Icons.Default.CheckCircle
        SnackbarType.ERROR -> Icons.Default.ErrorOutline
    }

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 400, easing = EaseOutQuart)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300, easing = EaseInQuart)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                WidthSpacer(10.0)
                Text(
                    text = currentEvent?.message?.resolve() ?: snackbarData.visuals.message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                snackbarData.visuals.actionLabel?.let { label ->
                    TextButton(onClick = { snackbarData.performAction() }) {
                        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}