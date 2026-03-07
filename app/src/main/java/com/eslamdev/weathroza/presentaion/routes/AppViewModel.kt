package com.eslamdev.weathroza.presentaion.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    private val settingsRepo: UserSettingsRepo
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = settingsRepo.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = true
        )
}