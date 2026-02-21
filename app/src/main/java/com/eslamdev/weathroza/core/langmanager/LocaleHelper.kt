package com.eslamdev.weathroza.core.langmanager

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.eslamdev.weathroza.core.settings.AppLanguage

object LocaleHelper {
    fun setAppLanguage(language: AppLanguage) {
        val tag = when (language) {
            AppLanguage.SYSTEM  -> ""
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC  -> "ar"
        }
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(tag)
        )
    }
}