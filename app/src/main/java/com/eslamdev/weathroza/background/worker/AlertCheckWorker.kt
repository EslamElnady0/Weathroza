// data/datasources/local/worker/AlertCheckWorker.kt
package com.eslamdev.weathroza.background.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.first

class AlertCheckWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
    private val alarmScheduler: AlarmScheduler,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val alertId = inputData.getLong(KEY_ALERT_ID, -1L)
        if (alertId == -1L) return Result.failure()

        val alert = weatherRepo.getAlertById(alertId)
            .first().getOrNull() ?: return Result.success()

        if (!alert.isEnabled) return Result.success()

        val settings = settingsRepo.settingsFlow.first()
        val latLng = settings.userLatLng ?: return Result.failure()


        val weather = weatherRepo
            .getWeatherFromApi(latLng.latitude, latLng.longitude, settings.language)
            .first().getOrNull() ?: return Result.retry()

        AlertConditionChecker.evaluate(appContext, alert, weather, settings)

        rescheduleOrCancel(alert)

        return Result.success()
    }

    private suspend fun rescheduleOrCancel(alert: AlertEntity) {
        val nextMillis = AlertSchedulingStrategy.nextTriggerMillis(alert)
        if (nextMillis != null) {
            val updatedAlert = alert.copy(startTimeMillis = nextMillis)
            weatherRepo.updateAlertStartTime(alert.id, nextMillis)
            alarmScheduler.scheduleAlert(updatedAlert)
        } else {
            weatherRepo.cancelTimeBasedAlert(alert.id)
        }
    }

    companion object {
        const val KEY_ALERT_ID = "alert_id"
    }
}