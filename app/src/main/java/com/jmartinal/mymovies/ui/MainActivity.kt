package com.jmartinal.mymovies.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.model.TmdbFactory.tmdbService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val adapter: MoviesAdapter = MoviesAdapter {
        Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
            putExtra(Constants.Communication.KEY_MOVIE, it)
            startActivity(this)
        }
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
