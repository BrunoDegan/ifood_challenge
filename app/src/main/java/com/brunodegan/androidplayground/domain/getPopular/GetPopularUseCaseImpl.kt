package com.brunodegan.androidplayground.domain.getPopular

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetPopularUseCaseImpl(
    private val repository: MoviesRepository,
) : GetPopularUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<PopularMoviesEntity>>> = repository.getPopularMovies()
}
