package com.eslamdev.weathroza.core.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.withPrevious(): Flow<Pair<T?, T>> = flow {
    var previous: T? = null
    collect { current ->
        emit(Pair(previous, current))
        previous = current
    }
}

fun <T> (suspend () -> T).asResultFlow(): Flow<Result<T>> =
    asFlow()
        .map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
        .flowOn(Dispatchers.IO)