package com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun MapWeatherError(message: String) {
    Text(
        text = message,
        color = AppColors.error,
        textAlign = TextAlign.Center,
        fontSize = 13.sp,
        lineHeight = 20.sp
    )
}