package com.jmartinal.mymovies.model

import androidx.appcompat.app.AppCompatActivity

class MoviesRepository(activity: AppCompatActivity, private val language: String) {

    private val regionRepository = RegionRepository(activity)

    suspend fun getMovies() = TmdbFactory.tmdbService.listMostPopularMoviesAsync(
        regionRepository.findLastRegion(),
        language
    ).await()
}