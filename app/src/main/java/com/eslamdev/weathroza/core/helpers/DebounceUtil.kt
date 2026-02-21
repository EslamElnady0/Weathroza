package com.eslamdev.weathroza.core.helpers

import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun <T> rememberDebounced(
    value: T,
    delayMillis: Long = 400L
): State<T> {
    val debouncedValue = remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        delay(delayMillis)
        debouncedValue.value = value
    }

    return debouncedValue
}