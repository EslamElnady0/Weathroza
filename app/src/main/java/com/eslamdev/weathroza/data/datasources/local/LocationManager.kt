package com.eslamdev.weathroza.data.datasources.local

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {
    fun getLocationFlow(): Flow<Location>
}