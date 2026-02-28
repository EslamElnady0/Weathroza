package com.eslamdev.weathroza.homewidget

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class WeatherWidgetState(
    val cityName: String = "",
    val country: String = "",
    val temp: Double = 0.0,
    val weatherDescription: String = "",
    val iconUrl: String = "",
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val temperatureUnit: String = "CELSIUS",
    val windSpeedUnit: String = "MS",
    val showHourly: Boolean = true,
    val hourlyForecasts: List<HourlyWidgetItem> = emptyList(),
    val dailyForecasts: List<DailyWidgetItem> = emptyList(),
    val lastUpdated: Long = 0L,
    val isLoading: Boolean = false,
)

@Serializable
data class HourlyWidgetItem(
    val dt: Long,
    val temp: Double,
    val iconUrl: String,
)

@Serializable
data class DailyWidgetItem(
    val dt: Long,
    val tempDay: Double,
    val iconUrl: String,
    val weatherDescription: String,
)

object WeatherWidgetStateDefinition : GlanceStateDefinition<WeatherWidgetState> {

    private const val FILE_NAME = "weather_widget_state"

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<WeatherWidgetState> {
        return androidx.datastore.core.DataStoreFactory.create(
            serializer = WeatherWidgetStateSerializer,
            produceFile = { context.dataFile(fileKey) }
        )
    }

    override fun getLocation(context: Context, fileKey: String): File =
        context.dataFile(fileKey)

    private fun Context.dataFile(fileKey: String): File =
        File(filesDir, "glance/$FILE_NAME/$fileKey")
}

object WeatherWidgetStateSerializer : Serializer<WeatherWidgetState> {
    override val defaultValue = WeatherWidgetState()

    override suspend fun readFrom(input: InputStream): WeatherWidgetState = try {
        Json.decodeFromString(WeatherWidgetState.serializer(), input.readBytes().decodeToString())
    } catch (e: SerializationException) {
        throw CorruptionException("Failed to read widget state", e)
    }

    override suspend fun writeTo(t: WeatherWidgetState, output: OutputStream) {
        output.write(Json.encodeToString(WeatherWidgetState.serializer(), t).encodeToByteArray())
    }
}