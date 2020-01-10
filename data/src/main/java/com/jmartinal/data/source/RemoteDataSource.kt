package com.jmartinal.data.source

import com.jmartinal.domain.Movie

interface RemoteDataSource {
    suspend fun getPopularMovies(region: String, language: String): List<Movie>
}