package com.jmartinal.mymovies.model

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface TmdbService {
    @GET("discover/movie?sort_by=popularity.desc")
    fun listMostPopularMoviesAsync(): Deferred<TmdbMovieResult>
}