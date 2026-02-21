package com.eslamdev.weathroza.presentaion.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.withPrevious
import com.eslamdev.weathroza.core.network.NetworkObserver
import com.eslamdev.weathroza.core.settings.LocationType
import com.eslamdev.weathroza.core.settings.SettingsDataStore
import com.eslamdev.weathroza.core.settings.UserSettings
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
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

    init {
        loadHomeData()
        observeLocationChanges()
    }

    private var isInitialized = false

    private fun loadHomeData() {
        viewModelScope.launch {
            val realSettings = dataStore.settingsFlow.first()
            realSettings.cityId?.let { cityId ->
                val cached = repo.getCachedHomeData(cityId)
                if (cached != null) {
                    val (weather, hourly, daily) = cached
                    _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
                }
            }

            val isOnlineNow = networkObserver.isConnected.first()
            isInitialized = true
            if (isOnlineNow) {
                refreshFromNetwork()
            }

            networkObserver.isConnected
                .drop(1)
                .onEach { isOnline ->
                    if (isOnline) refreshFromNetwork()
                }
                .launchIn(viewModelScope)

            settings
                .map { it.language }
                .distinctUntilChanged()
                .drop(1)
                .onEach { language ->
                    refreshFromNetwork()
                }
                .launchIn(viewModelScope)
        }
    }

    private fun observeLocationChanges() {
        settings
            .map { Triple(it.userLat, it.userLng, it.cityId) }
            .distinctUntilChanged()
            .drop(1)
            .withPrevious()
            .onEach { (old, new) ->
                val (newLat, newLng, newCityId) = new

                if (newLat != null && newLng != null && newCityId != null && isInitialized) {
                    if (old?.third != null && old.third != newCityId) {
                        repo.clearHomeData(old.third!!)
                    }
                    onLocationChanged(newLat, newLng)
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun onLocationChanged(lat: Double, lng: Double) {
        _uiState.value = UiState.Loading
        _isRefreshing.value = true
        try {
            val (weather, hourly, daily) = repo.refreshHomeData(
                latitude = lat,
                longitude = lng,
                language = settings.value.language,
            )
            _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
        } finally {
            _isRefreshing.value = false
        }
    }

    private suspend fun refreshFromNetwork() {
        val currentSettings = settings.value

        if (!isInitialized) {
            return
        }

        if (currentSettings.locationType == LocationType.NONE) {
            if (_uiState.value !is UiState.Success) {
                _uiState.value = UiState.Error("No location set")
            }
            return
        }

        val lat = currentSettings.userLat
        val lng = currentSettings.userLng

        if (lat == null || lng == null) return


        _isRefreshing.value = true
        try {
            val (weather, hourly, daily) = repo.refreshHomeData(
                latitude = lat,
                longitude = lng,
                language = currentSettings.language,
            )
            _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
        } catch (e: Exception) {
            if (_uiState.value !is UiState.Success) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        } finally {
            _isRefreshing.value = false
        }
    }

    fun refresh() {
        viewModelScope.launch { refreshFromNetwork() }
    }
}
private data class Location(val lat: Double, val lng: Double, val cityId: Long)

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