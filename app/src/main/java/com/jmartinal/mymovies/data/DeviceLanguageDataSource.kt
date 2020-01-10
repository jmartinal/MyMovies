package com.jmartinal.mymovies.data

import android.os.Build
import com.jmartinal.data.source.LanguageDataSource
import com.jmartinal.mymovies.MovieApp

class DeviceLanguageDataSource(val application: MovieApp): LanguageDataSource {

    override fun getCurrentLanguage(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        application.resources.configuration.locales[0].language
    } else {
        application.resources.configuration.locale.language
    }
}