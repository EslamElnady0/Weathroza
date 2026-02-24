package com.eslamdev.weathroza.core.common

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val messageRes: Int? = null, val message: String? = null) : UiState<Nothing>()
}