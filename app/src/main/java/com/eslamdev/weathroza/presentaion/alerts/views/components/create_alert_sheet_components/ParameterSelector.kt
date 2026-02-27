package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.data.models.alert.WeatherParameter

@Composable
fun ParameterSelector(
    selected: WeatherParameter,
    onSelect: (WeatherParameter) -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            WeatherParameter.entries.forEach { param ->
                ParameterChip(
                    parameter = param,
                    isSelected = selected == param,
                    onClick = { onSelect(param) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ParameterChip(
    parameter: WeatherParameter,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.primary.copy(alpha = 0.2f) else Color.Transparent,
        label = "chipBg",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) AppColors.primary else AppColors.lightGray,
        label = "chipContent",
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = parameter.icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(parameter.labelRes),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor,
        )
    }
}
