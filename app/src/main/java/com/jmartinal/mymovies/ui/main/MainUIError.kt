package com.jmartinal.mymovies.ui.main

sealed class MainUIError {
    object GenericError : MainUIError()
    object NetworkError : MainUIError()
}