package com.jmartinal.mymovies.model.database

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM Movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun getById(id: Long): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: List<Movie>)

    @Update
    fun update(movie: Movie)
}