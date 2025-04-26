package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesResponse
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.model.FavoriteMoviesViewData
import org.koin.core.annotation.Factory

@Factory
class AddOrRemoveToFavoritesResponseDataMapper :
    BaseMapper<AddToFavoritesResponse, FavoriteMoviesViewData> {
    override fun map(input: AddToFavoritesResponse): FavoriteMoviesViewData {
        return FavoriteMoviesViewData(
            success = input.success,
            statusMessage = input.statusMessage,
            statusCode = input.statusCode,
        )
    }
}