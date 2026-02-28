package com.eslamdev.weathroza.data.datasources.local.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.homewidget.WeatherWidgetWorker

class AlertWorkerFactory(
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
    private val alarmScheduler: AlarmScheduler,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            AlertCheckWorker::class.java.name ->
                AlertCheckWorker(
                    appContext,
                    workerParameters,
                    weatherRepo,
                    settingsRepo,
                    alarmScheduler
                )

            ContinuousAlertWorker::class.java.name ->
                ContinuousAlertWorker(appContext, workerParameters, weatherRepo, settingsRepo)

            WeatherWidgetWorker::class.java.name ->
                WeatherWidgetWorker(appContext, workerParameters, weatherRepo, settingsRepo)

            else -> null
        }
    }
}