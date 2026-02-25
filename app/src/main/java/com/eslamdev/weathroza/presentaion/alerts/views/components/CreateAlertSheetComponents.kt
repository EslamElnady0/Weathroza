package com.eslamdev.weathroza.presentaion.alerts.views.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.CardWithBoarder
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale
import com.eslamdev.weathroza.presentaion.alerts.model.ParameterConfig
import com.eslamdev.weathroza.presentaion.alerts.model.WeatherParameter
import kotlin.math.roundToInt

// ── Section label ─────────────────────────────────────────────────────────────

@Composable
fun SheetSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.lightGray,
        letterSpacing = 1.sp,
        modifier = modifier,
    )
}

// ── Alert name field ──────────────────────────────────────────────────────────

@Composable
fun AlertNameField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.alert_name_hint),
                color = AppColors.lightGray,
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.primary,
            unfocusedBorderColor = AppColors.primary.copy(alpha = 0.3f),
            focusedTextColor = AppColors.white,
            unfocusedTextColor = AppColors.white,
            cursorColor = AppColors.primary,
            focusedContainerColor = AppColors.primary.copy(alpha = 0.05f),
            unfocusedContainerColor = AppColors.primary.copy(alpha = 0.05f),
        ),
    )
}

// ── Parameter selector ────────────────────────────────────────────────────────

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

// ── Threshold section ─────────────────────────────────────────────────────────

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

// ── Time picker row ───────────────────────────────────────────────────────────

@Composable
fun TimePickerRow(
    startTime: String?,
    endTime: String?,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TimePickerCard(
            label = stringResource(R.string.start_time),
            value = startTime,
            onClick = onStartClick,
            modifier = Modifier.weight(1f),
        )
        TimePickerCard(
            label = stringResource(R.string.end_time),
            value = endTime,
            onClick = onEndClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TimePickerCard(
    label: String,
    value: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CardWithBoarder(
        modifier = modifier.clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = AppColors.primary,
                    modifier = Modifier.size(14.dp),
                )
                Text(text = label, fontSize = 11.sp, color = AppColors.lightGray)
            }
            Text(
                text = value ?: stringResource(R.string.pick_time),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (value != null) AppColors.white else AppColors.lightGray,
            )
        }
    }
}

// ── Create button ─────────────────────────────────────────────────────────────

@Composable
fun CreateAlertButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.primary,
            disabledContainerColor = AppColors.primary.copy(alpha = 0.3f),
        ),
    ) {
        Text(
            text = stringResource(R.string.create_alert),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = AppColors.white,
        )
    }
}