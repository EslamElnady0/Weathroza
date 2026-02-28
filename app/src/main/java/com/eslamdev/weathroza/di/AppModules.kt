package com.eslamdev.weathroza.di

import com.eslamdev.weathroza.AppViewModel
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.config.network.RetrofitHelper
import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.LocationManager
import com.eslamdev.weathroza.data.datasources.local.NetworkObserver
import com.eslamdev.weathroza.data.datasources.local.SettingsDataStore
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.local.impl.LocationManagerImpl
import com.eslamdev.weathroza.data.datasources.local.impl.NetworkObserverImpl
import com.eslamdev.weathroza.data.datasources.local.impl.SettingsDataStoreImpl
import com.eslamdev.weathroza.data.datasources.local.impl.WeatherLocalDataSourceImpl
import com.eslamdev.weathroza.data.datasources.local.impl.WorkManagerAlertScheduler
import com.eslamdev.weathroza.data.datasources.local.worker.AlertWorkerFactory
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.datasources.remote.impl.WeatherRemoteDataSourceImpl
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.data.repo.impl.UserSettingsRepoImpl
import com.eslamdev.weathroza.data.repo.impl.WeatherRepoImpl
import com.eslamdev.weathroza.presentaion.alerts.viewmodel.AlertsViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavWeatherDisplayViewModel
import com.eslamdev.weathroza.presentaion.home.viewmodel.HomeViewModel
import com.eslamdev.weathroza.presentaion.map.viewmodel.MapViewModel
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { WeatherDataBase.getInstance(androidContext()) }
    single { get<WeatherDataBase>().getWeatherDao() }
    single { get<WeatherDataBase>().getHourlyForecastDao() }
    single { get<WeatherDataBase>().getDailyForecastDao() }
    single { get<WeatherDataBase>().getFavouriteLocationDao() }
    single { get<WeatherDataBase>().getAlertDao() }
}

val networkModule = module {
    single { RetrofitHelper.weatherService }
}

val dataSourceModule = module {
    single<LocationManager> { LocationManagerImpl(androidContext()) }
    single<NetworkObserver> { NetworkObserverImpl(androidContext()) }
    single<SettingsDataStore> { SettingsDataStoreImpl(androidContext()) }
    single<WeatherLocalDataSource> {
        WeatherLocalDataSourceImpl(
            weatherDao = get(),
            hourlyForecastDao = get(),
            dailyForecastDao = get(),
            favouriteLocationDao = get(),
            alertDao = get(),
        )
    }
    single<WeatherRemoteDataSource> { WeatherRemoteDataSourceImpl(get()) }
    single<AlarmScheduler> { WorkManagerAlertScheduler(androidContext()) }
}

val repositoryModule = module {
    single<WeatherRepo> {
        WeatherRepoImpl(
            localDataSource = get(),
            remoteDataSource = get(),
            alarmScheduler = get(),
        )
    }
    single<UserSettingsRepo> {
        UserSettingsRepoImpl(
            dataStore = get(),
            locationManager = get(),
            networkObserver = get(),
        )
    }
}

val workerModule = module {
    single {
        AlertWorkerFactory(
            weatherRepo = get(),
            settingsRepo = get(),
            alarmScheduler = get(),
        )
    }
}

val viewModelModule = module {
    viewModel { AlertsViewModel(settingsRepo = get(), weatherRepo = get()) }
    viewModel { HomeViewModel(weatherRepo = get(), settingsRepo = get()) }
    viewModel { FavViewModel(repo = get(), settingsRepo = get()) }
    viewModel { FavWeatherDisplayViewModel(repo = get(), settingsRepo = get()) }
    viewModel { SettingsViewModel(settingsRepo = get()) }
    viewModel { AppViewModel(settingsRepo = get()) }
    viewModel { params ->
        MapViewModel(
            repo = get(),
            settingsRepo = get(),
            mode = params.get()
        )
    }
}