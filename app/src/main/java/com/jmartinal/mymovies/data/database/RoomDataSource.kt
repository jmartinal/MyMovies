package com.jmartinal.mymovies.data.database

import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.data.toDomainMovie
import com.jmartinal.mymovies.data.toRoomMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: MovieDB) : LocalDataSource {

    private val movieDao = db.getMovieDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { movieDao.getCount() <= 0 }

    override suspend fun getMovieById(id: Long): Movie =
        withContext(Dispatchers.IO) { movieDao.getById(id).toDomainMovie() }

    override suspend fun getPopularMovies(): List<Movie> =
        withContext(Dispatchers.IO) { movieDao.getAll().map { it.toDomainMovie() } }

    override suspend fun saveMovies(movies: List<Movie>) {
        withContext(Dispatchers.IO) { movieDao.insert(movies.map { it.toRoomMovie() }) }
    }

    override suspend fun updateMovie(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.update(movie.toRoomMovie()) }
    }

}
