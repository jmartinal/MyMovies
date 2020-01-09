package com.jmartinal.data.repository

import com.jmartinal.data.source.LanguageDataSource

class LanguageRepository(private val languageDataSource: LanguageDataSource) {

    fun getCurrentLanguage(): String {
        return languageDataSource.getCurrentLanguage()
    }

}