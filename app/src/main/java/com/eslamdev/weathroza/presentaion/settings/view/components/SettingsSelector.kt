package com.eslamdev.weathroza.presentaion.settings.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun RowScope.SettingSelectorItem(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,

    ) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.primary.copy(alpha = 0.2f) else Color.Transparent,
        label = "backgroundColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.primary else AppColors.lightGray,
        label = "contentColor"
    )
    val titleColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.primary else AppColors.darkGray,
        label = "titleColor"
    )

    Column(
        modifier = modifier
            .weight(1f)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
            HeightSpacer(8.0)
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = titleColor,
                fontSize = 12.sp
            )
        )
        subtitle?.let {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AppColors.lightGray,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun SettingsSelector(
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int = 0,
    horizontalPadding: androidx.compose.ui.unit.Dp = 16.dp,
    onOptionSelected: (Int) -> Unit = {},
    content: @Composable (RowScope.() -> Unit)
) {
    CardWithBoarder(modifier = modifier) {
        Spacer(modifier = Modifier.size(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}

