package com.jmartinal.mymovies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmartinal.domain.Movie
import com.jmartinal.testshared.mockedMovie
import com.jmartinal.usecases.GetMovieById
import com.jmartinal.usecases.ToggleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var movieObserver: Observer<Movie>

    @Mock
    lateinit var favoriteObserver: Observer<Boolean>

    @Mock
    lateinit var getMovieById: GetMovieById

    @Mock
    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    private lateinit var vm: MovieDetailViewModel

    @Before
    fun setup() {
        vm = MovieDetailViewModel(1, getMovieById, toggleMovieFavorite, Dispatchers.Unconfined)
    }

    @Test
    fun `observing LiveData finds the movie`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(getMovieById.invoke(1)).thenReturn(movie)

            vm.movie.observeForever(movieObserver)

            vm.initView()

            verify(movieObserver).onChanged(movie)
        }
    }

    @Test
    fun `marking movie as favorite triggers toggleMovieFavorite`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(getMovieById.invoke(1)).thenReturn(movie)
            whenever(toggleMovieFavorite.invoke(movie)).thenReturn(movie.copy(favorite = !movie.favorite))

            vm.favorite.observeForever(favoriteObserver)

            vm.initView()
            vm.onFavoriteClicked()

            verify(favoriteObserver).onChanged(!movie.favorite)
        }
    }
}