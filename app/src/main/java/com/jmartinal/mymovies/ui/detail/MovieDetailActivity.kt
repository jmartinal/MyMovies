package com.jmartinal.mymovies.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.R
import com.jmartinal.mymovies.databinding.ActivityMovieDetailBinding
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailActivity : AppCompatActivity() {

    private val movieId: Long by lazy {
        with(intent.extras) {
            this?.getLong(Constants.Communication.KEY_MOVIE, -1)
        } ?: -1
    }
    private val viewModel: MovieDetailViewModel by currentScope.viewModel(this) {
        parametersOf(movieId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMovieDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

    }

}
