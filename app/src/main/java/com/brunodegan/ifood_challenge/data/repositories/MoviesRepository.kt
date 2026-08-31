package com.brunodegan.ifood_challenge.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getTopRateMovies(): Flow<Resource<ImmutableList<TopRatedMoviesEntity>>>

    fun getPopularMovies(): Flow<Resource<ImmutableList<PopularMoviesEntity>>>

    fun getUpcomingMovies(): Flow<Resource<ImmutableList<UpcomingMoviesEntity>>>

    fun getNowPlayingMovies(): Flow<Resource<ImmutableList<NowPlayingMoviesEntity>>>

    fun addFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>>

    fun removeFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>>

    fun getFavorites(): Flow<Resource<ImmutableList<FavoriteMoviesEntity>>>
}
