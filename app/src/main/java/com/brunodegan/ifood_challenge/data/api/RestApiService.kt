package com.brunodegan.ifood_challenge.data.api

import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {

    @GET(NOW_PLAYING_URL)
    suspend fun fetchNowPlaying(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataResponse

    @GET(POPULAR_URL)
    suspend fun fetchPopular(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataResponse

    @GET(TOP_RATED_URL)
    suspend fun fetchTopRated(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataResponse

    @GET(UPCOMING_URL)
    suspend fun fetchUpcoming(
        @Query(LANGUAGE) language: String = PT_BR
    ): MoviesApiDataResponse

    @POST(ADD_TO_FAVORITES_URL)
    suspend fun addToFavorites(
        @Path(ACCOUNT_ID) accountId: String = ID,
        @Body addToFavoritesRequest: AddToFavoritesRequest
    ): AddToFavoritesResponse

    @GET(GET_FAVORITES_URL)
    suspend fun getFavorites(
        @Path(ACCOUNT_ID) accountId: String = ID,
    ): MoviesApiDataResponse

    companion object {
        const val ID = "21965972"
        const val BEARER_TOKEN =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OTljYTc2YjkyNDkxMzE5YWFmMWE3OWI4MzExMDE1MSIsIm5iZiI6MTc0NTM1NTY3NC4zOTEsInN1YiI6IjY4MDgwMzlhMTQyYjA5Y2VjZjg5YjFiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KN5rAk6UlsJJy2bEvjm8uJmLNE7_0ctTG9EbKjTuGP8"

        const val AUTHORIZATION_HEADER = "Authorization"
        const val APPLICATION_JSON = "application/json"
        const val CONTENT_TYPE = "content-type"
        const val ACCEPT = "accept"

        internal const val MEDIA_TYPE = "movie"
        private const val NOW_PLAYING_URL = "movie/now_playing"
        private const val POPULAR_URL = "movie/popular"
        private const val TOP_RATED_URL = "movie/top_rated"
        private const val UPCOMING_URL = "movie/upcoming"
        private const val ADD_TO_FAVORITES_URL = "account/{account_id}/favorite"
        private const val GET_FAVORITES_URL = "account/{account_id}/favorite/movies"
        private const val ACCOUNT_ID = "account_id"
        private const val LANGUAGE = "language"
        private const val PT_BR = "pt-BR"

        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}