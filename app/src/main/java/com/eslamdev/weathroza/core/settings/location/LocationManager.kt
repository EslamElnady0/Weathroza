package com.eslamdev.weathroza.core.settings.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager(context: Context) {

    private val fusedClient = LocationServices
        .getFusedLocationProviderClient(context.applicationContext)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { cont ->
        val cancellationSource = CancellationTokenSource()

        fusedClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationSource.token
        ).addOnSuccessListener { location ->
            if (location != null) cont.resume(location)
            else cont.resumeWithException(Exception("Location unavailable"))
        }.addOnFailureListener { e ->
            cont.resumeWithException(e)
        }

        cont.invokeOnCancellation { cancellationSource.cancel() }
    }
}