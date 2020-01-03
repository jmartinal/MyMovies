package com.jmartinal.mymovies.ui.detail

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.model.database.Movie
import com.jmartinal.mymovies.model.server.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _backdropPath = MutableLiveData<String>()
    val backdropPath: LiveData<String> get() = _backdropPath

    private val _posterPath = MutableLiveData<String>()
    val posterPath: LiveData<String> get() = _posterPath

    private val _releaseDate = MutableLiveData<String>()
    val releaseDate: LiveData<String> get() = _releaseDate

    private val _rating = MutableLiveData<Float>()
    val rating: LiveData<Float> get() = _rating

    private val _overview = MutableLiveData<String>()
    val overview: LiveData<String> get() = _overview

    private val _originalTitle = MutableLiveData<String>()
    val originalTitle: LiveData<String> get() = _originalTitle

    private val _originalLanguage = MutableLiveData<String>()
    val originalLanguage: LiveData<String> get() = _originalLanguage

    private val _popularity = MutableLiveData<String>()
    val popularity: LiveData<String> get() = _popularity

    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> get() = _favorite


    init {
        GlobalScope.launch(Dispatchers.Main) {
            _movie.value = moviesRepository.getById(movieId)
            updateUI()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateUI() {
        movie.value?.run {
            _title.value = title
            _backdropPath.value = Constants.TmdbApi.BACKDROP_BASE_URL + backdropPath
            _posterPath.value = Constants.TmdbApi.POSTER_BASE_URL + posterPath
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = parser.parse(releaseDate)
            val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val formattedDate = date?.let {
                formatter.format(it).capitalize()
            }
            _releaseDate.value = formattedDate
            _rating.value = (voteAverage / 2)
            _overview.value = overview
            _originalTitle.value = originalTitle
            _originalLanguage.value = originalLanguage
            _popularity.value = popularity.toString()
            _favorite.value = favorite

        }
    }

    fun onFavoriteClicked() = GlobalScope.launch(Dispatchers.Main) {
        _movie.value?.let {
            val updatedMovie = it.copy(favorite = !it.favorite)
            moviesRepository.update(updatedMovie)
            _movie.value = updatedMovie
            updateUI()
        }
    }
}