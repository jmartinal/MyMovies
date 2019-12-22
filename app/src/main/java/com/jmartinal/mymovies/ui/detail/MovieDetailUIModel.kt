package com.jmartinal.mymovies.ui.detail

import com.jmartinal.mymovies.model.database.Movie


sealed class MovieDetailUIModel {
    object Loading : MovieDetailUIModel()
    class ShowingResult(val movie: Movie) : MovieDetailUIModel()
}