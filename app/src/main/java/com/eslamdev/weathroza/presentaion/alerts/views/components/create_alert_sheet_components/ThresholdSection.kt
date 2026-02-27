package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.data.models.alert.ParameterConfig
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale
import kotlin.math.roundToInt

@Composable
fun ThresholdSection(
    config: ParameterConfig,
    thresholdValue: Float,
    onValueChange: (Float) -> Unit,
    isAbove: Boolean,
    onAboveToggle: (Boolean) -> Unit,
    settings: UserSettings,
    modifier: Modifier = Modifier,
) {
    val locale = settings.language.toLocale()
    CardWithBoarder(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ThresholdHeader(isAbove = isAbove, onAboveToggle = onAboveToggle)
            HeightSpacer(12.0)
            ThresholdValueDisplay(
                value = thresholdValue.roundToInt().formatLocalized(locale, "%d"),
                unit = config.unit,
            )

            HeightSpacer(12.0)
            ThresholdSlider(
                value = thresholdValue,
                min = config.minValue,
                max = config.maxValue,
                onValueChange = onValueChange,
            )
            ThresholdRangeLabels(
                min = "${config.minValue.roundToInt().formatLocalized(locale, "%d")}${config.unit}",
                max = "${config.maxValue.roundToInt().formatLocalized(locale, "%d")}${config.unit}",
            )
        }
    }
}

@Composable
private fun ThresholdHeader(isAbove: Boolean, onAboveToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SheetSectionLabel(text = stringResource(R.string.set_threshold))
        AboveBelowToggle(isAbove = isAbove, onToggle = onAboveToggle)
    }
}

@Composable
private fun AboveBelowToggle(isAbove: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(true to R.string.above, false to R.string.below).forEach { (value, labelRes) ->
            val isActive = isAbove == value
            val color by animateColorAsState(
                targetValue = if (isActive) AppColors.primary else AppColors.lightGray,
                label = "toggleColor",
            )
            Text(
                text = stringResource(labelRes),
                fontSize = 12.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = color,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onToggle(value) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun ThresholdValueDisplay(value: String, unit: String) {
    Text(
        text = "$value$unit",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.white,
    )
}

@Composable
private fun ThresholdSlider(
    value: Float,
    min: Float,
    max: Float,
    onValueChange: (Float) -> Unit,
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = min..max,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = AppColors.primary,
            activeTrackColor = AppColors.primary,
            inactiveTrackColor = AppColors.primary.copy(alpha = 0.2f),
        ),
    )
}

@Composable
private fun ThresholdRangeLabels(min: String, max: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = min, fontSize = 11.sp, color = AppColors.lightGray)
        Text(text = max, fontSize = 11.sp, color = AppColors.lightGray)
    }
}
