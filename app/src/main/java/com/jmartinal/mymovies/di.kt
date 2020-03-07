package com.jmartinal.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.jmartinal.data.ConnectivityManager
import com.jmartinal.data.PermissionManager
import com.jmartinal.data.repository.LanguageRepository
import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.data.repository.RegionRepository
import com.jmartinal.data.source.LanguageDataSource
import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.data.source.LocationDataSource
import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.mymovies.data.AndroidConnectivityManager
import com.jmartinal.mymovies.data.AndroidPermissionManager
import com.jmartinal.mymovies.data.DeviceLanguageDataSource
import com.jmartinal.mymovies.data.PlayServicesLocationDataSource
import com.jmartinal.mymovies.data.database.MovieDB
import com.jmartinal.mymovies.data.database.RoomDataSource
import com.jmartinal.mymovies.data.server.TheMovieDb
import com.jmartinal.mymovies.data.server.TheMovieDbDataSource
import com.jmartinal.mymovies.ui.detail.MovieDetailActivity
import com.jmartinal.mymovies.ui.detail.MovieDetailViewModel
import com.jmartinal.mymovies.ui.main.MainActivity
import com.jmartinal.mymovies.ui.main.MainViewModel
import com.jmartinal.usecases.GetMovieById
import com.jmartinal.usecases.GetPopularMovies
import com.jmartinal.usecases.ToggleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(
            appModule,
            dataModule,
            scopedModule
        ))
    }
}

val appModule = module {
    factory<AppCompatActivity> { MainActivity() }
    single { Room.databaseBuilder(get(), MovieDB::class.java, "movie-db").build() }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource(get()) }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionManager> { AndroidPermissionManager(get(), get()) }
    factory<LanguageDataSource> { DeviceLanguageDataSource(get()) }
    factory<ConnectivityManager> { AndroidConnectivityManager(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { Constants.TmdbApi.BASE_URL }
    single { TheMovieDb(get(named("baseUrl"))) }
}

val dataModule = module {
    factory{ LanguageRepository(get()) }
    factory{ MoviesRepository(get(), get(), get(), get(),get()) }
    factory{ RegionRepository(get(), get()) }
}

val scopedModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get(), get()) }
        scoped { GetPopularMovies(get()) }
    }
    scope(named<MovieDetailActivity>()) {
        viewModel { (movieId: Long) -> MovieDetailViewModel(movieId, get(), get(), get()) }
        scoped { GetMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
    }
}

