package com.jmartinal.mymovies.model.server

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jmartinal.mymovies.BuildConfig
import com.jmartinal.mymovies.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieFactory {

    private val tmdbInterceptor = Interceptor { chain ->
        val newUrl =
            chain.request().url()
                .newBuilder()
                .addQueryParameter("api_key", Constants.TmdbApi.API_KEY)
                .build()


        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    val client = OkHttpClient().newBuilder()
        .addInterceptor(tmdbInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    val service: TmdbService = Retrofit.Builder()
        .baseUrl(Constants.TmdbApi.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .run {
            create(TmdbService::class.java)
        }
}