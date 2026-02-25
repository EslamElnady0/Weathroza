package com.eslamdev.weathroza.presentaion.alerts.views.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.components.HeightSpacer
import com.eslamdev.weathroza.core.helpers.AlertTimeValidator
import com.eslamdev.weathroza.core.helpers.DateTimeHelper
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.toLocale
import com.eslamdev.weathroza.presentaion.alerts.model.AlertFrequency
import com.eslamdev.weathroza.presentaion.alerts.model.WeatherParameter
import com.eslamdev.weathroza.presentaion.alerts.model.resolveConfig
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlertBottomSheet(
    settings: UserSettings,
    onDismiss: () -> Unit,
    onCreateAlert: (
        name: String,
        parameter: WeatherParameter,
        threshold: Float,
        isAbove: Boolean,
        frequency: AlertFrequency,
        startTime: String?,
        endTime: String?,
    ) -> Unit,
) {
    var alertName by remember { mutableStateOf("") }
    var selectedParam by remember { mutableStateOf(WeatherParameter.TEMP) }
    var isAbove by remember { mutableStateOf(true) }
    var frequency by remember { mutableStateOf(AlertFrequency.ONE_TIME) }


    var startHour by remember { mutableIntStateOf(-1) }
    var startMinute by remember { mutableIntStateOf(-1) }
    var endHour by remember { mutableIntStateOf(-1) }
    var endMinute by remember { mutableIntStateOf(-1) }

    var startTimeDisplay by remember { mutableStateOf<String?>(null) }
    var endTimeDisplay by remember { mutableStateOf<String?>(null) }


    var startError by remember { mutableStateOf<Int?>(null) }
    var endError by remember { mutableStateOf<Int?>(null) }

    val config = selectedParam.resolveConfig(settings)
    var thresholdValue by remember(config) {
        mutableFloatStateOf(config.minValue + (config.maxValue - config.minValue) / 2f)
    }

    val context = LocalContext.current
    val locale = settings.language.toLocale()

    fun formatAndStore(hour: Int, minute: Int): String {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        return DateTimeHelper.formatTime(cal.timeInMillis / 1000, locale)
    }

    fun validateTimes(): Boolean {
        startError = null
        endError = null

        if (startHour != -1) {
            when {
                !AlertTimeValidator.isInFuture(startHour, startMinute) ->
                    startError = R.string.error_start_time_past

                !AlertTimeValidator.isWithinMaxFuture(startHour, startMinute) ->
                    startError = R.string.error_time_too_far
            }
        }

        if (endHour != -1) {
            when {
                !AlertTimeValidator.isWithinMaxFuture(endHour, endMinute) ->
                    endError = R.string.error_time_too_far

                startHour != -1 && !AlertTimeValidator.isEndAfterStart(
                    startHour, startMinute, endHour, endMinute
                ) -> endError = R.string.error_end_before_start
            }
        }

        return startError == null && endError == null
    }

    fun showTimePicker(isStart: Boolean) {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                if (isStart) {
                    startHour = hour
                    startMinute = minute
                    startTimeDisplay = formatAndStore(hour, minute)
                    // Re-validate end if already picked
                    if (endHour != -1) validateTimes()
                    else startError = if (!AlertTimeValidator.isInFuture(hour, minute))
                        R.string.error_start_time_past else null
                } else {
                    endHour = hour
                    endMinute = minute
                    endTimeDisplay = formatAndStore(hour, minute)
                    validateTimes()
                }
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false,
        ).show()
    }

    val isFormValid = alertName.isNotBlank()
            && startError == null
            && endError == null
            && (frequency == AlertFrequency.PERIODIC || startHour != -1)

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
            // Name
            SheetSectionLabel(text = stringResource(R.string.alert_name))
            AlertNameField(value = alertName, onValueChange = { alertName = it })

            // Parameter
            SheetSectionLabel(text = stringResource(R.string.select_parameter))
            ParameterSelector(selected = selectedParam, onSelect = { selectedParam = it })

            // Threshold
            if (selectedParam.hasThreshold) {
                ThresholdSection(
                    config = config,
                    thresholdValue = thresholdValue,
                    onValueChange = { thresholdValue = it },
                    isAbove = isAbove,
                    onAboveToggle = { isAbove = it },
                    settings = settings,
                )
            }

            // Frequency selector
            SheetSectionLabel(text = stringResource(R.string.frequency))
            FrequencySelector(
                selected = frequency,
                onSelect = {
                    frequency = it
                    // Clear time state when switching to periodic
                    if (it == AlertFrequency.PERIODIC) {
                        startHour = -1; startMinute = -1
                        endHour = -1; endMinute = -1
                        startTimeDisplay = null; endTimeDisplay = null
                        startError = null; endError = null
                    }
                },
            )

            // Frequency body — time pickers or periodic info
            FrequencyBody(
                frequency = frequency,
                startTime = startTimeDisplay,
                endTime = endTimeDisplay,
                startError = startError,
                endError = endError,
                onStartClick = { showTimePicker(isStart = true) },
                onEndClick = { showTimePicker(isStart = false) },
            )

            HeightSpacer(4.0)

            CreateAlertButton(
                enabled = isFormValid,
                onClick = {
                    if (validateTimes() || frequency == AlertFrequency.PERIODIC) {
                        onCreateAlert(
                            alertName, selectedParam, thresholdValue, isAbove,
                            frequency, startTimeDisplay, endTimeDisplay,
                        )
                        onDismiss()
                    }
                },
            )
        }
    }
}