package com.jmartinal.mymovies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmartinal.data.ConnectivityManager
import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.ui.FakeConnectivityManager
import com.jmartinal.mymovies.ui.FakeLocalDataSource
import com.jmartinal.mymovies.ui.fakeMovies
import com.jmartinal.mymovies.ui.initMockedDi
import com.jmartinal.testshared.mockedMovie
import com.jmartinal.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesObserver: Observer<List<Movie>>

    @Mock
    lateinit var errorObserver: Observer<MainUIError>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        val vmModule = module {
            factory { MainViewModel(get(), get()) }
            factory { GetPopularMovies(get()) }
        }

        initMockedDi(vmModule)

        viewModel = get()
    }

    @Test
    fun `data is loaded from server when local data source is empty`() {
        viewModel.moviesList.observeForever(moviesObserver)
        viewModel.onPermissionGranted()
        verify(moviesObserver).onChanged(fakeMovies)
    }

    @Test
    fun `data is loaded from local when available`() {
        val fakeLocalMovies = listOf(
            mockedMovie.copy(id = 11),
            mockedMovie.copy(id = 12),
            mockedMovie.copy(id = 13)
        )
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = fakeLocalMovies

        viewModel.moviesList.observeForever(moviesObserver)
        viewModel.onPermissionGranted()
        verify(moviesObserver).onChanged(fakeLocalMovies)
    }

    @Test
    fun `no movies are loaded when there is no connectivity`() {
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = emptyList()

        val connectivityManager = get<ConnectivityManager>() as FakeConnectivityManager
        connectivityManager.connectivityAvailable = false

        viewModel.error.observeForever(errorObserver)
        viewModel.onPermissionGranted()
        verify(errorObserver).onChanged(MainUIError.NetworkError)
    }

}