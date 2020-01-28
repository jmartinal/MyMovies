package com.jmartinal.mymovies

import android.app.Application

class MovieApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDI()
    }
}