package com.jmartinal.mymovies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.ui.FakeLocalDataSource
import com.jmartinal.mymovies.ui.fakeMovies
import com.jmartinal.mymovies.ui.initMockedDi
import com.jmartinal.testshared.mockedMovie
import com.jmartinal.usecases.GetMovieById
import com.jmartinal.usecases.ToggleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieObserver: Observer<Movie>

    @Mock
    lateinit var favoriteObserver: Observer<Boolean>

    lateinit var viewModel: MovieDetailViewModel
    lateinit var localDataSource: FakeLocalDataSource

    @Before
    fun setup() {
        val vmModule = module {
            factory { (movieId: Long) -> MovieDetailViewModel(movieId, get(), get(), get()) }
            factory { GetMovieById(get()) }
            factory { ToggleMovieFavorite(get()) }

        }

        initMockedDi(vmModule)
        viewModel = get { parametersOf(1) }
        localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = fakeMovies
    }

    @Test
    fun `observing LiveData finds the movie`() {
        viewModel.movie.observeForever(movieObserver)

        verify(movieObserver).onChanged(mockedMovie.copy(1))
    }

    @Test
    fun `toggle favorite updates local data source`() {
        viewModel.favorite.observeForever(favoriteObserver)

        viewModel.onFavoriteClicked()

        runBlocking {
            Assert.assertTrue(localDataSource.getMovieById(1).favorite)
        }
    }

}