package com.eslamdev.weathroza.presentaion.map.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents.MapWeatherCard
import com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents.MapWeatherError
import com.eslamdev.weathroza.presentaion.map.views.components.mapbottomsheetcomponents.MapWeatherLoading
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapLocationBottomSheet(
    weatherState: UiState<WeatherEntity>,
    settings: UserSettings,
    sheetState: SheetState,
    selectedLatLng: LatLng?,
    onDismiss: () -> Unit,
    onConfirm: (LatLng) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppColors.sheetBg,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 6.dp)
                    .size(width = 36.dp, height = 3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AppColors.divider)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(R.string.map_weather_at),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = AppColors.label,
                letterSpacing = 1.2.sp,
                textAlign = TextAlign.Center
            )

            when (weatherState) {
                is UiState.Loading -> MapWeatherLoading()
                is UiState.Error   -> MapWeatherError(message = weatherState.message)
                is UiState.Success -> {
                    MapWeatherCard(weather = weatherState.data, settings = settings)
                    Button(
                        onClick = {
                            selectedLatLng?.let { onConfirm(it) }
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.primary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.map_select_as_location),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
                else -> {}
            }
        }
    }
}