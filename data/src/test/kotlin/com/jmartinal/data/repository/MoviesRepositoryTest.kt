package com.jmartinal.data.repository

import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {

    @Mock
    lateinit var localDataSource: LocalDataSource
    @Mock
    lateinit var remoteDataSource: RemoteDataSource
    @Mock
    lateinit var regionRepository: RegionRepository
    @Mock
    lateinit var languageRepository: LanguageRepository
    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Before
    fun setup() {
        moviesRepository = MoviesRepository(
            localDataSource,
            remoteDataSource,
            regionRepository,
            languageRepository
        )
    }

    @Test
    fun `getPopularMovies gets data from local first`() {
        runBlocking {
            val expected = listOf(mockedMovie.copy(id = 1))
            whenever(localDataSource.isEmpty()).thenReturn(false)
            whenever(localDataSource.getPopularMovies()).thenReturn(expected)

            val result = moviesRepository.getPopularMovies()

            assertEquals(expected, result)
        }
    }

    @Test
    fun `getPopularMovies gets data from remote and saves it to local`() {
        runBlocking {
            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(localDataSource.isEmpty()).thenReturn(true)
            whenever(remoteDataSource.getPopularMovies(any(), any())).thenReturn(movies)
            whenever(regionRepository.getCurrentRegion()).thenReturn("ES")
            whenever(languageRepository.getCurrentLanguage()).thenReturn("ES")

            moviesRepository.getPopularMovies()
            verify(localDataSource).saveMovies(movies)
        }
    }

    @Test
    fun `findById gets data from local`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(localDataSource.getMovieById(movie.id)).thenReturn(movie)
            val result = moviesRepository.findById(movie.id)

            assertEquals(movie, result)
        }
    }

    @Test
    fun `update updates local`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            moviesRepository.update(movie)
            verify(localDataSource).updateMovie(movie)
        }
    }
}
