package com.eslamdev.weathroza.presentaion.home.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.enums.Units
import com.eslamdev.weathroza.core.network.NetworkObserver
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.core.settings.SettingsDataStore
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.home.model.HomeViewData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val latitude = 30.599405
    private val longitude = 31.489460

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            val cached = repo.getCachedHomeData()
            if (cached != null) {
                val (weather, hourly, daily) = cached
                _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
            } else {
                _uiState.value = UiState.Loading
            }

            networkObserver.isConnected
                .onEach { isOnline ->
                    if (isOnline) {
                        refreshFromNetwork()
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private suspend fun refreshFromNetwork() {
        _isRefreshing.value = true
        try {
            val currentSettings = settings.value
            val (weather, hourly, daily) = repo.refreshHomeData(
                latitude = latitude,
                longitude = longitude,
                language = currentSettings.language,
            )
            _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
        } catch (e: Exception) {
            if (_uiState.value is UiState.Loading) {
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