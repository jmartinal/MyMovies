package com.jmartinal.mymovies.ui.main

import com.jmartinal.mymovies.model.database.Movie


sealed class MainUIModel {
    object Loading : MainUIModel()
    class ShowingError(val error: MainUIError) : MainUIModel()
    class ShowingResult(val movies: List<Movie>) : MainUIModel()
    class Navigating(val movie: Movie) : MainUIModel()
}