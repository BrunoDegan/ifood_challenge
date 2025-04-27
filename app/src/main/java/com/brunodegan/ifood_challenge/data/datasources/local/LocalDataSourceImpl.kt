package com.brunodegan.ifood_challenge.data.datasources.local

import com.brunodegan.ifood_challenge.data.datasources.local.daos.FavoritesDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.NowPlayingDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.PopularDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.TopRatedDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.UpComingDao
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalDataSourceImpl(
    private val favoriteDao: FavoritesDao,
    private val nowPlayingDao: NowPlayingDao,
    private val topRatedDao: TopRatedDao,
    private val upComingDao: UpComingDao,
    private val popularDao: PopularDao
) : LocalDataSource {
    override fun saveFavorites(favoriteMovie: List<FavoriteMoviesEntity>) =
        favoriteDao.insertFavorite(favoriteMovie)

    override suspend fun getFavoriteMovies(): Flow<List<FavoriteMoviesEntity>> =
        favoriteDao.getFavoriteMovies()

    override suspend fun getNowPlaying(): Flow<List<NowPlayingMoviesEntity>> =
        nowPlayingDao.getAllNowPlaying()

    override suspend fun getPopular(): Flow<List<PopularMoviesEntity>> =
        popularDao.getAllPopular()

    override suspend fun getTopRated(): Flow<List<TopRatedMoviesEntity>> =
        topRatedDao.getAllTopRated()

    override suspend fun getUpcoming(): Flow<List<UpcomingMoviesEntity>> =
        upComingDao.getAllUpcoming()

    override fun saveNowPlaying(nowPlayingData: List<NowPlayingMoviesEntity>) {
        nowPlayingDao.insertNowPlayingMovies(nowPlayingData)
    }

    override fun savePopular(popularData: List<PopularMoviesEntity>) {
        popularDao.insertPopularMovies(popularData)
    }

    override fun saveTopRated(topRatedData: List<TopRatedMoviesEntity>) {
        topRatedDao.insertTopRatedMovies(topRatedData)
    }

    override fun saveUpcoming(upcomingData: List<UpcomingMoviesEntity>) {
        upComingDao.insertUpcomingMovies(upcomingData)
    }
}