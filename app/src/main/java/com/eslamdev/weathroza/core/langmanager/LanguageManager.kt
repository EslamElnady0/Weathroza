package com.eslamdev.weathroza.core.langmanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

object LanguageManager {
    var currentLanguage by mutableStateOf("en")
    val currentLocale: Locale
        get() = Locale(currentLanguage)
}