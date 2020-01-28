package com.jmartinal.mymovies.di

import com.jmartinal.data.PermissionManager
import com.jmartinal.data.repository.LanguageRepository
import com.jmartinal.data.repository.MoviesRepository
import com.jmartinal.data.repository.RegionRepository
import com.jmartinal.data.source.LanguageDataSource
import com.jmartinal.data.source.LocalDataSource
import com.jmartinal.data.source.LocationDataSource
import com.jmartinal.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun regionRepositoryProvider(
        locationDataSource: LocationDataSource,
        permissionManager: PermissionManager
    ) = RegionRepository(locationDataSource, permissionManager)

    @Provides
    fun languageRepositoryProvider(languageDataSource: LanguageDataSource) =
        LanguageRepository(languageDataSource)

    @Provides
    fun moviesRepositoryProvider(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        regionRepository: RegionRepository,
        languageRepository: LanguageRepository
    ) = MoviesRepository(localDataSource, remoteDataSource, regionRepository, languageRepository)
}