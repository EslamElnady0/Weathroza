package com.eslamdev.weathroza.presentation.settings.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eslamdev.weathroza.core.settings.langmanager.LocaleHelper
import com.eslamdev.weathroza.data.models.usersettings.AppLanguage
import com.eslamdev.weathroza.data.models.usersettings.TemperatureUnit
import com.eslamdev.weathroza.data.models.usersettings.UserSettings
import com.eslamdev.weathroza.data.models.usersettings.WindSpeedUnit
import com.eslamdev.weathroza.data.repo.UserSettingsRepo
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SettingsViewModel
    private val settingsRepo: UserSettingsRepo = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { settingsRepo.settingsFlow } returns flowOf(UserSettings())
        viewModel = SettingsViewModel(settingsRepo)
    }

    @Test
    fun onTemperatureUnitChanged_savesCorrectUnit() = runTest {
        // Given fahrenheit is at index 1
        coEvery { settingsRepo.saveTemperatureUnit(unit = TemperatureUnit.FAHRENHEIT) } just runs

        // When changing temperature unit to fahrenheit
        viewModel.onTemperatureUnitChanged(1)
        advanceUntilIdle()

        // Then repo saves fahrenheit
        coVerify(exactly = 1) { settingsRepo.saveTemperatureUnit(TemperatureUnit.FAHRENHEIT) }
    }

    @Test
    fun onWindSpeedUnitChanged_savesCorrectUnit() = runTest {
        // Given mph is at index 1
        coEvery { settingsRepo.saveWindSpeedUnit(WindSpeedUnit.MPH) } just runs

        // When changing wind speed unit to mph
        viewModel.onWindSpeedUnitChanged(1)
        advanceUntilIdle()

        // Then repo saves mph
        coVerify(exactly = 1) { settingsRepo.saveWindSpeedUnit(WindSpeedUnit.MPH) }
    }

    @Test
    fun onLanguageChanged_savesLanguageAndUpdatesLocale() = runTest {
        // Given arabic language
        coEvery { settingsRepo.saveLanguage(AppLanguage.ARABIC) } just runs
        mockkObject(LocaleHelper)
        every { LocaleHelper.setAppLanguage(AppLanguage.ARABIC) } just runs

        // When changing language to arabic
        viewModel.onLanguageChanged(AppLanguage.ARABIC)
        advanceUntilIdle()

        // Then repo saves arabic and locale is updated
        coVerify(exactly = 1) { settingsRepo.saveLanguage(AppLanguage.ARABIC) }
        verify(exactly = 1) { LocaleHelper.setAppLanguage(AppLanguage.ARABIC) }
    }
}