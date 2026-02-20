package com.eslamdev.weathroza.presentaion.settings.view

import android.content.res.Configuration
import androidx.compose.ui.res.stringResource
import com.eslamdev.weathroza.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.presentaion.settings.view.components.AppLanguage
import com.eslamdev.weathroza.presentaion.settings.view.components.LanguageSelector
import com.eslamdev.weathroza.presentaion.settings.view.components.LocationSelector
import com.eslamdev.weathroza.presentaion.settings.view.components.UnitsSection
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModelFactory

@Composable
fun SettingsView(bottomController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    SettingsViewImpl(
        settings = settings,
        onTemperatureUnitChanged = viewModel::onTemperatureUnitChanged,
        onWindSpeedUnitChanged = viewModel::onWindSpeedUnitChanged,
        modifier = modifier
    )
}


@Composable
fun SettingsViewImpl(settings: UserSettings,
                     onTemperatureUnitChanged: (Int) -> Unit,
                     onWindSpeedUnitChanged: (Int) -> Unit,
                     modifier: Modifier = Modifier) {
    var selectedLocationIndex by remember { mutableIntStateOf(0) }
    var selectedLanguage by remember { mutableStateOf(AppLanguage.SYSTEM) }

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
                selectedOptionIndex = selectedLocationIndex,
                onOptionSelected = { selectedLocationIndex = it }
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
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it }
            )
            HeightSpacer(32.0)

    }
}

