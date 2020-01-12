package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository

class GetMovieById(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Long) = moviesRepository.findById(id)
}
