package com.jmartinal.mymovies.model

import android.content.Context
import android.os.Build

class LanguageRepository(val context: Context) {

    @Suppress("DEPRECATION")
    fun getCurrentLanguage(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0].language
    } else {
        context.resources.configuration.locale.language
    }

}