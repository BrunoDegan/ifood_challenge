package com.brunodegan.ifood_challenge.domain.getFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetFavoritesUseCaseImpl(
    private val repository: MoviesRepository,
) : GetFavoritesUseCase {
    override suspend fun invoke(): Flow<Resource<List<FavoriteMoviesEntity>>> =
        repository.getFavorites()
}