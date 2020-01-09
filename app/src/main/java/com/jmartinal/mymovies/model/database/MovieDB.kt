package com.jmartinal.mymovies.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}