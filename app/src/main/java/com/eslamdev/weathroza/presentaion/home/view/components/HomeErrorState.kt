package com.eslamdev.weathroza.presentaion.home.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors

private const val NO_LOCATION_ERROR = "No location set"

@Composable
fun HomeErrorState(
    message: String,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNoLocation = message == NO_LOCATION_ERROR

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            if (isNoLocation) {
                NoLocationContent(onNavigateToSettings = onNavigateToSettings)
            } else {
                GenericErrorContent(message = message)
            }
        }
    }
}

@Composable
private fun NoLocationContent(onNavigateToSettings: () -> Unit) {
    ErrorIcon(
        icon = Icons.Default.LocationOff,
        tint = AppColors.primary
    )

    HeightSpacer(20.0)

    Text(
        text = stringResource(R.string.error_no_location_title),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    HeightSpacer(8.0)

    Text(
        text = stringResource(R.string.error_no_location_desc),
        fontSize = 14.sp,
        color = AppColors.lightGray,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp
    )

    HeightSpacer(24.0)

    Button(
        onClick = onNavigateToSettings,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.primary
        )
    ) {
        Text(
            text = stringResource(R.string.go_to_settings),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun GenericErrorContent(message: String) {
    ErrorIcon(
        icon = Icons.Default.WifiOff,
        tint = AppColors.error
    )

    HeightSpacer(20.0)

    Text(
        text = stringResource(R.string.error_generic_title),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    HeightSpacer(8.0)

    Text(
        text = message,
        fontSize = 14.sp,
        color = AppColors.lightGray,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp
    )
}

@Composable
private fun ErrorIcon(
    icon: ImageVector,
    tint: androidx.compose.ui.graphics.Color
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(72.dp)
    )
}