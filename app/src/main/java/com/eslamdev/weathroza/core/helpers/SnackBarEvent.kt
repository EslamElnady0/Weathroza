package com.eslamdev.weathroza.core.helpers

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackbarController {

    private val _events = MutableSharedFlow<SnackbarEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun sendEvent(event: SnackbarEvent) {
        _events.tryEmit(event)
    }
}

enum class SnackbarType { SUCCESS, ERROR }

sealed class SnackbarMessage {
    data class StringMessage(val message: String) : SnackbarMessage()
    data class ResourceMessage(@StringRes val resId: Int) : SnackbarMessage()
}

data class SnackbarEvent(
    val message: SnackbarMessage,
    val actionLabel: SnackbarMessage? = null,
    val type: SnackbarType = SnackbarType.SUCCESS
)

@Composable
fun SnackbarMessage.resolve(): String = when (this) {
    is SnackbarMessage.StringMessage -> message
    is SnackbarMessage.ResourceMessage -> stringResource(resId)
}