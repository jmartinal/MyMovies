package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.domain.Movie

class GetPopularMovies(private val moviesRepository: MoviesRepository) {

    suspend fun invoke(): List<Movie> = moviesRepository.getPopularMovies()

}