package com.jmartinal.data.repository

import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.domain.Movie

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val regionRepository: RegionRepository,
    private val languageRepository: LanguageRepository
    ) {

    suspend fun getPopularMovies(): List<Movie> {
        return if (localDataSource.isEmpty()) {
            val movies = remoteDataSource.getPopularMovies(
                regionRepository.getCurrentRegion(),
                languageRepository.getCurrentLanguage()
            )
            localDataSource.saveMovies(movies)
            localDataSource.getPopularMovies()
        } else {
            localDataSource.getPopularMovies()
        }
    }

    suspend fun findById(id: Long): Movie {
        return localDataSource.getMovieById(id)
    }

    suspend fun update(movie: Movie) {
        return localDataSource.updateMovie(movie)
    }
}
