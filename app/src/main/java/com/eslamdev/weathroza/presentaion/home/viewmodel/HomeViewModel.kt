package com.eslamdev.weathroza.presentaion.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.network.NetworkObserver
import com.eslamdev.weathroza.core.settings.LocationType
import com.eslamdev.weathroza.core.settings.SettingsDataStore
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.core.settings.location.LocationManager
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: WeatherRepo,
    private val context: Context
) : ViewModel() {

    private val dataStore = SettingsDataStore(context)
    private val networkObserver = NetworkObserver(context)

    val settings: StateFlow<UserSettings> = dataStore.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )

    private val _uiState = MutableStateFlow<UiState<HomeViewData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeViewData>> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var isInitialized = false

    init {
        loadHomeData()
        observeLocationChanges()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            // 1. Show cache immediately while we wait for network
            val realSettings = dataStore.settingsFlow.first()
            realSettings.cityId?.let { cityId ->
                repo.getCachedHomeData(cityId)?.let { (weather, hourly, daily) ->
                    _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
                }
            }

            val isOnlineNow = networkObserver.isConnected.first()
            isInitialized = true

            if (isOnlineNow) refreshFromNetwork()

            // 2. Re-fetch whenever connection is restored
            networkObserver.isConnected
                .drop(1)
                .onEach { isOnline -> if (isOnline) refreshFromNetwork() }
                .launchIn(viewModelScope)

            // 3. Re-fetch whenever language changes
            settings
                .map { it.language }
                .distinctUntilChanged()
                .drop(1)
                .onEach { refreshFromNetwork() }
                .launchIn(viewModelScope)
        }
    }

    private fun observeLocationChanges() {
        var previousCityId: Long? = null

        settings
            .map { it.userLat to it.userLng }
            .distinctUntilChanged()
            .drop(1)
            .onEach { (lat, lng) ->
                if (lat != null && lng != null && isInitialized) {
                    val oldCityId = previousCityId
                    previousCityId = settings.value.cityId
                    onLocationChanged(lat, lng, oldCityId)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onLocationChanged(lat: Double, lng: Double, oldCityId: Long?) {
        viewModelScope.launch {
            if (oldCityId != null && oldCityId != 0L)
                repo.clearHomeData(oldCityId)
        }

        _uiState.value = UiState.Loading

        repo.refreshHomeData(lat, lng, settings.value.language)
            .onStart { _isRefreshing.value = true }
            .onEach { result ->
                result.fold(
                    onSuccess = { (weather, hourly, daily) ->
                        if (settings.value.locationType == LocationType.GPS) {
                            dataStore.saveGpsLocation(lat, lng)
                            dataStore.saveCityId(weather.id.toLong())
                        }
                        _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
                    },
                    onFailure = { e ->
                        _uiState.value = UiState.Error(e.message ?: "Unknown error")
                    }
                )
            }
            .onCompletion { _isRefreshing.value = false }
            .launchIn(viewModelScope)
    }

    private fun refreshFromNetwork() {
        if (!isInitialized) return

        val currentSettings = settings.value

        if (currentSettings.locationType == LocationType.NONE) {
            if (_uiState.value !is UiState.Success)
                _uiState.value = UiState.Error("No location set")
            return
        }

        val lat = currentSettings.userLat ?: return
        val lng = currentSettings.userLng ?: return

        repo.refreshHomeData(lat, lng, currentSettings.language)
            .onStart { _isRefreshing.value = true }
            .onEach { result ->
                result.fold(
                    onSuccess = { (weather, hourly, daily) ->
                        if (settings.value.locationType == LocationType.GPS)
                            dataStore.saveCityId(weather.id.toLong())

                        _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
                    },
                    onFailure = { e ->
                        if (_uiState.value !is UiState.Success)
                            _uiState.value = UiState.Error(e.message ?: "Unknown error")
                    }
                )
            }
            .onCompletion { _isRefreshing.value = false }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        refreshFromNetwork()
    }

    fun fetchAndSaveGpsLocation() {
        viewModelScope.launch {
            runCatching {
                val location = LocationManager(context).getCurrentLocation()
                dataStore.saveGpsLocation(
                    lat = location.latitude,
                    lng = location.longitude
                )
            }.onFailure { e ->
                Log.e("HomeVM", "fetchAndSaveGpsLocation: failed → ${e.message}")
            }
        }
    }
}

class HomeViewModelFactory(
    private val repo: WeatherRepo,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repo, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}