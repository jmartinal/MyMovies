package com.jmartinal.mymovies.ui.main

import com.jmartinal.mymovies.model.Movie
import com.jmartinal.mymovies.model.MoviesRepository
import com.jmartinal.mymovies.model.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter(
    private val moviesRepository: MoviesRepository,
    private val networkManager: NetworkManager
) {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun updateData(movies: List<Movie>)
        fun navigateTo(movie: Movie)
        fun showNoConnectivityError()
    }

    private var view: View? = null

    fun onCreate(view: View) {
        this.view = view
        requestMovies()
    }

    fun requestMovies() {
        view?.showProgress()
        if (networkManager.isConnected()) {
            GlobalScope.launch(Dispatchers.Main) {
                view?.apply {
                    updateData(moviesRepository.findPopularMovies().results)
                    hideProgress()
                }
            }
        } else {
            view?.apply {
                hideProgress()
                showNoConnectivityError()
            }
        }
    }

    fun onDestroy() {
        view = null
    }

    fun onMovieClicked(movie: Movie) {
        view?.navigateTo(movie)
    }
}