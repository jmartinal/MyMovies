package com.jmartinal.mymovies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmartinal.mymovies.model.Movie

class MovieDetailViewModel(val movie: Movie) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class MovieDetailViewModelFactory(private val movie: Movie) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MovieDetailViewModel(movie) as T

    }

    private val _state = MutableLiveData<MovieDetailUIModel>()
    val state: LiveData<MovieDetailUIModel>
        get() {
            if (_state.value == null) {
                fetchMovieData(movie)
            }
            return _state
        }

    fun fetchMovieData(movie: Movie) {
        _state.value = MovieDetailUIModel.Loading
        _state.value = MovieDetailUIModel.ShowingResult(movie)
    }
}