package com.jmartinal.data.source

import com.jmartinal.domain.Movie

interface LocalDataSource {
    fun isEmpty(): Boolean
    fun getPopularMovies(): List<Movie>
    fun saveMovies(movies: List<Movie>)
}