package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie) = moviesRepository.update(movie)
}
