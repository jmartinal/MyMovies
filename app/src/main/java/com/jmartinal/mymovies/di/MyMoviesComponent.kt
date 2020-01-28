package com.jmartinal.mymovies.di

import android.app.Application
import com.jmartinal.mymovies.ui.detail.DetailActivityComponent
import com.jmartinal.mymovies.ui.detail.DetailActivityModule
import com.jmartinal.mymovies.ui.main.MainActivityComponent
import com.jmartinal.mymovies.ui.main.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface MyMoviesComponent {

    fun plus(module: MainActivityModule): MainActivityComponent
    fun plus(module: DetailActivityModule): DetailActivityComponent

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance app: Application): MyMoviesComponent
    }

}