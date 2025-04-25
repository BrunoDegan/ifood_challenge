package com.brunodegan.ifood_challenge.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getTopRateMovies(): Flow<Resource<List<TopRatedMoviesEntity>>>
    suspend fun getPopularMovies(): Flow<Resource<List<PopularMoviesEntity>>>
    suspend fun getUpcomingMovies(): Flow<Resource<List<UpcomingMoviesEntity>>>
    suspend fun getNowPlayingMovies(): Flow<Resource<List<NowPlayingMoviesEntity>>>
}