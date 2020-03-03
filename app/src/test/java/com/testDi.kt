package com.jmartinal.mymovies.ui

import com.jmartinal.data.ConnectivityManager
import com.jmartinal.data.PermissionManager
import com.jmartinal.data.source.LanguageDataSource
import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.data.source.LocationDataSource
import com.jmartinal.data.source.RemoteDataSource
import com.jmartinal.domain.Movie
import com.jmartinal.mymovies.dataModule
import com.jmartinal.testshared.mockedMovie
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}


val fakeMovies = listOf(
    mockedMovie.copy(id = 1),
    mockedMovie.copy(id = 2),
    mockedMovie.copy(id = 3),
    mockedMovie.copy(id = 4),
    mockedMovie.copy(id = 5)
)

private val mockedAppModule = module {
    single<LocalDataSource> { FakeLocalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<PermissionManager> { FakePermissionManager() }
    single<LanguageDataSource> { FakeLanguageDataSource() }
    single<ConnectivityManager> { FakeConnectivityManager() }
    single { Dispatchers.Unconfined }
}

class FakeLocalDataSource : LocalDataSource {

    var movies: List<Movie> = fakeMovies

    override suspend fun isEmpty(): Boolean = movies.isEmpty()

    override suspend fun getMovieById(id: Long) = movies.first { it.id == id }

    override suspend fun getPopularMovies(): List<Movie> = movies

    override suspend fun saveMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override suspend fun updateMovie(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }

}

class FakeRemoteDataSource : RemoteDataSource {

    var movies = fakeMovies

    override suspend fun getPopularMovies(region: String, language: String) = movies

}

class FakeLocationDataSource : LocationDataSource {
    var currentRegion = "ES"
    override suspend fun getCurrentRegion(): String? = currentRegion

}

class FakePermissionManager : PermissionManager {
    var permissionGranted = true

    override suspend fun checkPermissions(permissions: List<PermissionManager.Permission>): Boolean =
        permissionGranted

    override fun requestPermission(permission: String, continuation: (Boolean) -> Unit) {
        continuation(permissionGranted)
    }

}

class FakeLanguageDataSource : LanguageDataSource {
    override fun getCurrentLanguage() = "ES"

}

class FakeConnectivityManager : ConnectivityManager {
    var connectivityAvailable = true
    override fun isConnected() = connectivityAvailable

}