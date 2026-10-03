package com.brunodegan.androidplayground.domain.getUpComing

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetUpComingUseCaseImpl(
    private val repository: MoviesRepository,
) : GetUpComingUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<UpcomingMoviesEntity>>> = repository.getUpcomingMovies()
}
