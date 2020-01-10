package com.jmartinal.mymovies.data

import com.jmartinal.mymovies.data.server.Movie as ServerMovie
import com.jmartinal.domain.Movie as DomainMovie
import com.jmartinal.mymovies.data.database.Movie as RoomMovie

fun RoomMovie.toDomainMovie(): DomainMovie = DomainMovie(
    id,
    adult,
    backdropPath,
    favorite,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)

fun DomainMovie.toRoomMovie(): RoomMovie = RoomMovie(
    id,
    adult,
    backdropPath,
    favorite,
    originalLanguage,
    originalTitle,
    overview,
    popularity,
    posterPath,
    releaseDate,
    title,
    video,
    voteAverage,
    voteCount
)

fun ServerMovie.toDomainMovie(): DomainMovie {
    val favorite = false
    return DomainMovie(
        id,
        adult,
        backdropPath,
        favorite,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        title,
        video,
        voteAverage,
        voteCount
    )
}