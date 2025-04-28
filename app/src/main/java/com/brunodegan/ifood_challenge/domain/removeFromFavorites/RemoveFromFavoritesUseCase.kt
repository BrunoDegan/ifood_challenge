package com.brunodegan.ifood_challenge.domain.removeFromFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import kotlinx.coroutines.flow.Flow

interface RemoveFromFavoritesUseCase {
        suspend operator fun invoke(
            id: Int,
        ): Flow<Resource<AddToFavoriteMoviesData>>
}