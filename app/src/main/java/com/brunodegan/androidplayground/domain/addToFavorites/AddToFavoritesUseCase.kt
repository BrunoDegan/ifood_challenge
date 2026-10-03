package com.brunodegan.androidplayground.domain.addToFavorites

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.AddToFavoriteMoviesData
import kotlinx.coroutines.flow.Flow

interface AddToFavoritesUseCase {
    suspend operator fun invoke(id: Int): Flow<Resource<AddToFavoriteMoviesData>>
}
