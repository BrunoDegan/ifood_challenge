package com.brunodegan.ifood_challenge.data.datasources.remote

import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataModel

interface RemoteDataSource {
    suspend fun fetchNowPlaying(): MoviesApiDataModel
    suspend fun fetchPopular(): MoviesApiDataModel
    suspend fun fetchTopRated(): MoviesApiDataModel
    suspend fun fetchUpcoming(): MoviesApiDataModel
}