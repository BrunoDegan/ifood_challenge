package com.brunodegan.androidplayground.data.datasources.remote

import com.brunodegan.androidplayground.data.datasources.local.entities.AddToFavoritesApiResponse
import com.brunodegan.androidplayground.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.androidplayground.data.datasources.local.entities.MoviesApiDataResponse

interface RemoteDataSource {
    suspend fun fetchNowPlaying(): MoviesApiDataResponse

    suspend fun fetchPopular(): MoviesApiDataResponse

    suspend fun fetchTopRated(): MoviesApiDataResponse

    suspend fun fetchUpcoming(): MoviesApiDataResponse

    suspend fun fetchFavorites(): MoviesApiDataResponse

    suspend fun addOrRemoveFromFavorites(requestData: AddToFavoritesRequest): AddToFavoritesApiResponse
}
