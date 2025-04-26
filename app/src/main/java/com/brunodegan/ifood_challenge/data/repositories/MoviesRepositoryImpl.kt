package com.brunodegan.ifood_challenge.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.LocalDataSource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.remote.RemoteDataSource
import com.brunodegan.ifood_challenge.data.mappers.AddOrRemoveToFavoritesResponseDataMapper
import com.brunodegan.ifood_challenge.data.mappers.FavoritesDataMapper
import com.brunodegan.ifood_challenge.data.mappers.NowPlayingDataMapper
import com.brunodegan.ifood_challenge.data.mappers.PopularDataMapper
import com.brunodegan.ifood_challenge.data.mappers.TopRatedDataMapper
import com.brunodegan.ifood_challenge.data.mappers.UpcomingDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    override suspend fun getTopRateMovies(): Flow<Resource<List<TopRatedMoviesEntity>>> = flow {
        val localTopRatedMovies = localDataSource.getTopRated().first()

        if (!localTopRatedMovies.isNullOrEmpty()) {
            emit(Resource.Success(localTopRatedMovies))
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
                emit(Resource.Success(convertedData))
            }
        }
    }

    override suspend fun getPopularMovies(): Flow<Resource<List<PopularMoviesEntity>>> = flow {
        val localPopularMovies = localDataSource.getPopular().first()

        if (!localPopularMovies.isNullOrEmpty()) {
            emit(Resource.Success(localPopularMovies))
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
                emit(Resource.Success(convertedData))
            }
        }
    }

    override suspend fun getUpcomingMovies(): Flow<Resource<List<UpcomingMoviesEntity>>> = flow {
        val localUpcomingMovies = localDataSource.getUpcoming().first()

        if (!localUpcomingMovies.isNullOrEmpty()) {
            emit(Resource.Success(localUpcomingMovies))
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
                emit(Resource.Success(convertedData))
            }
        }
    }

    override suspend fun getNowPlayingMovies(): Flow<Resource<List<NowPlayingMoviesEntity>>> =
        flow {
            val nowPlayingMovies = localDataSource.getNowPlaying().first()

            if (!nowPlayingMovies.isNullOrEmpty()) {
                emit(Resource.Success(nowPlayingMovies))
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
                    emit(Resource.Success(convertedData))
                }
            }
        }

    override suspend fun addFavorite(id: Int): Flow<Resource<FavoriteMoviesResponse>> =
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

    override suspend fun removeFavorite(id: Int): Flow<Resource<FavoriteMoviesResponse>> = flow {
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

    override suspend fun getFavorites(): Flow<Resource<List<FavoriteMoviesEntity>>> = flow {
        val favorites = localDataSource.getFavoriteMovies().first()

        if (!favorites.isNullOrEmpty()) {
            emit(Resource.Success(favorites))
        } else {
            runCatching {
                remoteDataSource.getFavorites()
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
    }
}