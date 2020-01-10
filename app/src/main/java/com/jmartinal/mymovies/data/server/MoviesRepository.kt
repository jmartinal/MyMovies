package com.jmartinal.mymovies.data.server

import android.content.Context
import com.jmartinal.mymovies.Constants
import com.jmartinal.mymovies.MovieApp
import com.jmartinal.mymovies.data.LanguageRepository
import com.jmartinal.mymovies.data.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import com.jmartinal.mymovies.data.database.Movie as DbMovie
import com.jmartinal.mymovies.data.server.Movie as ServerMovie

class MoviesRepository(application: MovieApp) {

    private val regionRepository = RegionRepository(application)
    private val languageRepository = LanguageRepository(application)
    private val database = application.database
    private val preferences =
        application.getSharedPreferences(Constants.Preferences.PREF_FILE_NAME, Context.MODE_PRIVATE)
    private val lastUpdate = preferences.getLong(Constants.Preferences.BD_LAST_UPDATE, 0)

    suspend fun findPopularMovies(): List<DbMovie> = withContext(Dispatchers.IO) {
        with(database.getMovieDao()) {
            val yesterdayDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -1)
            }.timeInMillis
            val needToUpdate: Boolean = lastUpdate <= yesterdayDate
            val isEmptyBD: Boolean = getCount() <= 0

            if (isEmptyBD || needToUpdate) {
                val movies = MovieFactory.service.listMostPopularMoviesAsync(
                    regionRepository.getCurrentRegion(),
                    languageRepository.getCurrentLanguage()
                ).await().results
                insert(movies.map(ServerMovie::convertToDbMovie))
                with(preferences.edit()) {
                    putLong(
                        Constants.Preferences.BD_LAST_UPDATE,
                        Calendar.getInstance().timeInMillis
                    )
                    commit()
                }
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
    id,
    adult,
    backdropPath,
    favorite = false,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)