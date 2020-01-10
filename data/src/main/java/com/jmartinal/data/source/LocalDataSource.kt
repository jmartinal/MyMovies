package com.jmartinal.data.source

import com.jmartinal.domain.Movie

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun getMovieById(id: Long): Movie
    suspend fun getPopularMovies(): List<Movie>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun updateMovie(movie: Movie)
}