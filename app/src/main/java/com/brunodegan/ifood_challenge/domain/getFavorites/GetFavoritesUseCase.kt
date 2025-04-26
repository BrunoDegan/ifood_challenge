package com.brunodegan.ifood_challenge.domain.getFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    suspend operator fun invoke(): Flow<Resource<List<FavoriteMoviesEntity>>>
}
