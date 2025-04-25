package com.brunodegan.ifood_challenge.data.datasources.local

import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getNowPlaying(): Flow<List<NowPlayingMoviesEntity>?>
    suspend fun getPopular(): Flow<List<PopularMoviesEntity>?>
    suspend fun getTopRated(): Flow<List<TopRatedMoviesEntity>?>
    suspend fun getUpcoming(): Flow<List<UpcomingMoviesEntity>?>
    fun saveNowPlaying(nowPlayingData: List<NowPlayingMoviesEntity>)
    fun savePopular(popularData: List<PopularMoviesEntity>)
    fun saveTopRated(topRatedData: List<TopRatedMoviesEntity>)
    fun saveUpcoming(upcomingData: List<UpcomingMoviesEntity>)
}