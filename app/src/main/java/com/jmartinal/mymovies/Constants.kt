package com.jmartinal.mymovies

class Constants {
    class Communication {
        companion object {
            const val KEY_MOVIE = "movie"
        }
    }

    class TmdbApi {
        companion object {
            const val API_KEY = "a3280f1ae3f9bdab003ab1f6a35197d4"
            const val BASE_URL = "https://api.themoviedb.org/3/"
            const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185/"
            const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/original/"
        }
    }

    companion object {
        const val DEFAULT_REGION = "US"
    }
}