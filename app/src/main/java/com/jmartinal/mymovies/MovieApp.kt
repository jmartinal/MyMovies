package com.jmartinal.mymovies

import android.app.Application
import androidx.room.Room
import com.jmartinal.mymovies.data.database.MovieDB
import com.jmartinal.mymovies.di.DaggerMyMoviesComponent
import com.jmartinal.mymovies.di.MyMoviesComponent

class MovieApp : Application() {

    lateinit var database: MovieDB
        private set

    lateinit var component: MyMoviesComponent
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, MovieDB::class.java, "movie-db").build()

        component = DaggerMyMoviesComponent.factory().create(this)
    }
}