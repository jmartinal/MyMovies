package com.jmartinal.mymovies.ui.detail

import com.jmartinal.mymovies.model.Movie

sealed class MovieDetailUIModel {
    object Loading : MovieDetailUIModel()
    class ShowingResult(val movie: Movie) : MovieDetailUIModel()
}