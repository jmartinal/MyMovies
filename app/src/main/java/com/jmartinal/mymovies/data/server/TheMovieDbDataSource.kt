package com.jmartinal.mymovies.data.server

import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.data.toDomainMovie

class TheMovieDbDataSource : RemoteDataSource {
    override suspend fun getPopularMovies(region: String, language: String): List<Movie> =
        TheMovieDb(Constants.TmdbApi.BASE_URL).service.listMostPopularMoviesAsync(
            region,
            language
        ).await().results.map { it.toDomainMovie() }
}
