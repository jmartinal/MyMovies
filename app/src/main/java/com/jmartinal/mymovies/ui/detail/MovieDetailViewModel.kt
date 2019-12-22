package com.jmartinal.mymovies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmartinal.mymovies.model.server.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Long,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class MovieDetailViewModelFactory(
        private val movieId: Long,
        private val moviesRepository: MoviesRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MovieDetailViewModel(movieId, moviesRepository) as T

    }

    private val _state = MutableLiveData<MovieDetailUIModel>()
    val state: LiveData<MovieDetailUIModel>
        get() {
            if (_state.value == null) {
                findMovie(movieId)
            }
            return _state
        }

    private fun findMovie(movieId: Long) = GlobalScope.launch(Dispatchers.Main) {
        _state.value = MovieDetailUIModel.Loading
        _state.value = MovieDetailUIModel.ShowingResult(moviesRepository.getById(movieId))
    }

    fun onFavoriteClicked() = GlobalScope.launch(Dispatchers.Main) {
        if (_state.value is MovieDetailUIModel.ShowingResult) {
            (_state.value as MovieDetailUIModel.ShowingResult).movie?.let {
                val updatedMovie = it.copy(favorite = !it.favorite)
                _state.value = MovieDetailUIModel.ShowingResult(updatedMovie)
                moviesRepository.update(updatedMovie)
            }
        }

    }
}