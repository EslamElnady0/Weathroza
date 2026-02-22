package com.eslamdev.weathroza.presentaion.settings.view

import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.core.settings.LocationType
import com.eslamdev.weathroza.core.settings.UserSettings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import com.eslamdev.weathroza.presentaion.routes.Route
import com.eslamdev.weathroza.presentaion.settings.view.components.LanguageSelector
import com.eslamdev.weathroza.presentaion.settings.view.components.LocationSelector
import com.eslamdev.weathroza.presentaion.settings.view.components.SettingsSnackbar
import com.eslamdev.weathroza.presentaion.settings.view.components.UnitsSection
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsView(bottomController: NavController,
                 settingsViewModel: SettingsViewModel,
                 modifier: Modifier = Modifier,
                 onNavigateToMap: ()->Unit,) {

    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()
    val snackBarState by settingsViewModel.snackBarState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        SettingsViewImpl(
            settings = settings,
            onTemperatureUnitChanged = settingsViewModel::onTemperatureUnitChanged,
            onWindSpeedUnitChanged = settingsViewModel::onWindSpeedUnitChanged,
            onLanguageChanged = settingsViewModel::onLanguageChanged,
            onGpsSelected = settingsViewModel::onGpsLocationSelected,
            onNavigateToMap = onNavigateToMap
        )

        SettingsSnackbar(
            message = snackBarState.message,
            isVisible = snackBarState.isVisible,
            isLoading = snackBarState.isLoading,
            isError = snackBarState.isError,
            onDismiss = settingsViewModel::onSnackBarDismissed,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}


@Composable
fun SettingsViewImpl(settings: UserSettings,
                     onTemperatureUnitChanged: (Int) -> Unit,
                     onWindSpeedUnitChanged: (Int) -> Unit,
                     onLanguageChanged: (AppLanguage) -> Unit,
                     onGpsSelected: () -> Unit,
                     onNavigateToMap: () -> Unit,
                     modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
            HeightSpacer(32.0)
            Text(stringResource(R.string.settings), fontSize = 30.sp, fontWeight = FontWeight.Bold)
            HeightSpacer(16.0)
            Text(
                stringResource(R.string.location),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.primary
            )

            HeightSpacer(16.0)
            LocationSelector(
                selectedOptionIndex = when (settings.locationType) {
                    LocationType.GPS    -> 0
                    LocationType.MANUAL -> 1
                    LocationType.NONE   -> -1
                },
                onGpsSelected = onGpsSelected,
                onMapSelected = onNavigateToMap
            )
            HeightSpacer(12.0)
            Text(
                stringResource(R.string.location_description),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = AppColors.darkGray
            )
            HeightSpacer(12.0)
            HorizontalDivider()
            HeightSpacer(16.0)
            Text(
                stringResource(R.string.unit),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.primary
            )
            HeightSpacer(12.0)
            UnitsSection(
                selectedTempUnitIndex = settings.temperatureUnit.ordinal,
                onTempOptionSelected = onTemperatureUnitChanged,
                selectedWindUnitIndex = settings.windSpeedUnit.ordinal,
                onWindOptionSelected = onWindSpeedUnitChanged
            )
            HeightSpacer(12.0)
            HorizontalDivider()
            HeightSpacer(16.0)
            Text(
                stringResource(R.string.preferences),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.primary
            )
            HeightSpacer(12.0)
            LanguageSelector(
                selectedLanguage = settings.language,
                onLanguageSelected = onLanguageChanged
            )
            HeightSpacer(32.0)

    }
}



