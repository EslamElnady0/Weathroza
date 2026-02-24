package com.eslamdev.weathroza.presentaion.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.enums.MapMode
import com.eslamdev.weathroza.core.network.ErrorHandler
import com.eslamdev.weathroza.data.models.geocoding.CityEntity
import com.eslamdev.weathroza.data.models.usersettings.LocationType
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MapViewModel(
    private val repo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
    val mode: MapMode = MapMode.SELECT_LOCATION
) : ViewModel() {


    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )

    // ── Weather ──────────────────────────────────────────────────

    private val _weatherState = MutableStateFlow<UiState<WeatherEntity>>(UiState.Idle)
    val weatherState: StateFlow<UiState<WeatherEntity>> = _weatherState.asStateFlow()

    fun loadWeather(latLng: LatLng) {
        repo.getWeatherFromApi(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            language = settings.value.language
        )
            .onStart { _weatherState.value = UiState.Loading }
            .onEach { result ->
                result.fold(
                    onSuccess = { weather ->
                        _weatherState.value = UiState.Success(weather)
                    },
                    onFailure = { e ->
                        _weatherState.value = UiState.Error(
                            ErrorHandler.handleException(e as Exception)
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    fun resetWeather() {
        _weatherState.value = UiState.Idle
    }

    // ── Location ─────────────────────────────────────────────────

    fun confirmLocation(latLng: LatLng, cityId: Long, postOperationCallBack: () -> Unit) {
        viewModelScope.launch {
            when (mode) {
                MapMode.SELECT_LOCATION -> {
                    settingsRepo.saveManualLocation(latLng.latitude, latLng.longitude, cityId)
                    settingsRepo.saveLocationType(LocationType.MANUAL)
                    postOperationCallBack()
                }

                MapMode.ADD_FAVOURITE -> {
                    val weatherState = _weatherState.value
                    if (weatherState is UiState.Success) {
                        repo.getCityNamesLocalized(latLng.latitude, latLng.longitude)
                            .onStart { _weatherState.value = UiState.Loading }
                            .onEach { result ->
                                result.fold(
                                    onSuccess = { cities ->
                                        if (cities.isNotEmpty()) {
                                            repo.addFavourite(
                                                weatherEntity = weatherState.data,
                                                latLng = latLng,
                                                cityEntity = cities[0]
                                            )
                                            postOperationCallBack()
                                        }
                                    }, onFailure = { t ->
                                        _weatherState.value = UiState.Error(
                                            ErrorHandler.handleException(
                                                t as Exception,
                                            )
                                        )
                                    }
                                )
                            }.launchIn(viewModelScope)
                    }
                }
            }

        }
    }
    // ── Cities search ─────────────────────────────────────────────

    private val _citiesState = MutableStateFlow<UiState<List<CityEntity>>>(UiState.Idle)
    val citiesState: StateFlow<UiState<List<CityEntity>>> = _citiesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length < 2) resetCitiesState()
    }

    fun searchCities(query: String) {
        if (query.isBlank()) return
        repo.getPossibleCities(query)
            .onStart { _citiesState.value = UiState.Loading }
            .onEach { result ->
                result.fold(
                    onSuccess = { cities ->
                        _citiesState.value = UiState.Success(cities)
                    },
                    onFailure = { e ->
                        _citiesState.value = UiState.Error(
                            ErrorHandler.handleException(e as Exception)
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }

    fun resetCitiesState() {
        _citiesState.value = UiState.Idle
    }

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(400L)
                .filter { it.length >= 2 }
                .collectLatest { query -> searchCities(query) }
        }
    }
}

class MapViewModelFactory(
    private val repo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
    private val mode: MapMode = MapMode.SELECT_LOCATION
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repo, settingsRepo, mode) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
