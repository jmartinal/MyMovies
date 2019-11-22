package com.jmartinal.mymovies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.TmdbFactory.tmdbService
import com.jmartinal.mymovies.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val adapter: MoviesAdapter = MoviesAdapter {
        toast(it.title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Main) {
            val movies = tmdbService.listMostPopularMoviesAsync().await()
            adapter.movies = movies.results
            adapter.notifyDataSetChanged()
        }

        moviesList.adapter = adapter
    }
}
