package com.eslamdev.weathroza.data.datasources.local.impl

import com.eslamdev.weathroza.data.datasources.local.dao.AlertDao
import com.eslamdev.weathroza.data.datasources.local.dao.DailyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.FavouriteLocationDao
import com.eslamdev.weathroza.data.datasources.local.dao.HourlyForecastDao
import com.eslamdev.weathroza.data.datasources.local.dao.WeatherDao
import com.eslamdev.weathroza.data.models.alert.AlertEntity
import com.eslamdev.weathroza.data.models.alert.AlertFrequency
import com.eslamdev.weathroza.data.models.alert.WeatherParameter
import com.eslamdev.weathroza.data.models.forecast.HourlyForecastEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceImplTest {
    private lateinit var weatherLocalDataSource: WeatherLocalDataSourceImpl
    private lateinit var alertDao: AlertDao
    private lateinit var favouriteLocationDao: FavouriteLocationDao
    private lateinit var weatherDao: WeatherDao
    private lateinit var hourlyForecastDao: HourlyForecastDao
    private lateinit var dailyForecastDao: DailyForecastDao

    @Before
    fun setUp() {
        alertDao = mockk()
        favouriteLocationDao = mockk()
        weatherDao = mockk()
        hourlyForecastDao = mockk()
        dailyForecastDao = mockk()

        weatherLocalDataSource = WeatherLocalDataSourceImpl(
            alertDao = alertDao,
            favouriteLocationDao = favouriteLocationDao,
            weatherDao = weatherDao,
            hourlyForecastDao = hourlyForecastDao,
            dailyForecastDao = dailyForecastDao
        )
    }

    // ── Hourly Forecast ──────────────────────────────────────────

    @Test
    fun insertHourlyForecasts_callsDaoWithCorrectData() = runTest {
        // Given a list of hourly forecasts
        val forecasts = listOf(
            HourlyForecastEntity(dt = 1L, temp = 25.0, icon = "", cityId = "1"),
            HourlyForecastEntity(dt = 2L, temp = 26.0, icon = "", cityId = "1"),
        )
        coEvery { hourlyForecastDao.insertHourlyForecasts(forecasts) } just runs

        // When inserting hourly forecasts
        weatherLocalDataSource.insertHourlyForecasts(forecasts)

        // Then dao insert is called once with the correct data
        coVerify(exactly = 1) { hourlyForecastDao.insertHourlyForecasts(forecasts) }
    }

    @Test
    fun getHourlyForecastsByCityId_returnsForecastsForCity() = runTest {
        // Given a mocked dao response
        val forecasts = listOf(
            HourlyForecastEntity(dt = 1L, temp = 25.0, icon = "", cityId = "1"),
            HourlyForecastEntity(dt = 1L, temp = 25.0, icon = "", cityId = "2"),
        )
        coEvery { hourlyForecastDao.getHourlyForecastsByCityId(1L) } returns forecasts

        // When getting hourly forecasts by city id
        val result = weatherLocalDataSource.getHourlyForecastsByCityId(1L)

        // Then getHourlyForecastsByCityId runs once returns the correct forecasts
        coVerify(exactly = 1) { hourlyForecastDao.getHourlyForecastsByCityId(1L) }

        assertThat(result.size, `is`(2))
        assertThat(result[0].cityId, `is`("1"))
        assertThat(result[1].cityId, `is`("2"))
    }

    @Test
    fun deleteHourlyForecastsByCityId_callsDaoWithCorrectId() = runTest {
        // Given a city id
        coEvery { hourlyForecastDao.deleteHourlyForecastsByCityId(1L) } just runs

        // When deleting hourly forecasts by city id
        weatherLocalDataSource.deleteHourlyForecastsByCityId(1L)

        // Then dao delete is called once with the correct id
        coVerify(exactly = 1) { hourlyForecastDao.deleteHourlyForecastsByCityId(1L) }
    }

    // ── Alerts ───────────────────────────────────────────────────

    @Test
    fun insertAlert_returnsDaoInsertedId() = runTest {
        // Given an alert entity
        val alert = AlertEntity(
            name = "test alert",
            parameter = WeatherParameter.TEMP,
            threshold = 30f,
            isAbove = true,
            frequency = AlertFrequency.TIME_BASED,
        )
        coEvery { alertDao.insertAlert(alert) } returns 1L

        // When inserting the alert
        val result = weatherLocalDataSource.insertAlert(alert)

        // Then the insertion runs once and returns the inserted id
        coVerify(exactly = 1) { alertDao.insertAlert(alert) }
        assertThat(result, `is`(1L))
    }

    @Test
    fun getAllAlerts_returnsFlowOfAlerts() = runTest {
        // Given a mocked dao flow response
        val alerts = listOf(
            AlertEntity(
                id = 1L,
                name = "test alert",
                parameter = WeatherParameter.TEMP,
                threshold = 30f,
                isAbove = true,
                frequency = AlertFrequency.TIME_BASED,
            )
        )
        every { alertDao.getAllAlerts() } returns flowOf(alerts)

        // When getting all alerts
        val result = weatherLocalDataSource.getAllAlerts().first()

        // Then returns the correct alerts
        assertThat(result.size, `is`(1))
        assertThat(result[0].name, `is`("test alert"))
    }

    @Test
    fun updateEnabled_callsDaoWithCorrectParams() = runTest {
        // Given an alert id and enabled state
        coEvery { alertDao.updateEnabled(1L, false) } just runs

        // When updating enabled state
        weatherLocalDataSource.updateEnabled(1L, false)

        // Then dao update is called once with the correct params
        coVerify(exactly = 1) { alertDao.updateEnabled(1L, false) }
    }

    @Test
    fun deleteAlert_callsDaoWithCorrectId() = runTest {
        // Given an alert id
        coEvery { alertDao.deleteAlert(1L) } just runs

        // When deleting the alert
        weatherLocalDataSource.deleteAlert(1L)

        // Then dao delete is called once with the correct id
        coVerify(exactly = 1) { alertDao.deleteAlert(1L) }
    }

    @Test
    fun getAlertById_returnsFlowOfAlert() = runTest {
        // Given a mocked dao flow response
        val alert = AlertEntity(
            id = 1L,
            name = "test alert",
            parameter = WeatherParameter.TEMP,
            threshold = 30f,
            isAbove = true,
            frequency = AlertFrequency.TIME_BASED,
        )
        every { alertDao.getAlertById(1L) } returns flowOf(alert)

        // When getting alert by id
        val result = weatherLocalDataSource.getAlertById(1L).first()

        // Then returns the correct alert
        assertNotNull(result)
        assertThat(result!!.id, `is`(1L))
        assertThat(result.name, `is`("test alert"))
    }
}