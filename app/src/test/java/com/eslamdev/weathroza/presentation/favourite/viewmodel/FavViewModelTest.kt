package com.eslamdev.weathroza.presentation.favourite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eslamdev.weathroza.core.common.UiState
import com.eslamdev.weathroza.data.models.fav.FavouriteLocationEntity
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.data.repo.WeatherRepo
import com.eslamdev.weathroza.presentaion.favourite.viewmodel.FavViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: FavViewModel
    private val repo: WeatherRepo = mockk()
    private val settingsRepo: UserSettingsRepo = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        every { settingsRepo.settingsFlow } returns flowOf(UserSettings())
    }

    @Test
    fun favourites_whenRepoReturnsData_emitsSuccessWithCorrectData() = runTest {
        // Given repo returns a list of two favourites
        val fav1 = FavouriteLocationEntity(cityId = 1, enName = "Cairo")
        val fav2 = FavouriteLocationEntity(cityId = 2, enName = "Alex")
        every { repo.getAllFavourites() } returns flowOf(Result.success(listOf(fav1, fav2)))
        viewModel = FavViewModel(repo, settingsRepo)

        backgroundScope.launch {
            viewModel.favourites.collect {}
        }
        advanceUntilIdle()
        // When observing favourites state
        val state = viewModel.favourites.value
        // Then emits success with correct data
        assertThat(state, instanceOf(UiState.Success::class.java))
        assertThat((state as UiState.Success).data.size, `is`(2))
        assertThat(state.data[0].enName, `is`("Cairo"))
        assertThat(state.data[1].enName, `is`("Alex"))
    }

    @Test
    fun favourites_whenRepoFails_emitsErrorWithCorrectMessage() = runTest {
        // Given repo returns a failure
        every { repo.getAllFavourites() } returns flowOf(Result.failure(Exception("database error")))
        viewModel = FavViewModel(repo, settingsRepo)

        backgroundScope.launch {
            viewModel.favourites.collect {}
        }

        advanceUntilIdle()

        // When observing favourites state
        val state = viewModel.favourites.value

        // Then emits error with correct message
        assertThat(state, instanceOf(UiState.Error::class.java))
        assertThat((state as UiState.Error).message, `is`("database error"))
    }

    @Test
    fun favourites_afterRemovingOne_flowUpdatesToOneItem() = runTest {
        // Given a flow that emits 2 items then 1 after deletion
        val fav1 = FavouriteLocationEntity(cityId = 1, enName = "Cairo")
        val fav2 = FavouriteLocationEntity(cityId = 2, enName = "Alex")

        val favouritesFlow = MutableStateFlow(listOf(fav1, fav2))
        every { repo.getAllFavourites() } returns favouritesFlow.map { Result.success(it) }
        coEvery { repo.removeFavourite(1L) } coAnswers {
            favouritesFlow.value = listOf(fav2)
        }

        viewModel = FavViewModel(repo, settingsRepo)

        backgroundScope.launch {
            viewModel.favourites.collect {}
        }
        advanceUntilIdle()

        // When state has 2 items
        assertThat(
            (viewModel.favourites.value as UiState.Success).data.size,
            `is`(2)
        )

        // When removing the first favourite
        viewModel.removeFavourite(1L)
        advanceUntilIdle()

        // Then flow updates to 1 item and does not contain deleted item
        val updatedState = viewModel.favourites.value as UiState.Success
        assertThat(updatedState.data.size, `is`(1))
        assertThat(updatedState.data.none { it.cityId == 1L }, `is`(true))
        assertThat(updatedState.data[0].enName, `is`("Alex"))
    }
}