package com.eslamdev.weathroza

import android.app.Application
import androidx.work.Configuration
import com.eslamdev.weathroza.core.notification.AlertNotificationManager
import com.eslamdev.weathroza.data.datasources.local.worker.AlertWorkerFactory

class WeathrozaApp : Application(), Configuration.Provider {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl(this)
        AlertNotificationManager(this).initChannels()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                AlertWorkerFactory(
                    weatherRepo = appContainer.weatherRepo,
                    settingsRepo = appContainer.userSettingsRepo,
                    alarmScheduler = appContainer.alarmScheduler
                )
            ).build()
}