package com.jmartinal.mymovies.data.server

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {
    @GET("movie/now_playing")
    fun listMostPopularMoviesAsync(@Query("region") region: String, @Query("language") language: String): Deferred<TmdbMovieResult>
}