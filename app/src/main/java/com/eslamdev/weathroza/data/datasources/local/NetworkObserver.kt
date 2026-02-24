package com.eslamdev.weathroza.data.datasources.local

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    val isConnected: Flow<Boolean>
}