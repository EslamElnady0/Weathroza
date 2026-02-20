package com.eslamdev.weathroza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eslamdev.weathroza.core.langmanager.AppLocaleWrapper
import com.eslamdev.weathroza.core.langmanager.LanguageManager
import com.eslamdev.weathroza.core.settings.AppLanguage
import com.eslamdev.weathroza.presentaion.routes.App
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModel
import com.eslamdev.weathroza.presentaion.settings.viewmodel.SettingsViewModelFactory
import com.eslamdev.weathroza.ui.theme.WeathrozaTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModelFactory(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeathrozaTheme(darkTheme = true) {

                val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

                val resolvedLanguage = when (settings.language) {
                    AppLanguage.SYSTEM  -> Locale.getDefault().language
                    AppLanguage.ENGLISH -> "en"
                    AppLanguage.ARABIC  -> "ar"
                }

                LaunchedEffect(resolvedLanguage) {
                    LanguageManager.currentLanguage = resolvedLanguage
                }
                AppLocaleWrapper(language = LanguageManager.currentLanguage) {
                    App(settingsViewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeathrozaTheme {
        //  Greeting("Android")
    }
}