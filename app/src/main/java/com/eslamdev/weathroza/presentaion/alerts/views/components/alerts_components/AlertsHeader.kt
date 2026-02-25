package com.eslamdev.weathroza.presentaion.alerts.views.components.alerts_components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun AlertsHeader(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.alerts),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = AppColors.white,
            fontSize = 28.sp,
        ),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}
