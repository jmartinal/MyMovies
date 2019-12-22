package com.jmartinal.mymovies.model.server

import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.model.LanguageRepository
import com.jmartinal.mymovies.model.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.jmartinal.mymovies.model.database.Movie as DbMovie
import com.jmartinal.mymovies.model.server.Movie as ServerMovie

class MoviesRepository(application: MovieApp) {

    private val regionRepository = RegionRepository(application)
    private val languageRepository = LanguageRepository(application)
    private val database = application.database

    suspend fun findPopularMovies(): List<DbMovie> = withContext(Dispatchers.IO) {
        with(database.getMovieDao()) {
            if (getCount() <= 0) {
                val movies = MovieFactory.service.listMostPopularMoviesAsync(
                    regionRepository.getCurrentRegion(),
                    languageRepository.getCurrentLanguage()
                ).await().results
                insert(movies.map(ServerMovie::convertToDbMovie))
            }
            getAll()
        }
    }

    suspend fun getById(id: Long): DbMovie = withContext(Dispatchers.IO) {
        database.getMovieDao().getById(id)
    }

    suspend fun update(movie: DbMovie) = withContext(Dispatchers.IO) {
        database.getMovieDao().update(movie)
    }

}

private fun ServerMovie.convertToDbMovie() = DbMovie(
    0,
    posterPath,
    adult,
    overview,
    releaseDate,
    id,
    originalTitle,
    originalLanguage,
    title,
    backdropPath,
    popularity,
    video,
    voteCount,
    voteAverage,
    favorite = false
)