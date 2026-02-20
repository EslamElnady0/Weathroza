package com.eslamdev.weathroza.presentaion.map.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.components.DegreeText
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.helpers.convertTemp
import com.eslamdev.weathroza.core.helpers.convertWind
import com.eslamdev.weathroza.core.helpers.formatLocalized
import com.eslamdev.weathroza.core.helpers.label
import com.eslamdev.weathroza.core.helpers.toLocalizedPercentage
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.toLocale
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
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
                is UiState.Loading -> {
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

                is UiState.Error -> {
                    Text(
                        text = weatherState.message,
                        color = Color(0xFFFF5370),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }

                is UiState.Success -> {
                    WeatherCard(weather = weatherState.data, settings = settings)

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherCard(weather: WeatherEntity, settings: UserSettings) {
    val locale = settings.language.toLocale()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.cardBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = weather.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AppColors.value
                )
                Text(
                    text = weather.country,
                    fontSize = 13.sp,
                    color = AppColors.label,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                DegreeText(
                    degree = weather.temp,
                    fontSize = 38.sp,
                    color = AppColors.value,
                    settings = settings,
                    fontWeight = FontWeight.Light
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val minLabel = "${
                        weather.tempMin.convertTemp(settings.temperatureUnit)
                            .formatLocalized(locale, "%d")
                    }°${settings.temperatureUnit.label()}"

                    val maxLabel = "${
                        weather.tempMax.convertTemp(settings.temperatureUnit)
                            .formatLocalized(locale, "%d")
                    }°${settings.temperatureUnit.label()}"


                    Text(
                        text = "↑ $maxLabel",
                        fontSize = 13.sp,
                        color = Color(0xFFFF8A65),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "·",
                        fontSize = 13.sp,
                        color = AppColors.label
                    )
                    Text(
                        text = "↓ $minLabel",
                        fontSize = 13.sp,
                        color = Color(0xFF6BAAFF),
                        fontWeight = FontWeight.Medium
                    )

                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                GlideImage(
                    model = weather.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = weather.weatherDescription,
                    fontSize = 12.sp,
                    color = AppColors.label,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Thin divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AppColors.divider)
        )

        val feelsLikeLabel = "${
            weather.feelsLike.convertTemp(settings.temperatureUnit)
                .formatLocalized(locale, "%d")
        }°${settings.temperatureUnit.label()}"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.feels_like_minimal),
                fontSize = 13.sp,
                color = AppColors.label
            )
            Text(
                text = feelsLikeLabel,
                fontSize = 13.sp,
                color = AppColors.value,
                fontWeight = FontWeight.Medium
            )
        }

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.statBg)
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherStatItem(
                label = stringResource(R.string.humidity),
                value = weather.humidity.toLocalizedPercentage(locale)
            )
            VerticalStatDivider()
            WeatherStatItem(
                label = stringResource(R.string.wind),
                value = "${
                    weather.windSpeed.convertWind(settings.windSpeedUnit)
                        .formatLocalized(locale, "%.1f")
                } ${settings.windSpeedUnit.label()}"
            )
            VerticalStatDivider()
            WeatherStatItem(
                label = stringResource(R.string.pressure),
                value = "${weather.pressure.formatLocalized(locale, "%d")} ${
                    stringResource(R.string.pressure_unit)
                }"
            )
        }
    }
}

@Composable
private fun WeatherStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            color = AppColors.label,
            letterSpacing = 0.8.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.value
        )
    }
}

@Composable
private fun VerticalStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(AppColors.divider)
    )
}