package com.eslamdev.weathroza.presentaion.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.settings.SettingsDataStore
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.core.network.ErrorHandler
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MapViewModel(
    private val repo: WeatherRepo,
    private val context: Context
) : ViewModel() {

    //Settings State --------------------------------------------------------------

    private val dataStore = SettingsDataStore(context)

    val settings: StateFlow<UserSettings> = dataStore.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )
    //Weather State --------------------------------------------------------------

    private val _weatherState = MutableStateFlow<UiState<WeatherEntity>>(UiState.Idle)
    val weatherState: StateFlow<UiState<WeatherEntity>> = _weatherState.asStateFlow()

    fun loadWeather(latLng: LatLng) {
        viewModelScope.launch {
            _weatherState.value = UiState.Loading
            try {
                val weather = repo.fetchWeatherFromApi(
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    language = settings.value.language
                )
                _weatherState.value = UiState.Success(weather)
            } catch (e: Exception) {
                _weatherState.value = UiState.Error(ErrorHandler.handleException(e, context = context))
            }
        }
    }

    fun resetWeather() {
        _weatherState.value = UiState.Idle
    }
    //Search Response --------------------------------------------------------------

    private val _citiesState = MutableStateFlow<UiState<List<CityEntity>>>(UiState.Idle)
    val citiesState: StateFlow<UiState<List<CityEntity>>> = _citiesState.asStateFlow()

    fun getPossibleCities(cityName: String) {
        if (cityName.isBlank()) return
        viewModelScope.launch {
            _citiesState.value = UiState.Loading
            try {
                val cities = repo.getPossibleCities(cityName)
                _citiesState.value = UiState.Success(cities)
            } catch (e: Exception) {
                _citiesState.value = UiState.Error(ErrorHandler.handleException(e, context))
            }
        }
    }

    fun resetCitiesState() {
        _citiesState.value = UiState.Idle
    }

    //Search Action --------------------------------------------------------------
    private val _searchQuery = MutableStateFlow("")

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length < 2) resetCitiesState()
    }

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(400L)
                .filter { it.length >= 2 }
                .collectLatest { getPossibleCities(it) }
        }
    }
}

class MapViewModelFactory(
    private val repo: WeatherRepo,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repo, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
