package com.jmartinal.mymovies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.data.NetworkManager
import com.jmartinal.mymovies.ui.SingleLiveEvent
import com.jmartinal.usecases.GetPopularMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPopularMovies: GetPopularMovies,
    private val networkManager: NetworkManager
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(
        private val getPopularMovies: GetPopularMovies,
        private val networkManager: NetworkManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MainViewModel(getPopularMovies, networkManager) as T
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList: LiveData<List<Movie>> get() = _moviesList

    private val _error = SingleLiveEvent<MainUIError>()
    val error: SingleLiveEvent<MainUIError> get() = _error

    private val _navigateToDetails = SingleLiveEvent<Movie>()
    val navigateToDetails: LiveData<Movie>
        get() = _navigateToDetails

    init {
        requestMovies()
    }

    private fun requestMovies() {
        if (networkManager.isConnected()) {
            GlobalScope.launch(Dispatchers.Main) {
                _loading.value = true
                _moviesList.value = getPopularMovies.invoke()
                _loading.value = false
            }
        } else {
            _loading.value = true
            _error.value = MainUIError.NetworkError
            _loading.value = false
        }
    }

    fun onMovieClicked(movie: Movie) {
        _navigateToDetails.value = movie
    }

}