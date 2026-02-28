package com.eslamdev.weathroza.data.datasources.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eslamdev.weathroza.data.config.db.WeatherDataBase
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class FavouriteLocationDaoTest {
    private lateinit var database: WeatherDataBase
    private lateinit var favDao: FavouriteLocationDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()

        favDao = database.getFavouriteLocationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetFavLocationTest_returnsFavLocation() = runTest {
        //Given FavEntity
        val favLocEntity = FavouriteLocationEntity(cityId = 1, enName = "fav city")
        //when Inserting this entity in database
        favDao.insertFavourite(favLocEntity)
        //then get this entity from database as result
        val result = favDao.getFavouriteById(1).first()
        assertNotNull(result)
        assertThat(result!!.cityId, `is`(favLocEntity.cityId))
        assertThat(result.enName, `is`(favLocEntity.enName))
    }

    @Test
    fun deleteFavourite_getAll_doesNotContainDeletedEntity() = runTest {
        // Given two favourites inserted
        val fav1 = FavouriteLocationEntity(cityId = 1, enName = "fav city 1")
        val fav2 = FavouriteLocationEntity(cityId = 2, enName = "fav city 2")
        favDao.insertFavourite(fav1)
        favDao.insertFavourite(fav2)

        // When deleting the first one
        favDao.deleteFavouriteById(fav1.cityId)

        // Then only one remains
        val result = favDao.getAllFavourites().first()
        assertThat(result.size, `is`(1))
    }

    @Test
    fun updateLastWeather_updatesLastTempAndIconUrl_returnsUpdatedEntity() = runTest {
        // Given one favourite inserted
        val fav1 = FavouriteLocationEntity(
            cityId = 1,
            enName = "fav city 1",
            lastTemp = 20.0,
            iconUrl = "icon_url"
        )
        favDao.insertFavourite(fav1)

        // When updating the first one
        favDao.updateLastWeather(1, 25.0, "icon_url_updated")

        // Then returns the updated entity
        val result = favDao.getFavouriteById(1).first()
        assertNotNull(result)
        assertThat(result!!.lastTemp, `is`(25.0))
        assertThat(result.iconUrl, `is`("icon_url_updated"))
    }


    @Test
    fun getAll_emptyDatabase_returnsEmptyList() = runTest {
        // Given an empty database
        // When getting all favourites
        val result = favDao.getAllFavourites().first()

        // Then result is empty
        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun getById_nonExistentId_returnsNull() = runTest {
        // Given an empty database
        // When getting a favourite by a non-existent id
        val result = favDao.getFavouriteById(1).first()

        // Then result is null
        assertThat(result, `is`(nullValue()))
    }
}