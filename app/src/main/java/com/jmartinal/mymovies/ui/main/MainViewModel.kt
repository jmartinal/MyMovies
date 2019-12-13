package com.jmartinal.mymovies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmartinal.mymovies.model.Movie
import com.jmartinal.mymovies.model.MoviesRepository
import com.jmartinal.mymovies.model.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val moviesRepository: MoviesRepository,
    private val networkManager: NetworkManager
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(
        private val moviesRepository: MoviesRepository,
        private val networkManager: NetworkManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MainViewModel(moviesRepository, networkManager) as T
    }

    private val _state = MutableLiveData<MainUIModel>()
    val state: LiveData<MainUIModel>
        get() {
            if (_state.value == null) {
                requestMovies()
            }
            return _state
        }

    private fun requestMovies() {
        _state.value = MainUIModel.Loading
        if (networkManager.isConnected()) {
            GlobalScope.launch(Dispatchers.Main) {
                _state.value =
                    MainUIModel.ShowingResult(moviesRepository.findPopularMovies().results)
            }
        } else {
            _state.value = MainUIModel.ShowingError(MainUIError.NetworkError)
        }
    }

    fun onMovieClicked(movie: Movie) {
        _state.value = MainUIModel.Navigating(movie)
    }
}