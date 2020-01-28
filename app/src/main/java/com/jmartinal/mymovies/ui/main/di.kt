package com.jmartinal.mymovies.ui.main

import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.mymovies.data.AndroidConnectivityManager
import com.jmartinal.usecases.GetPopularMovies
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun mainViewModelProvider(getPopularMovies: GetPopularMovies, netWorkManager: AndroidConnectivityManager) = MainViewModel(getPopularMovies, netWorkManager)

    @Provides
    fun getPopularMoviesProvider(moviesRepository: MoviesRepository) = GetPopularMovies(moviesRepository)

}

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}