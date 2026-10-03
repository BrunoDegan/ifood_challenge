package com.brunodegan.androidplayground.domain.getFavorites

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.FavoriteMoviesEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    suspend operator fun invoke(): Flow<Resource<ImmutableList<FavoriteMoviesEntity>>>
}
