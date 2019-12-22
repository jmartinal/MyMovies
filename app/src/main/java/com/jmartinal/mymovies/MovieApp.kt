package com.jmartinal.mymovies

import android.app.Application
import androidx.room.Room
import com.jmartinal.mymovies.model.database.MovieDB

class MovieApp : Application() {

    lateinit var database: MovieDB
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, MovieDB::class.java, "movie-db").build()
    }
}