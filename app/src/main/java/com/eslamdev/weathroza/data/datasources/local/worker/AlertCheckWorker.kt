// data/datasources/local/worker/AlertCheckWorker.kt
package com.eslamdev.weathroza.data.datasources.local.worker

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.receiver.AlertReceiver
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.first

class AlertCheckWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val alertId = inputData.getLong(KEY_ALERT_ID, -1L)
        if (alertId == -1L) return Result.failure()

        // 1. Load alert
        val alert = weatherRepo.getAlertById(alertId)
            .first()
            .getOrNull() ?: return Result.success()

        if (!alert.isEnabled) return Result.success()

        // 2. Load settings
        val settings = settingsRepo.settingsFlow.first()
        val latLng = settings.userLatLng ?: return Result.failure()

        // 3. Fetch weather — retry on any failure
        val weather = weatherRepo
            .getWeatherFromApi(latLng.latitude, latLng.longitude, settings.language)
            .first()
            .getOrNull() ?: return Result.retry()

        // 4. Compute current value
        val currentValue = extractValue(
            alert.parameter, weather,
            settings.temperatureUnit, settings.windSpeedUnit,
        )

        // 5. Build localized display strings
        val unit = resolveUnit(alert.parameter, settings.temperatureUnit, settings.windSpeedUnit)
        val operator = appContext.getString(
            if (alert.isAbove) R.string.alert_operator_above else R.string.alert_operator_below
        )
        val currentDisplay = "%.1f %s".format(currentValue, unit)
        val thresholdDisplay = "%.1f %s".format(alert.threshold, unit)

        // 6. Check condition
        val conditionMet = if (alert.isAbove) currentValue >= alert.threshold
        else currentValue <= alert.threshold

        // 7. Build notification title + body
        val title = resolveTitle(alert.parameter, conditionMet)
        val body = if (conditionMet) {
            appContext.getString(
                R.string.alert_met_body,
                currentDisplay,
                operator,
                thresholdDisplay
            )
        } else {
            appContext.getString(
                R.string.alert_not_met_body,
                alert.name,
                currentDisplay,
                operator,
                thresholdDisplay,
            )
        }

        // 8. Build duration text
        val durationText = alert.endTimeMillis?.let { end ->
            val mins = (end - System.currentTimeMillis()) / 60_000
            if (mins > 0) appContext.getString(R.string.alert_active_duration, mins.toInt())
            else appContext.getString(R.string.alert_active_now)
        } ?: appContext.getString(R.string.alert_active_now)

        // 9. Fire broadcast — AlertReceiver handles sound/notification
        val action = if (conditionMet) AlertReceiver.ACTION_ALERT_START
        else AlertReceiver.ACTION_ALERT_NOT_MET

        val intent = Intent(appContext, AlertReceiver::class.java).apply {
            this.action = action
            putExtra(AlertReceiver.EXTRA_ALERT_ID, alert.id)
            putExtra(AlertReceiver.EXTRA_ALERT_NAME, alert.name)
            putExtra(AlertReceiver.EXTRA_START_MILLIS, alert.startTimeMillis)
            putExtra(AlertReceiver.EXTRA_END_MILLIS, alert.endTimeMillis ?: -1L)
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_TITLE, title)
            putExtra(AlertReceiver.EXTRA_NOTIFICATION_BODY, body)
            putExtra(AlertReceiver.EXTRA_DURATION_TEXT, durationText)
        }
        appContext.sendBroadcast(intent)

        // 10. Clean up
        weatherRepo.cancelOneTimeAlert(alertId)

        return Result.success()
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
        parameter: WeatherParameter,
        tempUnit: TemperatureUnit,
        windUnit: WindSpeedUnit,
    ): String = when (parameter) {
        WeatherParameter.TEMP -> when (tempUnit) {
            TemperatureUnit.CELSIUS -> appContext.getString(R.string.celsius_symbol)
            TemperatureUnit.FAHRENHEIT -> appContext.getString(R.string.fahrenheit_symbol)
            TemperatureUnit.KELVIN -> appContext.getString(R.string.kelvin_symbol)
        }

        WeatherParameter.WIND -> when (windUnit) {
            WindSpeedUnit.MS -> appContext.getString(R.string.unit_ms)
            WindSpeedUnit.MPH -> appContext.getString(R.string.unit_mph)
        }

        WeatherParameter.HUMIDITY -> appContext.getString(R.string.unit_percent)
        WeatherParameter.RAIN -> appContext.getString(R.string.unit_mm)
    }

    private fun resolveTitle(parameter: WeatherParameter, conditionMet: Boolean): String {
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
        return appContext.getString(resId)
    }

    companion object {
        const val KEY_ALERT_ID = "alert_id"
    }
}