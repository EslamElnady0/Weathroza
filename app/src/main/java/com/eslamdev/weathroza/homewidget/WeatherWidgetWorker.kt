package com.eslamdev.weathroza.homewidget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class WeatherWidgetWorker(
    private val appContext: Context,
    params: WorkerParameters,
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val settings = settingsRepo.getSettings()
        val cityId = settings.cityId
        val latLng = settings.userLatLng

        val data = if (latLng != null) {

            weatherRepo.refreshHomeData(latLng.latitude, latLng.longitude, settings.language)
                .first()
                .getOrNull()
                ?: weatherRepo.getCachedHomeData(cityId)
        } else {
            weatherRepo.getCachedHomeData(cityId)
        } ?: return Result.retry()

        val (weather, hourly, daily) = data

        // Update Glance state
        WeatherWidget().apply {
            updateAll(appContext)
        }

        GlanceAppWidgetManager(appContext)
            .getGlanceIds(WeatherWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(
                    appContext,
                    WeatherWidgetStateDefinition,
                    glanceId
                ) {
                    it.copy(
                        cityName = weather.name,
                        country = weather.country,
                        temp = weather.temp,
                        weatherDescription = weather.weatherDescription,
                        iconUrl = weather.iconUrl,
                        humidity = weather.humidity,
                        windSpeed = weather.windSpeed,
                        temperatureUnit = settings.temperatureUnit.name,
                        windSpeedUnit = settings.windSpeedUnit.name,
                        hourlyForecasts = hourly.take(6).map { h ->
                            HourlyWidgetItem(h.dt, h.temp, h.iconUrl)
                        },
                        dailyForecasts = daily.take(5).map { d ->
                            DailyWidgetItem(d.dt, d.tempDay, d.iconUrl, d.weatherDescription)
                        },
                        lastUpdated = System.currentTimeMillis(),
                        isLoading = false,
                    )
                }
            }

        WeatherWidget().updateAll(appContext)
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "weather_widget_worker"

        fun schedule(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequestBuilder<WeatherWidgetWorker>(1, TimeUnit.HOURS).build(),
            )
        }

        fun runOnce(context: Context) {
            WorkManager.getInstance(context).enqueue(
                OneTimeWorkRequestBuilder<WeatherWidgetWorker>().build()
            )
        }
    }
}