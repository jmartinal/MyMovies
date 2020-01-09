package com.jmartinal.data.source

import com.jmartinal.domain.Movie

interface RemoteDataSource {
    fun getPopularMovies(region: String, language: String): List<Movie>
}