package com.eslamdev.weathroza.core.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.helpers.toTwoDigitString
import com.eslamdev.weathroza.core.settings.UserSettings

@Composable
fun DegreeText(
    degree: Double,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    color: Color = Color.White,
    settings: UserSettings,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        "${degree.convertTemp(settings.temperatureUnit).toTwoDigitString()}°${settings.temperatureUnit.label()}",
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier
    )
}