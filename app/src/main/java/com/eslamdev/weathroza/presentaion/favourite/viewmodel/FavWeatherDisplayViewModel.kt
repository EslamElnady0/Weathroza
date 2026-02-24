package com.eslamdev.weathroza.presentaion.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.network.ErrorHandler
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
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

class FavWeatherDisplayViewModel(
    private val repo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : ViewModel() {

    private val isConnectedFlow = settingsRepo.isConnected

    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
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

    fun loadData(lat: Double, lng: Double, cityId: Long) {
        viewModelScope.launch {
            repo.getCachedHomeData(cityId)?.let { (weather, hourly, daily) ->
                _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
            }

            val isOnlineNow = isConnectedFlow.first()
            isInitialized = true

            if (isOnlineNow) {
                refreshFromNetwork(lat, lng, cityId)
            } else if (_uiState.value is UiState.Loading) {
                _uiState.value =
                    UiState.Error(R.string.error_no_internet)

            }
            isConnectedFlow
                .drop(1)
                .onEach { isOnline -> if (isOnline) refreshFromNetwork(lat, lng, cityId) }
                .launchIn(viewModelScope)

            settings
                .map { it.language }
                .distinctUntilChanged()
                .drop(1)
                .onEach { refreshFromNetwork(lat, lng, cityId) }
                .launchIn(viewModelScope)
        }
    }

    fun refreshFromNetwork(
        lat: Double,
        lng: Double,
        cityId: Long,
    ) {
        if (!isInitialized) return
        val currentSettings = settings.value

        repo.refreshHomeData(lat, lng, currentSettings.language)
            .onStart { _isRefreshing.value = true }
            .onEach { result ->
                result.fold(
                    onSuccess = { (weather, hourly, daily) ->
                        _uiState.value = UiState.Success(HomeViewData(weather, hourly, daily))
                        // update the record to be viewed at the fav list with updated icon and temp
                        repo.refreshFavouriteWeather(
                            cityId = cityId,
                            temp = weather.temp,
                            iconUrl = weather.iconUrl
                        )
                    },
                    onFailure = { e ->
                        if (_uiState.value !is UiState.Success)
                            _uiState.value =
                                UiState.Error(ErrorHandler.handleException(e as Exception))
                    }
                )
            }
            .onCompletion { _isRefreshing.value = false }
            .launchIn(viewModelScope)
    }

    fun refresh(
        lat: Double, lng: Double, cityId: Long,
    ) {
        refreshFromNetwork(lat, lng, cityId)
    }
}

class FavWeatherDisplayViewModelFactory(
    private val repo: WeatherRepo,
    private val settingsRepo: UserSettingsRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavWeatherDisplayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavWeatherDisplayViewModel(repo, settingsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
