package com.eslamdev.weathroza.presentaion.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.settings.SettingsDataStore
import com.eslamdev.weathroza.core.settings.UserSettings
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavViewModel(private val repo: WeatherRepo, private val context: Context) : ViewModel() {

    private val dataStore = SettingsDataStore(context)
    val settings: StateFlow<UserSettings> = dataStore.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )

    val favourites: StateFlow<UiState<List<FavouriteLocationEntity>>> = repo.getAllFavourites()
        .map { result ->
            result.fold(
                onSuccess = { locations -> UiState.Success(locations) },
                onFailure = { error -> UiState.Error(error.message ?: "Unknown error") }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun removeFavourite(cityId: Long) {
        viewModelScope.launch {
            repo.removeFavourite(cityId)
        }
    }
}

class FavViewModelFactory(
    private val repo: WeatherRepo,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavViewModel(repo, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
