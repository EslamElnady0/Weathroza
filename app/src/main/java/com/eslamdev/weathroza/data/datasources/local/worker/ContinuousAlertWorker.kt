// data/datasources/local/worker/ContinuousAlertWorker.kt
package com.eslamdev.weathroza.data.datasources.local.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.first

class ContinuousAlertWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.i("TAG", "doWork: begun")
        val alerts = weatherRepo.getContinuousAlerts()
            .first().getOrNull() ?: return Result.retry()

        val enabledAlerts = alerts.filter { it.isEnabled }
        if (enabledAlerts.isEmpty()) return Result.success()

        val settings = settingsRepo.settingsFlow.first()
        val latLng = settings.userLatLng ?: return Result.failure()

        val weather = weatherRepo
            .getWeatherFromApi(latLng.latitude, latLng.longitude, settings.language)
            .first().getOrNull() ?: return Result.retry()

        enabledAlerts.forEach { alert ->
            AlertConditionChecker.evaluate(appContext, alert, weather, settings)
        }

        return Result.success()
    }
}