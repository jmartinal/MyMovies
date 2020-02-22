package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToggleMovieFavoriteTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Mock
    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Before
    fun setup() {
        toggleMovieFavorite = ToggleMovieFavorite(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            val result = toggleMovieFavorite.invoke(movie)
            verify(moviesRepository).update(result)
        }
    }

    @Test
    fun `check favorite makes movie favorite`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1, favorite = false)
            val result = toggleMovieFavorite.invoke(movie)

            assertTrue(result.favorite)
        }
    }

    @Test
    fun `uncheck favorite make movie not-favorite`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1, favorite = true)
            val result = toggleMovieFavorite.invoke(movie)

            assertFalse(result.favorite)
        }
    }
}
