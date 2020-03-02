package com.jmartinal.mymovies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmartinal.domain.Movie
import com.jmartinal.testshared.mockedMovie
import com.jmartinal.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getPopularMovies: GetPopularMovies

    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    @Mock
    lateinit var moviesObserver: Observer<List<Movie>>

    @Mock
    lateinit var permissionsObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<MainUIError>

    @Mock
    lateinit var navigationObserver: Observer<Movie>

    private lateinit var vm: MainViewModel

    @Before
    fun setup() {
        vm = MainViewModel(getPopularMovies)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when started, permissions are requested`() {
        vm.requestPermission.observeForever(permissionsObserver)

        verify(permissionsObserver).onChanged(true)
    }

    @Test
    fun `when permissions accepted, loading is shown and then hidden`() {
        runBlocking {
            vm.loading.observeForever(loadingObserver)

            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getPopularMovies.invoke()).thenReturn(movies)

            vm.onPermissionGranted()

            verify(loadingObserver, times(1)).onChanged(true)
            verify(loadingObserver, times(1)).onChanged(false)
        }
    }

    @Test
    fun `when permissions denied, error is shown`() {
        vm.error.observeForever(errorObserver)
        vm.onPermissionDenied()

        verify(errorObserver).onChanged(MainUIError.GenericError)
    }

    @Test
    fun `when permissions granted and connectivity is up, movies are fetched`() {
        runBlocking {
            vm.moviesList.observeForever(moviesObserver)

            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getPopularMovies.invoke()).thenReturn(movies)

            vm.onPermissionGranted()

            verify(moviesObserver).onChanged(movies)
        }
    }

    @Test
    fun `when permissions granted and connectivity is down, error is shown`() {
        runBlocking {
            vm.error.observeForever(errorObserver)

            whenever(getPopularMovies.invoke()).thenReturn(emptyList())

            vm.onPermissionGranted()

            verify(errorObserver).onChanged(MainUIError.NetworkError)
        }
    }

    @Test
    fun `when movie clicked, navigation is triggered`() {
        val movie = mockedMovie.copy(id = 1)
        vm.navigateToDetails.observeForever(navigationObserver)

        vm.onMovieClicked(movie)

        verify(navigationObserver).onChanged(movie)
    }
}