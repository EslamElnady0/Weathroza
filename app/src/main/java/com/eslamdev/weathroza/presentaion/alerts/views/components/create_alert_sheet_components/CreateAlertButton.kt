package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors

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
