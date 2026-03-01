package com.eslamdev.weathroza.presentaion.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.R
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.core.helpers.SnackbarController
import com.eslamdev.weathroza.core.helpers.SnackbarEvent
import com.eslamdev.weathroza.core.helpers.SnackbarMessage
import com.eslamdev.weathroza.core.helpers.SnackbarType
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavViewModel(private val repo: WeatherRepo, private val settingsRepo: UserSettingsRepo) :
    ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepo.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )

    val favourites: StateFlow<UiState<List<FavouriteLocationEntity>>> = repo.getAllFavourites()
        .map { result ->
            result.fold(
                onSuccess = { locations -> UiState.Success(locations) },
                onFailure = { error -> UiState.Error(message = error.message ?: "Unknown error") }
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
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = SnackbarMessage.ResourceMessage(
                    R.string.favourite_removed_successfully,
                ),
                type = SnackbarType.ERROR
            )
        )
    }
}
