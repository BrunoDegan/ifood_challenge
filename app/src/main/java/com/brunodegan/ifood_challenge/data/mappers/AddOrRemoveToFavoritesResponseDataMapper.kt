package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoritesApiResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import org.koin.core.annotation.Factory

@Factory
class AddOrRemoveToFavoritesResponseDataMapper :
    BaseMapper<AddToFavoritesApiResponse, AddToFavoriteMoviesData> {
    override fun map(input: AddToFavoritesApiResponse): AddToFavoriteMoviesData {
        return AddToFavoriteMoviesData(
            success = input.success,
            statusMessage = input.statusMessage,
            statusCode = input.statusCode,
        )
    }
}