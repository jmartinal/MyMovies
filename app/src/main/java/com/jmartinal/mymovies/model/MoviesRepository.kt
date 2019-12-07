package com.jmartinal.mymovies.model

import android.content.Context

class MoviesRepository(context: Context) {

    private val regionRepository = RegionRepository(context)
    private val languageRepository = LanguageRepository(context)

    suspend fun findPopularMovies(): TmdbMovieResult {
        return TmdbFactory.tmdbService.listMostPopularMoviesAsync(
            regionRepository.getCurrentRegion(),
            languageRepository.getCurrentLanguage()
        )
            .await()
    }
}