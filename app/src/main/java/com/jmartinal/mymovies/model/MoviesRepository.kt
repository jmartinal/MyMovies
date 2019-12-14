package com.jmartinal.mymovies.model

import android.app.Application

class MoviesRepository(application: Application) {

    private val regionRepository = RegionRepository(application)
    private val languageRepository = LanguageRepository(application)

    suspend fun findPopularMovies(): TmdbMovieResult {
        return TmdbFactory.tmdbService.listMostPopularMoviesAsync(
            regionRepository.getCurrentRegion(),
            languageRepository.getCurrentLanguage()
        )
            .await()
    }
}