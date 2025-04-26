package com.brunodegan.ifood_challenge.data.datasources.remote

import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataResponse

interface RemoteDataSource {
    suspend fun fetchNowPlaying(): MoviesApiDataResponse
    suspend fun fetchPopular(): MoviesApiDataResponse
    suspend fun fetchTopRated(): MoviesApiDataResponse
    suspend fun fetchUpcoming(): MoviesApiDataResponse
    suspend fun addOrRemoveFromFavorites(requestData: AddToFavoritesRequest): AddToFavoritesResponse
    suspend fun getFavorites(): MoviesApiDataResponse
}