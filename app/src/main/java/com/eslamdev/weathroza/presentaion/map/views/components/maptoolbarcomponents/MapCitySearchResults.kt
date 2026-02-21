package com.eslamdev.weathroza.presentaion.map.views.components.maptoolbarcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapCitySearchResults(
    citiesState: UiState<List<CityEntity>>,
    language: AppLanguage,
    onCityClick: (LatLng) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldShow = citiesState is UiState.Loading
            || citiesState is UiState.Error
            || (citiesState is UiState.Success && citiesState.data.isNotEmpty())

    if (!shouldShow) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.cardBg)
    ) {
        when (citiesState) {
            is UiState.Loading -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        color = AppColors.primary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.searching),
                        fontSize = 13.sp,
                        color = AppColors.label
                    )
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onRetry)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = citiesState.message,
                        fontSize = 13.sp,
                        color = AppColors.error
                    )
                    Text(
                        text = stringResource(R.string.tap_to_retry),
                        fontSize = 12.sp,
                        color = AppColors.label
                    )
                }
            }

            is UiState.Success -> {
                if (citiesState.data.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_cities_found),
                        fontSize = 13.sp,
                        color = AppColors.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)
                    )
                } else {
                    LazyColumn {
                        items(citiesState.data, key = { "${it.lat}_${it.lon}" }) { city ->
                            MapCityResultItem(
                                city = city,
                                language = language,
                                onClick = { onCityClick(LatLng(city.lat, city.lon)) }
                            )
                            if (city != citiesState.data.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    color = AppColors.divider,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun MapCityResultItem(
    city: CityEntity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val displayName = if (language == AppLanguage.ARABIC) city.nameAr else city.nameEn
    val subtitle = listOfNotNull(
        city.state.takeIf { it.isNotBlank() },
        city.country.takeIf { it.isNotBlank() }
    ).joinToString(", ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(18.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = displayName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.value
            )
            if (subtitle.isNotBlank()) {
                Text(text = subtitle, fontSize = 12.sp, color = AppColors.label)
            }
        }
    }
}