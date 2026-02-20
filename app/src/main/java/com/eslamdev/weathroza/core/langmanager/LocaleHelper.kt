package com.eslamdev.weathroza.core.langmanager

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.eslamdev.weathroza.core.settings.AppLanguage

object LocaleHelper {

    fun setLocale(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // API 33+
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            // API 21–32
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(languageCode)
            )
        }
    }
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