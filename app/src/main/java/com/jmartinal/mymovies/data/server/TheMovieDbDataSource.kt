package com.jmartinal.mymovies.data.server

import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.data.toDomainMovie

class TheMovieDbDataSource(private val theMovieDb: TheMovieDb) : RemoteDataSource {
    override suspend fun getPopularMovies(region: String, language: String): List<Movie> =
        theMovieDb.service.listMostPopularMoviesAsync(
            region,
            language
        ).await().results.map { it.toDomainMovie() }
}
