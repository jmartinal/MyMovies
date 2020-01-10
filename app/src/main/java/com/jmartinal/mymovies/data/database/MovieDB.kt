package com.jmartinal.mymovies.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jmartinal.mymovies.data.database.Movie
import com.jmartinal.mymovies.data.database.MovieDao

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}