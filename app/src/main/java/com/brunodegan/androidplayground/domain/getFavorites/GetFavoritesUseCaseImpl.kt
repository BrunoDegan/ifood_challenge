package com.brunodegan.androidplayground.domain.getFavorites

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetFavoritesUseCaseImpl(
    private val repository: MoviesRepository,
) : GetFavoritesUseCase {
    override suspend fun invoke(): Flow<Resource<ImmutableList<FavoriteMoviesEntity>>> = repository.getFavorites()
}
