package com.jmartinal.mymovies.ui.detail

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.usecases.GetMovieById
import com.jmartinal.usecases.ToggleMovieFavorite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class DetailActivityModule(private val movieId: Long) {

    @Provides
    fun movieDetailViewModelProvider(
        getMovieById: GetMovieById,
        toggleMovieFavorite: ToggleMovieFavorite
    ) = MovieDetailViewModel(movieId, getMovieById, toggleMovieFavorite)

    @Provides
    fun getMovieByIdProvider(moviesRepository: MoviesRepository) = GetMovieById(moviesRepository)

    @Provides
    fun toggleMovieFavoriteProvider(moviesRepository: MoviesRepository) =
        ToggleMovieFavorite(moviesRepository)

}

@Subcomponent(modules = [DetailActivityModule::class])
interface DetailActivityComponent {
    val detailViewModel: MovieDetailViewModel
}