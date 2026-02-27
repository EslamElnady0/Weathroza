package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.core.helpers.AppColors

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
