package com.eslamdev.weathroza.presentaion.settings.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AppColors
import com.eslamdev.weathroza.presentaion.settings.view.components.LocationSelector
import com.eslamdev.weathroza.presentaion.settings.view.components.UnitsSection

@Composable
fun SettingsView(bottomController: NavController, modifier: Modifier = Modifier) {
    SettingsViewImpl(modifier)
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeBodyPreview(modifier: Modifier = Modifier) {

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold { innerPadding ->
            SettingsViewImpl(modifier.padding(innerPadding))
        }
    }

}

@Composable
fun SettingsViewImpl(modifier: Modifier = Modifier) {
    var selectedLocationIndex by remember { mutableIntStateOf(0) }
    var selectedUnitIndex by remember { mutableIntStateOf(0) }
    var selectedTempIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        HeightSpacer(48.0)
        Text("Settings", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        HeightSpacer(40.0)
        Text(
            "Location",
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
            "Switch between automatic device positioning or manual location " +
                    "selection for accurate weather data.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = AppColors.darkGray
        )
        HeightSpacer(16.0)
        HorizontalDivider()
        HeightSpacer(20.0)
        Text("Unit", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AppColors.primary)
        HeightSpacer(12.0)
        UnitsSection(
            selectedTempUnitIndex = selectedUnitIndex,
            onTempOptionSelected = { selectedUnitIndex = it },
            selectedWindUnitIndex = selectedTempIndex,
            onWindOptionSelected = { selectedTempIndex = it }
        )

    }
}

