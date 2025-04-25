package com.brunodegan.ifood_challenge.data.api

import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiService {

    @GET(NOW_PLAYING_URL)
    suspend fun fetchNowPlaying(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataModel

    @GET(POPULAR_URL)
    suspend fun fetchPopular(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataModel

    @GET(TOP_RATED_URL)
    suspend fun fetchTopRated(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataModel

    @GET(UPCOMING_URL)
    suspend fun fetchUpcoming(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataModel

    companion object {
        const val BEARER_TOKEN =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OTljYTc2YjkyNDkxMzE5YWFmMWE3OWI4MzExMDE1MSIsIm5iZiI6MTc0NTM1NTY3NC4zOTEsInN1YiI6IjY4MDgwMzlhMTQyYjA5Y2VjZjg5YjFiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KN5rAk6UlsJJy2bEvjm8uJmLNE7_0ctTG9EbKjTuGP8"

        const val AUTHORIZATION_HEADER = "Authorization"
        const val APPLICATION_JSON = "application/json"
        const val CONTENT_TYPE = "content-type"
        const val ACCEPT = "accept"

        private const val NOW_PLAYING_URL = "now_playing"
        private const val POPULAR_URL = "popular"
        private const val TOP_RATED_URL = "top_rated"
        private const val UPCOMING_URL = "upcoming"
        private const val LANGUAGE = "language"
        private const val PT_BR = "pt-BR"

        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    }
}