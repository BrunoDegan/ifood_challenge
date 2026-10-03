package com.brunodegan.androidplayground.domain.getNowPlaying

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetNowPlayingUseCaseImpl(
    private val repository: MoviesRepository,
) : GetNowPlayingUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<NowPlayingMoviesEntity>>> = repository.getNowPlayingMovies()
}
