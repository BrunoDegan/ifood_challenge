package com.brunodegan.ifood_challenge.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.utils.isCacheValid
import com.brunodegan.ifood_challenge.data.datasources.local.LocalDataSource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.update
import com.brunodegan.ifood_challenge.data.datasources.remote.RemoteDataSource
import com.brunodegan.ifood_challenge.data.mappers.AddOrRemoveToFavoritesResponseDataMapper
import com.brunodegan.ifood_challenge.data.mappers.FavoritesDataMapper
import com.brunodegan.ifood_challenge.data.mappers.NowPlayingDataMapper
import com.brunodegan.ifood_challenge.data.mappers.PopularDataMapper
import com.brunodegan.ifood_challenge.data.mappers.TopRatedDataMapper
import com.brunodegan.ifood_challenge.data.mappers.UpcomingDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
class MoviesRepositoryImpl(
    private val addOrRemoveToFavoritesResponseDataMapper: AddOrRemoveToFavoritesResponseDataMapper,
    private val favoritesDataMapper: FavoritesDataMapper,
    private val nowPlayingMoviesDataMapper: NowPlayingDataMapper,
    private val popularMoviesDataMapper: PopularDataMapper,
    private val topRatedMoviesDataMapper: TopRatedDataMapper,
    private val upcomingMoviesDataMapper: UpcomingDataMapper,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : MoviesRepository {

    private fun updateTopRatedMovies(
        movies: List<TopRatedMoviesEntity>,
        favorites: List<FavoriteMoviesEntity>?
    ): List<TopRatedMoviesEntity> {
        return favorites?.let { movies.update(it) } ?: movies
    }

    private fun updatePopularMovies(
        movies: List<PopularMoviesEntity>,
        favorites: List<FavoriteMoviesEntity>?
    ): List<PopularMoviesEntity> {
        return favorites?.let { movies.update(it) } ?: movies
    }

    private fun updateUpcomingMovies(
        movies: List<UpcomingMoviesEntity>,
        favorites: List<FavoriteMoviesEntity>?
    ): List<UpcomingMoviesEntity> {
        return favorites?.let { movies.update(it) } ?: movies
    }

    private fun updateNowPlayingMovies(
        movies: List<NowPlayingMoviesEntity>,
        favorites: List<FavoriteMoviesEntity>?
    ): List<NowPlayingMoviesEntity> {
        return favorites?.let { movies.update(it) } ?: movies
    }

    override fun getTopRateMovies(): Flow<Resource<List<TopRatedMoviesEntity>>> = flow {

        val localTopRatedMovies = localDataSource.getTopRated().firstOrNull()
        val savedFavoriteMovies = getSavedFavoriteMovies().firstOrNull()

        if (!localTopRatedMovies.isNullOrEmpty()) {
            emit(
                Resource.Success(
                    updateTopRatedMovies(
                        localTopRatedMovies,
                        savedFavoriteMovies
                    )
                )
            )
        } else {
            runCatching {
                remoteDataSource.fetchTopRated()
            }.onFailure { error ->
                emit(Resource.Error(ErrorType.Generic(error.message)))
            }.map { apiDataModel ->
                val mappedResult = topRatedMoviesDataMapper.map(apiDataModel)
                localDataSource.saveTopRated(mappedResult)
                mappedResult
            }.onSuccess { convertedData ->
                emit(
                    Resource.Success(
                        updateTopRatedMovies(
                            convertedData,
                            savedFavoriteMovies
                        )
                    )
                )
            }
        }

    }

    override fun getPopularMovies(): Flow<Resource<List<PopularMoviesEntity>>> = flow {
        val localPopularMovies = localDataSource.getPopular().firstOrNull()
        val savedFavoriteMovies = getSavedFavoriteMovies().firstOrNull()

        if (!localPopularMovies.isNullOrEmpty()) {
            emit(
                Resource.Success(
                    updatePopularMovies(
                        localPopularMovies,
                        savedFavoriteMovies
                    )
                )
            )
        } else {
            runCatching {
                remoteDataSource.fetchPopular()
            }.onFailure { error ->
                emit(Resource.Error(ErrorType.Generic(error.message)))
            }.map { apiDataModel ->
                val mappedResult = popularMoviesDataMapper.map(apiDataModel)
                localDataSource.savePopular(mappedResult)
                mappedResult
            }.onSuccess { convertedData ->
                emit(
                    Resource.Success(
                        updatePopularMovies(
                            convertedData,
                            savedFavoriteMovies
                        )
                    )
                )
            }
        }
    }

    override fun getUpcomingMovies(): Flow<Resource<List<UpcomingMoviesEntity>>> = flow {
        val localUpcomingMovies = localDataSource.getUpcoming().firstOrNull()
        val savedFavoriteMovies = getSavedFavoriteMovies().firstOrNull()

        if (!localUpcomingMovies.isNullOrEmpty()) {
            emit(
                Resource.Success(
                    updateUpcomingMovies(
                        localUpcomingMovies,
                        savedFavoriteMovies
                    )
                )
            )
        } else {
            runCatching {
                remoteDataSource.fetchUpcoming()
            }.onFailure { error ->
                emit(Resource.Error(ErrorType.Generic(error.message)))
            }.map { apiDataModel ->
                val mappedResult = upcomingMoviesDataMapper.map(apiDataModel)
                localDataSource.saveUpcoming(mappedResult)
                mappedResult
            }.onSuccess { convertedData ->
                emit(
                    Resource.Success(
                        updateUpcomingMovies(
                            convertedData,
                            savedFavoriteMovies
                        )
                    )
                )
            }
        }
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<NowPlayingMoviesEntity>>> = flow {
        val nowPlayingMovies = localDataSource.getNowPlaying().firstOrNull()
        val savedFavoriteMovies = getSavedFavoriteMovies().firstOrNull()

        if (!nowPlayingMovies.isNullOrEmpty()) {
            emit(
                Resource.Success(
                    updateNowPlayingMovies(
                        nowPlayingMovies,
                        savedFavoriteMovies
                    )
                )
            )
        } else {
            runCatching {
                remoteDataSource.fetchNowPlaying()
            }.onFailure { error ->
                emit(Resource.Error(ErrorType.Generic(error.message)))
            }.map { apiDataModel ->
                val mappedResult = nowPlayingMoviesDataMapper.map(apiDataModel)
                localDataSource.saveNowPlaying(mappedResult)
                mappedResult
            }.onSuccess { convertedData ->
                emit(
                    Resource.Success(
                        updateNowPlayingMovies(
                            convertedData,
                            savedFavoriteMovies
                        )
                    )
                )
            }
        }
    }

    override fun addFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>> =
        flow {
            runCatching {
                val request = AddToFavoritesRequest(mediaId = id, favorite = true)
                remoteDataSource.addOrRemoveFromFavorites(request)
            }.onFailure { error ->
                emit(Resource.Error(ErrorType.Generic(error.message)))
            }.map { apiDataModel ->
                val mappedResult = addOrRemoveToFavoritesResponseDataMapper.map(apiDataModel)
                mappedResult
            }.onSuccess { convertedData ->
                emit(Resource.Success(convertedData))
            }
        }

    override fun removeFavorite(id: Int): Flow<Resource<AddToFavoriteMoviesData>> = flow {
        runCatching {
            val request = AddToFavoritesRequest(mediaId = id, favorite = false)
            remoteDataSource.addOrRemoveFromFavorites(request)
        }.onFailure { error ->
            emit(Resource.Error(ErrorType.Generic(error.message)))
        }.map { apiDataModel ->
            val mappedResult = addOrRemoveToFavoritesResponseDataMapper.map(apiDataModel)
            mappedResult
        }.onSuccess { convertedData ->
            emit(Resource.Success(convertedData))
        }
    }

    override fun getFavorites(): Flow<Resource<List<FavoriteMoviesEntity>>> = flow {
        val favorites = localDataSource.getFavoriteMovies().first()

        if (!favorites.isNullOrEmpty() && favorites.all { isCacheValid(it.lastUpdated) }) {
            emit(Resource.Success(favorites))
            return@flow
        }

        runCatching {
            remoteDataSource.fetchFavorites()
        }.onFailure { error ->
            emit(Resource.Error(ErrorType.Generic(error.message)))
        }.map { apiDataModel ->
            val mappedResult = favoritesDataMapper.map(apiDataModel)
            localDataSource.saveFavorites(mappedResult)
            mappedResult
        }.onSuccess { convertedData ->
            emit(Resource.Success(convertedData))
        }
    }

    private fun getSavedFavoriteMovies(): Flow<List<FavoriteMoviesEntity>?> = flow {
        val favorites = localDataSource.getFavoriteMovies().first()

        if (!favorites.isNullOrEmpty()) {
            emit(favorites)
            return@flow
        }

        runCatching {
            remoteDataSource.fetchFavorites()
        }.onFailure {
            emit(null)
        }.map { apiDataModel ->
            val mappedResult = favoritesDataMapper.map(apiDataModel)
            localDataSource.saveFavorites(mappedResult)
            mappedResult
        }.onSuccess { convertedData ->
            emit(convertedData)
        }
    }
}