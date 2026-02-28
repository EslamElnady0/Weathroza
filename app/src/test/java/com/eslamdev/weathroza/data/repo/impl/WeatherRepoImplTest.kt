package com.eslamdev.weathroza.data.repo.impl

import com.eslamdev.weathroza.data.datasources.local.AlarmScheduler
import com.eslamdev.weathroza.data.datasources.local.WeatherLocalDataSource
import com.eslamdev.weathroza.data.datasources.remote.WeatherRemoteDataSource
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.forecast.DailyForecastEntity
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class WeatherRepoImplTest {

    private lateinit var repo: WeatherRepoImpl
    private val localDataSource: WeatherLocalDataSource = mockk()
    private val remoteDataSource: WeatherRemoteDataSource = mockk()
    private val alarmScheduler: AlarmScheduler = mockk()

    @Before
    fun setUp() {
        repo = WeatherRepoImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            alarmScheduler = alarmScheduler,
        )
    }
    // ── RefreshHomeData ─────────────────────────────────────────

    @Test
    fun refreshHomeData_allSuccessful_emitsSuccessAndSavesToLocal() = runTest {
        // Given all three api calls succeed
        val weather = WeatherEntity(id = 1, name = "Cairo")
        val hourly = listOf(HourlyForecastEntity(dt = 1L, temp = 25.0, icon = "", cityId = "1"))
        val daily = listOf(
            DailyForecastEntity(
                cityId = 1L,
                dt = 1L,
                tempDay = 25.0,
                feelsLikeDay = 24.0,
                weatherMain = "",
                weatherDescription = "",
                weatherIcon = ""
            )
        )

        every {
            remoteDataSource.getWeather(
                any(),
                any(),
                any(),
                any()
            )
        } returns flowOf(Result.success(weather))
        every { remoteDataSource.getHourlyForecast(any(), any(), any(), any()) } returns flowOf(
            Result.success(hourly)
        )
        every { remoteDataSource.getDailyForecast(any(), any(), any(), any()) } returns flowOf(
            Result.success(daily)
        )

        coEvery { localDataSource.insertWeather(weather) } just runs
        coEvery { localDataSource.insertHourlyForecasts(hourly) } just runs
        coEvery { localDataSource.insertDailyForecasts(daily) } just runs

        // When collecting the flow
        val result =
            repo.refreshHomeData(30.0, 31.0, AppLanguage.ENGLISH).first()

        // Then emits success with correct data and saves to local
        assertThat(result.isSuccess, `is`(true))
        assertThat(result.getOrNull()?.first?.name, `is`("Cairo"))
        assertThat(result.getOrNull()?.second?.size, `is`(1))
        assertThat(result.getOrNull()?.third?.size, `is`(1))
        coVerify(exactly = 1) { localDataSource.insertWeather(weather) }
        coVerify(exactly = 1) { localDataSource.insertHourlyForecasts(hourly) }
        coVerify(exactly = 1) { localDataSource.insertDailyForecasts(daily) }
    }

    @Test
    fun refreshHomeData_weatherFails_emitsFailureAndDoesNotSaveToLocal() = runTest {
        // Given weather api call fails
        every {
            remoteDataSource.getWeather(
                any(),
                any(),
                any(),
                any()
            )
        } returns flowOf(Result.failure(Exception("network error")))
        every { remoteDataSource.getHourlyForecast(any(), any(), any(), any()) } returns flowOf(
            Result.failure(Exception("network error"))
        )
        every { remoteDataSource.getDailyForecast(any(), any(), any(), any()) } returns flowOf(
            Result.failure(Exception("network error"))
        )

        // When collecting the flow
        val result =
            repo.refreshHomeData(30.0, 31.0, AppLanguage.ENGLISH).first()

        // Then emits failure and nothing is saved to local
        assertThat(result.isFailure, `is`(true))
        coVerify(exactly = 0) { localDataSource.insertWeather(any()) }
        coVerify(exactly = 0) { localDataSource.insertHourlyForecasts(any()) }
        coVerify(exactly = 0) { localDataSource.insertDailyForecasts(any()) }
    }
    // ── getCachedHomeData ─────────────────────────────────────────

    @Test
    fun getCachedHomeData_nullCityId_returnsNull() = runTest {
        // Given a null city id

        // When getting cached home data
        val result = repo.getCachedHomeData(null)

        // Then returns null without touching the data source
        assertThat(result, `is`(nullValue()))
        coVerify(exactly = 0) { localDataSource.getWeatherByCityId(any()) }
        coVerify(exactly = 0) { localDataSource.getHourlyForecastsByCityId(any()) }
        coVerify(exactly = 0) { localDataSource.getDailyForecastsByCityId(any()) }
    }

    @Test
    fun getCachedHomeData_noWeatherInCache_returnsNull() = runTest {
        // Given no weather cached for this city
        coEvery { localDataSource.getWeatherByCityId(1L) } returns null

        // When getting cached home data
        val result = repo.getCachedHomeData(1L)

        // Then returns null without fetching forecasts
        assertThat(result, `is`(nullValue()))
        coVerify(exactly = 0) { localDataSource.getHourlyForecastsByCityId(any()) }
        coVerify(exactly = 0) { localDataSource.getDailyForecastsByCityId(any()) }
    }

    // ── insertAlert ───────────────────────────────────────────────

    @Test
    fun insertAlert_timeBased_schedulesAlert() = runTest {
        // Given a time based alert
        val alert = AlertEntity(
            id = 1L,
            name = "test",
            parameter = WeatherParameter.TEMP,
            threshold = 30f,
            isAbove = true,
            frequency = AlertFrequency.TIME_BASED,
        )
        coEvery { localDataSource.insertAlert(alert) } returns 1L
        coEvery { alarmScheduler.scheduleAlert(any()) } just runs

        // When inserting the alert
        repo.insertAlert(alert)

        // Then scheduler is called once with the alert
        coVerify(exactly = 1) { alarmScheduler.scheduleAlert(any()) }
    }

    @Test
    fun insertAlert_continuous_doesNotScheduleAlert() = runTest {
        // Given a continuous alert
        val alert = AlertEntity(
            id = 1L,
            name = "test",
            parameter = WeatherParameter.TEMP,
            threshold = 30f,
            isAbove = true,
            frequency = AlertFrequency.CONTINUOUS,
        )
        coEvery { localDataSource.insertAlert(alert) } returns 1L

        // When inserting the alert
        repo.insertAlert(alert)

        // Then scheduler is never called
        coVerify(exactly = 0) { alarmScheduler.scheduleAlert(any()) }
    }

    // ── toggleAlert ───────────────────────────────────────────────

    @Test
    fun toggleAlert_disablingTimeBasedAlert_cancelsScheduledAlert() = runTest {
        // Given an enabled time based alert
        val alert = AlertEntity(
            id = 1L,
            name = "test",
            parameter = WeatherParameter.TEMP,
            threshold = 30f,
            isAbove = true,
            frequency = AlertFrequency.TIME_BASED,
            isEnabled = true,
        )
        coEvery { localDataSource.updateEnabled(1L, false) } just runs
        every { localDataSource.getAlertById(1L) } returns flowOf(alert)
        coEvery { alarmScheduler.cancelAlert(1L) } just runs

        // When disabling the alert
        repo.toggleAlert(1L, false)

        // Then the scheduled alert is cancelled
        coVerify(exactly = 1) { alarmScheduler.cancelAlert(1L) }
    }
}