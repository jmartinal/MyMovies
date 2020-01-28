package com.jmartinal.mymovies.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.jmartinal.data.PermissionManager
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
import com.jmartinal.mymovies.data.server.TheMovieDbDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun databaseProvider(app: Application) = Room.databaseBuilder(
        app,
        MovieDB::class.java,
        "movie-db"
    ).build()

    @Provides
    fun localDataSourceProvider(db: MovieDB): LocalDataSource = RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider(): RemoteDataSource = TheMovieDbDataSource()

    @Provides
    fun languageDataSourceProvider(app: Application): LanguageDataSource =
        DeviceLanguageDataSource(app)

    @Provides
    fun locationDataSourceProvider(app: Application): LocationDataSource =
        PlayServicesLocationDataSource(app)

    @Provides
    fun permissionManagerProvider(
        app: Application,
        activity: AppCompatActivity
    ): PermissionManager = AndroidPermissionManager(app, activity)

    @Provides
    fun connectivityManagerProvider(app: Application) = AndroidConnectivityManager(app)

    @Provides
    fun activityProvider() = AppCompatActivity()

}