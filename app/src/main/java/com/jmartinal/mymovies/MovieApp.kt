package com.jmartinal.mymovies

import android.app.Application
import androidx.room.Room
import com.jmartinal.mymovies.data.database.MovieDB
import com.jmartinal.mymovies.di.DaggerMyMoviesComponent
import com.jmartinal.mymovies.di.MyMoviesComponent

class MovieApp : Application() {

    lateinit var component: MyMoviesComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerMyMoviesComponent
            .factory()
            .create(this)
    }
}