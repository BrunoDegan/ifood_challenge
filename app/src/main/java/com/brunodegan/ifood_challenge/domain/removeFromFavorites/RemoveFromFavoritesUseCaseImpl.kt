package com.brunodegan.ifood_challenge.domain.removeFromFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesResponse
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class RemoveFromFavoritesUseCaseImpl(private val repository: MoviesRepository) :
    RemoveFromFavoritesUseCase {
    override suspend fun invoke(id: Int): Flow<Resource<FavoriteMoviesResponse>> =
        repository.removeFavorite(id)
}