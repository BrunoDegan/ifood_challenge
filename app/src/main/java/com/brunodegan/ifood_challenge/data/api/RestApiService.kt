package com.brunodegan.ifood_challenge.data.api

import com.brunodegan.ifood_challenge.BuildConfig
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesApiResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApiService {
    @GET(NOW_PLAYING_URL)
    suspend fun fetchNowPlaying(
        @Query(LANGUAGE) language: String = PT_BR,
    ): MoviesApiDataResponse

    @GET(POPULAR_URL)
    suspend fun fetchPopular(
        @Query(LANGUAGE) language: String = PT_BR,
    ): MoviesApiDataResponse

    @GET(TOP_RATED_URL)
    suspend fun fetchTopRated(
        @Query(LANGUAGE) language: String = PT_BR,
    ): MoviesApiDataResponse

    @GET(UPCOMING_URL)
    suspend fun fetchUpcoming(
        @Query(LANGUAGE) language: String = PT_BR,
    ): MoviesApiDataResponse

    @POST(ADD_TO_FAVORITES_URL)
    suspend fun addToFavorites(
        @Path(ACCOUNT_ID) accountId: String = BuildConfig.TMDB_ACCOUNT_ID,
        @Body addToFavoritesRequest: AddToFavoritesRequest,
    ): AddToFavoritesApiResponse

    @GET(GET_FAVORITES_URL)
    suspend fun getFavorites(
        @Path(ACCOUNT_ID) accountId: String = BuildConfig.TMDB_ACCOUNT_ID,
    ): MoviesApiDataResponse

    companion object {
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
