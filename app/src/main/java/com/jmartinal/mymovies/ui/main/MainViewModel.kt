package com.jmartinal.mymovies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antonioleiva.mymovies.ui.common.ScopedViewModel
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.ui.SingleLiveEvent
import com.jmartinal.usecases.GetPopularMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPopularMovies: GetPopularMovies,
    override val uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList: LiveData<List<Movie>> get() = _moviesList

    private val _requestPermission = SingleLiveEvent<Boolean>()
    val requestPermission: SingleLiveEvent<Boolean> get() = _requestPermission

    private val _error = SingleLiveEvent<MainUIError>()
    val error: SingleLiveEvent<MainUIError> get() = _error

    private val _navigateToDetails = SingleLiveEvent<Movie>()
    val navigateToDetails: LiveData<Movie>
        get() = _navigateToDetails

    init {
        refresh()
    }

    private fun refresh() {
        _requestPermission.value = true
    }

    fun onPermissionGranted() {
        launch {
            _loading.value = true
            val movies = getPopularMovies.invoke()
            _loading.value = false
            if (movies.isNotEmpty()) {
                _moviesList.value = movies
            } else {
                _error.value = MainUIError.NetworkError
            }
        }
    }

    fun onPermissionDenied() {
        _loading.value = true
        _error.value = MainUIError.GenericError
        _loading.value = false
    }

    fun onMovieClicked(movie: Movie) {
        _navigateToDetails.value = movie
    }

}
