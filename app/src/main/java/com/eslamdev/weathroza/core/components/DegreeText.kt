package com.eslamdev.weathroza.core.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale

@Composable
fun DegreeText(
    degree: Double,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    color: Color = Color.White,
    settings: UserSettings,
    fontWeight: FontWeight = FontWeight.Normal
) {
    val context = LocalContext.current
    Text(
        "${
            degree.convertTemp(settings.temperatureUnit)
                .formatLocalized(
                    locale = settings.language.toLocale(),
                    pattern = "%d"
                )
        }°${settings.temperatureUnit.label(context)}",
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight,
        modifier = modifier
    )
}