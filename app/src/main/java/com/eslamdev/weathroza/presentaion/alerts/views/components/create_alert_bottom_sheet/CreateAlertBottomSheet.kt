package com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_bottom_sheet

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.alert.resolveConfig
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.CreateAlertIntent
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.CreateAlertUiState
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.AlertNameField
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.CreateAlertButton
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.ParameterSelector
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.SheetSectionLabel
import com.eslamdev.weathroza.presentaion.alerts.views.components.create_alert_sheet_components.ThresholdSection
import com.eslamdev.weathroza.presentaion.alerts.views.components.frequency_components.FrequencyBody
import com.eslamdev.weathroza.presentaion.alerts.views.components.frequency_components.FrequencySelector
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlertBottomSheet(
    settings: UserSettings,
    state: CreateAlertUiState,
    onIntent: (CreateAlertIntent) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val config = state.selectedParam.resolveConfig(settings, context)
    val locale = settings.language.toLocale()

    fun showTimePicker(isStart: Boolean) {
        val cal = Calendar.getInstance()
        TimePickerDialog(context, { _, hour, minute ->
            val display = DateTimeHelper.formatTime(
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }.timeInMillis / 1000, locale
            )
            onIntent(
                if (isStart) CreateAlertIntent.SetStartTime(hour, minute, display)
                else CreateAlertIntent.SetEndTime(hour, minute, display)
            )
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        tonalElevation = 0.dp,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SheetSectionLabel(text = stringResource(R.string.alert_name))
            AlertNameField(
                value = state.alertName,
                onValueChange = { onIntent(CreateAlertIntent.SetName(it)) },
            )

            SheetSectionLabel(text = stringResource(R.string.select_parameter))
            ParameterSelector(
                selected = state.selectedParam,
                onSelect = { param ->
                    val newConfig = param.resolveConfig(settings, context)
                    onIntent(
                        CreateAlertIntent.SetParameter(
                            param = param,
                            initialThreshold = newConfig.minValue + (newConfig.maxValue - newConfig.minValue) / 2f,
                        )
                    )
                },
            )

            if (state.selectedParam.hasThreshold) {
                ThresholdSection(
                    config = config,
                    thresholdValue = state.thresholdValue,
                    onValueChange = { onIntent(CreateAlertIntent.SetThreshold(it)) },
                    isAbove = state.isAbove,
                    onAboveToggle = { onIntent(CreateAlertIntent.SetAbove(it)) },
                    settings = settings,
                )
            }

            SheetSectionLabel(text = stringResource(R.string.frequency))
            FrequencySelector(
                selected = state.frequency,
                onSelect = { onIntent(CreateAlertIntent.SetFrequency(it)) },
            )

            FrequencyBody(
                frequency = state.frequency,
                startTime = state.startTimeDisplay,
                endTime = state.endTimeDisplay,
                startError = state.startError,
                endError = state.endError,
                onStartClick = { showTimePicker(isStart = true) },
                onEndClick = { showTimePicker(isStart = false) },
            )

            HeightSpacer(4.0)

            CreateAlertButton(
                enabled = state.isFormValid,
                onClick = {
                    onIntent(CreateAlertIntent.Submit)
                    onDismiss()
                },
            )
        }
    }
}
