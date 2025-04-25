package com.brunodegan.ifood_challenge.domain.getTopRated

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetTopRatedUseCaseImpl(
    private val repository: MoviesRepository,
) : GetTopRatedUseCase {
    override suspend fun invoke(): Flow<Resource<List<TopRatedMoviesEntity>>> =
        repository.getTopRateMovies()
}