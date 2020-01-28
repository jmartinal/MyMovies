package com.jmartinal.mymovies.data

import android.app.Application
import android.os.Build
import com.jmartinal.data.source.LanguageDataSource

class DeviceLanguageDataSource(val application: Application): LanguageDataSource {

    @Suppress("DEPRECATION")
    override fun getCurrentLanguage(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        application.resources.configuration.locales[0].language
    } else {
        application.resources.configuration.locale.language
    }
}