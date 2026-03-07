package com.eslamdev.weathroza.background.worker

import android.content.Context
import android.content.Intent
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.background.receiver.AlertReceiver
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertNotifyType
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import com.eslamdev.weathroza.data.models.weather.WeatherEntity

object AlertConditionChecker {

    fun evaluate(
        context: Context,
        alert: AlertEntity,
        weather: WeatherEntity,
        settings: UserSettings,
    ) {
        val currentValue = extractValue(
            alert.parameter, weather,
            settings.temperatureUnit, settings.windSpeedUnit,
        )

        val conditionMet = if (alert.isAbove) currentValue >= alert.threshold
        else currentValue <= alert.threshold

        val unit =
            resolveUnit(context, alert.parameter, settings.temperatureUnit, settings.windSpeedUnit)
        val operator =
            context.getString(if (alert.isAbove) R.string.alert_operator_above else R.string.alert_operator_below)
        val currentDisplay = "%.1f %s".format(currentValue, unit)
        val thresholdDisplay = "%.1f %s".format(alert.threshold, unit)

        val title = resolveTitle(context, alert.parameter, conditionMet)
        val body =
            buildBody(context, conditionMet, alert.name, currentDisplay, operator, thresholdDisplay)
        val durationText = resolveDurationText(context, alert.endTimeMillis)

        sendBroadcast(context, conditionMet, alert, title, body, durationText)
    }

    // ── Broadcast ────────────────────────────────────────────────

    private fun sendBroadcast(
        context: Context,
        conditionMet: Boolean,
        alert: AlertEntity,
        title: String,
        body: String,
        durationText: String,
    ) {
        val action = if (conditionMet) AlertReceiver.ACTION_ALERT_START
        else AlertReceiver.ACTION_ALERT_NOT_MET

        val intent = Intent(context, AlertReceiver::class.java).apply {
            this.action = action
            putExtra(AlertReceiver.EXTRA_ALERT_ID, alert.id)
            putExtra(AlertReceiver.EXTRA_ALERT_NAME, alert.name)
            putExtra(AlertReceiver.EXTRA_START_MILLIS, alert.startTimeMillis)
            putExtra(AlertReceiver.EXTRA_END_MILLIS, alert.endTimeMillis ?: -1L)
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_TITLE, title)
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_BODY, body)
            putExtra(AlertReceiver.EXTRA_DURATION_TEXT, durationText)
            putExtra(
                AlertReceiver.EXTRA_NOTIFY_TYPE,
                if (conditionMet) alert.notifyType.name else AlertNotifyType.NOTIFICATION_SOUND.name
            )
        }
        context.sendBroadcast(intent)
    }

    // ── Value extraction ─────────────────────────────────────────

    private fun extractValue(
        parameter: WeatherParameter,
        weather: WeatherEntity,
        tempUnit: TemperatureUnit,
        windUnit: WindSpeedUnit,
    ): Float = when (parameter) {
        WeatherParameter.TEMP -> convertTemp(weather.temp, tempUnit).toFloat()
        WeatherParameter.WIND -> convertWind(weather.windSpeed, windUnit).toFloat()
        WeatherParameter.HUMIDITY -> weather.humidity.toFloat()
        WeatherParameter.RAIN -> 0f
    }

    private fun convertTemp(celsius: Double, unit: TemperatureUnit): Double = when (unit) {
        TemperatureUnit.CELSIUS -> celsius
        TemperatureUnit.FAHRENHEIT -> celsius * 9.0 / 5.0 + 32.0
        TemperatureUnit.KELVIN -> celsius + 273.15
    }

    private fun convertWind(ms: Double, unit: WindSpeedUnit): Double = when (unit) {
        WindSpeedUnit.MS -> ms
        WindSpeedUnit.MPH -> ms * 2.23694
    }

    // ── String helpers ───────────────────────────────────────────

    private fun resolveUnit(
        context: Context,
        parameter: WeatherParameter,
        tempUnit: TemperatureUnit,
        windUnit: WindSpeedUnit,
    ): String = when (parameter) {
        WeatherParameter.TEMP -> when (tempUnit) {
            TemperatureUnit.CELSIUS -> context.getString(R.string.celsius_symbol)
            TemperatureUnit.FAHRENHEIT -> context.getString(R.string.fahrenheit_symbol)
            TemperatureUnit.KELVIN -> context.getString(R.string.kelvin_symbol)
        }

        WeatherParameter.WIND -> when (windUnit) {
            WindSpeedUnit.MS -> context.getString(R.string.unit_ms)
            WindSpeedUnit.MPH -> context.getString(R.string.unit_mph)
        }

        WeatherParameter.HUMIDITY -> context.getString(R.string.unit_percent)
        WeatherParameter.RAIN -> context.getString(R.string.unit_mm)
    }

    private fun resolveTitle(
        context: Context,
        parameter: WeatherParameter,
        conditionMet: Boolean,
    ): String {
        val resId = if (conditionMet) {
            when (parameter) {
                WeatherParameter.TEMP -> R.string.alert_met_title_temp
                WeatherParameter.WIND -> R.string.alert_met_title_wind
                WeatherParameter.HUMIDITY -> R.string.alert_met_title_humidity
                WeatherParameter.RAIN -> R.string.alert_met_title_rain
            }
        } else {
            when (parameter) {
                WeatherParameter.TEMP -> R.string.alert_not_met_title_temp
                WeatherParameter.WIND -> R.string.alert_not_met_title_wind
                WeatherParameter.HUMIDITY -> R.string.alert_not_met_title_humidity
                WeatherParameter.RAIN -> R.string.alert_not_met_title_rain
            }
        }
        return context.getString(resId)
    }

    private fun buildBody(
        context: Context,
        conditionMet: Boolean,
        alertName: String,
        currentDisplay: String,
        operator: String,
        thresholdDisplay: String,
    ): String = if (conditionMet) {
        context.getString(R.string.alert_met_body, currentDisplay, operator, thresholdDisplay)
    } else {
        context.getString(
            R.string.alert_not_met_body,
            alertName,
            currentDisplay,
            operator,
            thresholdDisplay
        )
    }

    private fun resolveDurationText(context: Context, endTimeMillis: Long?): String =
        endTimeMillis?.let { end ->
            val mins = (end - System.currentTimeMillis()) / 60_000
            if (mins > 0) context.getString(R.string.alert_active_duration, mins.toInt())
            else context.getString(R.string.alert_active_now)
        } ?: context.getString(R.string.alert_active_now)
}