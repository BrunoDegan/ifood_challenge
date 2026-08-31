package com.brunodegan.ifood_challenge.domain.getUpComing

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetUpComingUseCaseImpl(
    private val repository: MoviesRepository,
) : GetUpComingUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<UpcomingMoviesEntity>>> = repository.getUpcomingMovies()
}
