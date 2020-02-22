package com.jmartinal.usecases

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie): Movie = with(movie) {
        movie.copy(favorite = !favorite).also { moviesRepository.update(it) }
    }
}
