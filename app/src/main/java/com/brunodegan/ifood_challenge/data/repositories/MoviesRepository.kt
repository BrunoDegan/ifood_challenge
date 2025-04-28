package com.brunodegan.ifood_challenge.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getTopRateMovies(): Flow<Resource<List<TopRatedMoviesEntity>>>
    fun getPopularMovies(): Flow<Resource<List<PopularMoviesEntity>>>
    fun getUpcomingMovies(): Flow<Resource<List<UpcomingMoviesEntity>>>
    fun getNowPlayingMovies(): Flow<Resource<List<NowPlayingMoviesEntity>>>
    fun addFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>>
    fun removeFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>>
    fun getFavorites(): Flow<Resource<List<FavoriteMoviesEntity>>>
}