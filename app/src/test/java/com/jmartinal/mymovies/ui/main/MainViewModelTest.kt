package com.jmartinal.mymovies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jmartinal.domain.Movie
import com.jmartinal.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
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
    fun `after requesting permissions, loading is shown`() {
        runBlocking {
            GlobalScope.launch(Dispatchers.Main) {
                vm.loading.observeForever(loadingObserver)
                vm.onPermissionGranted()
                verify(loadingObserver).onChanged(true)
            }
        }
    }
}