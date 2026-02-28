package com.eslamdev.weathroza

import android.app.Application
import androidx.work.Configuration
import com.eslamdev.weathroza.core.notification.AlertNotificationManager
import com.eslamdev.weathroza.di.dataSourceModule
import com.eslamdev.weathroza.di.databaseModule
import com.eslamdev.weathroza.di.networkModule
import com.eslamdev.weathroza.di.repositoryModule
import com.eslamdev.weathroza.di.viewModelModule
import com.eslamdev.weathroza.di.workerModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class WeathrozaApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeathrozaApp)
            workManagerFactory()
            modules(
                databaseModule,
                networkModule,
                dataSourceModule,
                repositoryModule,
                workerModule,
                viewModelModule,
            )
        }
        AlertNotificationManager(this).initChannels()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(KoinWorkerFactory())
            .build()
}