package com.eslamdev.weathroza.data.datasources.local.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo

class AlertWorkerFactory(
    private val weatherRepo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            AlertCheckWorker::class.java.name ->
                AlertCheckWorker(appContext, workerParameters, weatherRepo, settingsRepo)

            else -> null
        }
    }
}