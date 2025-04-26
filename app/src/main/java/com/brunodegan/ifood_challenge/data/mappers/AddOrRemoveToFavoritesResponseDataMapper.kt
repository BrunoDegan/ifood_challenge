package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesResponse
import org.koin.core.annotation.Factory

@Factory
class AddOrRemoveToFavoritesResponseDataMapper :
    BaseMapper<AddToFavoritesResponse, FavoriteMoviesResponse> {
    override fun map(input: AddToFavoritesResponse): FavoriteMoviesResponse {
        return FavoriteMoviesResponse(
            success = input.success,
            statusMessage = input.statusMessage,
            statusCode = input.statusCode,
        )
    }
}