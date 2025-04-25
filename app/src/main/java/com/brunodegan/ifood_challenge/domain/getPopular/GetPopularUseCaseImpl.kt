package com.brunodegan.ifood_challenge.domain.getPopular

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetPopularUseCaseImpl(
    private val repository: MoviesRepository,
) : GetPopularUseCase {
    override suspend fun invoke(): Flow<Resource<List<PopularMoviesEntity>>> =
        repository.getPopularMovies()
}