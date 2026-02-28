package com.eslamdev.weathroza.data.datasources.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.models.weather.WeatherEntity
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class WeatherDaoTest {
    private lateinit var database: WeatherDataBase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        weatherDao = database.getWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetWeather_returnsWeatherEntity() = runTest {
        // Given a weather entity
        val weather = WeatherEntity(id = 1, name = "Cairo")

        // When inserting it into the database
        weatherDao.insertWeather(weather)

        // Then getting it by city id returns the same entity
        val result = weatherDao.getWeatherByCityId(1)
        assertNotNull(result)
        assertThat(result!!.id, `is`(1))
        assertThat(result.name, `is`("Cairo"))
    }

    @Test
    fun insertMultipleWeather_getAll_returnsAllEntities() = runTest {
        // Given three weather entities
        weatherDao.insertWeather(WeatherEntity(id = 1, name = "Cairo"))
        weatherDao.insertWeather(WeatherEntity(id = 2, name = "Alex"))
        weatherDao.insertWeather(WeatherEntity(id = 3, name = "Giza"))

        // When getting all weather
        val result = weatherDao.getAllWeatherList()

        // Then all three are returned
        assertThat(result.size, `is`(3))
    }

    @Test
    fun deleteWeatherByCityId_getAll_doesNotContainDeletedEntity() = runTest {
        // Given two weather entities inserted
        val weather1 = WeatherEntity(id = 1, name = "Cairo")
        weatherDao.insertWeather(weather1)
        weatherDao.insertWeather(WeatherEntity(id = 2, name = "Alex"))

        // When deleting the first one
        weatherDao.deleteWeatherByCityId(weather1.id.toLong())

        // Then only one remains
        val result = weatherDao.getAllWeatherList()
        assertThat(result.size, `is`(1))
        assertThat(result.none { it.id == weather1.id }, `is`(true))
    }

    @Test
    fun deleteAllWeather_getAll_returnsEmptyList() = runTest {
        // Given two weather entities inserted
        weatherDao.insertWeather(WeatherEntity(id = 1, name = "Cairo"))
        weatherDao.insertWeather(WeatherEntity(id = 2, name = "Alex"))

        // When deleting all weather
        weatherDao.deleteAllWeather()

        // Then result is empty
        val result = weatherDao.getAllWeatherList()
        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun getWeatherByCityId_nonExistentId_returnsNull() = runTest {
        // Given an empty database

        // When getting weather by a non-existent id
        val result = weatherDao.getWeatherByCityId(999)

        // Then result is null
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun insertWeather_withSameId_replacesOldEntity() = runTest {
        // Given a weather entity inserted
        weatherDao.insertWeather(WeatherEntity(id = 1, name = "Cairo"))

        // When inserting another entity with the same id
        weatherDao.insertWeather(WeatherEntity(id = 1, name = "Cairo Updated"))

        // Then the old entity is replaced
        val result = weatherDao.getWeatherByCityId(1)
        assertThat(result?.name, `is`("Cairo Updated"))
    }

}