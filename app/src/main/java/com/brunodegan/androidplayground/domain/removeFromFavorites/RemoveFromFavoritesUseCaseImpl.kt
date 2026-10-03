package com.brunodegan.androidplayground.domain.removeFromFavorites

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.androidplayground.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class RemoveFromFavoritesUseCaseImpl(
    private val repository: MoviesRepository,
) : RemoveFromFavoritesUseCase {
    override suspend fun invoke(id: Int): Flow<Resource<AddToFavoriteMoviesData>> = repository.removeFavorite(id)
}
