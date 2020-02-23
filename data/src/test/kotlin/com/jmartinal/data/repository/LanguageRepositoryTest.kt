package com.jmartinal.data.repository

import com.jmartinal.data.source.LanguageDataSource
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LanguageRepositoryTest {

    @Mock
    lateinit var languageDataSource: LanguageDataSource

    @Mock
    lateinit var languageRepository: LanguageRepository

    @Before
    fun setup() {
        languageRepository = LanguageRepository(languageDataSource)
    }

    @Test
    fun `returns language from language data source`() {
        runBlocking {
            whenever(languageDataSource.getCurrentLanguage()).thenReturn("ES")
            val result = languageRepository.getCurrentLanguage()
            assertEquals(result, "ES")
        }
    }

}