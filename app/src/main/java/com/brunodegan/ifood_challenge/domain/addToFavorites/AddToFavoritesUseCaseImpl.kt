package com.brunodegan.ifood_challenge.domain.addToFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesResponse
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class AddToFavoritesUseCaseImpl(private val repository: MoviesRepository) : AddToFavoritesUseCase {
    override suspend fun invoke(id: Int): Flow<Resource<FavoriteMoviesResponse>> =
        repository.addFavorite(id = id)
}