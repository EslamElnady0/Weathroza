package com.eslamdev.weathroza.presentaion.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.enums.AppLanguage
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: WeatherRepo
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<HomeViewData>>(UiState.Idle)

    val uiState: StateFlow<UiState<HomeViewData>> =
        _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value =
            UiState.Error(throwable.message ?: "Unknown error")
    }

    fun loadWeather(
        latitude: Double,
        longitude: Double,
        language: AppLanguage = AppLanguage.ENGLISH,
        units: Units = Units.METRIC
    ) {
        viewModelScope.launch(exceptionHandler) {

            _uiState.value = UiState.Loading

            val weather = repo.fetchWeatherFromApi(
                latitude,
                longitude,
                language,
                units
            )

            val hourly = repo.getHourlyForecastFromApi(
                latitude,
                longitude,
                language,
                units
            )

            val daily = repo.getDailyForecastFromApi(
                latitude,
                longitude,
                language,
                units
            )

            val homeData = HomeViewData(
                weather = weather,
                hourlyForecast = hourly,
                dailyForecast = daily
            )

            _uiState.value = UiState.Success(homeData)
        }
    }

    init {
        loadWeather(


                latitude = 67.452792,
            longitude = 29.394130,
            language = AppLanguage.ENGLISH,
            units = Units.METRIC
        )
    }
}


class HomeViewModelFactory(
    private val repo: WeatherRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}