package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieByIdTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Mock
    lateinit var getMovieById: GetMovieById

    @Before
    fun setup() {
        getMovieById = GetMovieById(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(moviesRepository.findById(1)).thenReturn(movie)

            val result = getMovieById.invoke(1)

            assertEquals(movie, result)
        }
    }

}