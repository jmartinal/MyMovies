package com.jmartinal.mymovies.ui.main

sealed class MainUIError {
    object NetworkError : MainUIError()
}