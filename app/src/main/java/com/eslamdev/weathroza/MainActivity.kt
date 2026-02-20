package com.eslamdev.weathroza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeathrozaTheme(darkTheme = true) {
                App()
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