package com.jmartinal.mymovies.ui.detail

import com.jmartinal.mymovies.model.Movie

class MovieDetailPresenter {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun updateUI(movie: Movie)
    }

    private var view: View? = null

    fun onCreate(view: View, movie: Movie) {
        this.view = view
        fetchMovieData(movie)
    }

    fun onDestroy() {
        this.view = null
    }

    fun fetchMovieData(movie: Movie) {
        view?.apply {
            showProgress()
            updateUI(movie)
            hideProgress()
        }
    }
}