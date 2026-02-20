package com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.helpers.AppColors

@Composable
fun MapWeatherLoading() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressIndicator(
            color = AppColors.primary,
            strokeWidth = 2.dp,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = stringResource(R.string.map_loading_weather),
            color = AppColors.label,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}