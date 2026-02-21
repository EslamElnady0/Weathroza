package com.eslamdev.weathroza.core.settings.langmanager

import android.content.res.Configuration
import android.text.TextUtils
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale
import androidx.core.text.layoutDirection


@Composable
fun AppLocaleWrapper(language: String, content: @Composable () -> Unit) {
    val locale = Locale(language)
    val configuration = Configuration(LocalConfiguration.current)
    configuration.setLocale(locale)

    val context = LocalContext.current
    val localizedContext = remember(language) {
        context.createConfigurationContext(configuration)
    }

    val layoutDirection = if (locale.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides configuration,
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}