package com.eslamdev.weathroza.core.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

    fun <T> Flow<T>.withPrevious(): Flow<Pair<T?, T>> = flow {
        var previous: T? = null
        collect { current ->
            emit(Pair(previous, current))
            previous = current
        }
    }