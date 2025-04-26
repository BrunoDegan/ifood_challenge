package com.brunodegan.ifood_challenge.domain.addToFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesResponse
import kotlinx.coroutines.flow.Flow

interface AddToFavoritesUseCase {
    suspend operator fun invoke(
        id: Int,
    ): Flow<Resource<FavoriteMoviesResponse>>
}