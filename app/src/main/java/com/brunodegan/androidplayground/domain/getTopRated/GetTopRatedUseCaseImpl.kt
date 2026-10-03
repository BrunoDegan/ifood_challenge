package com.brunodegan.androidplayground.domain.getTopRated

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetTopRatedUseCaseImpl(
    private val repository: MoviesRepository,
) : GetTopRatedUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<TopRatedMoviesEntity>>> = repository.getTopRateMovies()
}
