package com.eslamdev.weathroza.data.datasources.local

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationManager(context: Context) {

    private val fusedClient = LocationServices
        .getFusedLocationProviderClient(context.applicationContext)

    @SuppressLint("MissingPermission")
    fun getLocationFlow(): Flow<Location> = callbackFlow {
        val cancellationSource = CancellationTokenSource()

        fusedClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                trySend(location)
                close()
            } else {
                close(Exception("Location unavailable"))
            }
        }.addOnFailureListener { e ->
            close(e)
        }

        awaitClose { cancellationSource.cancel() }
    }
}