package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.base.utils.formatFullCDNUrl
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataResponse
import org.koin.core.annotation.Factory

@Factory
class FavoritesDataMapper : BaseMapper<MoviesApiDataResponse, List<FavoriteMoviesEntity>> {
    override fun map(input: MoviesApiDataResponse): List<FavoriteMoviesEntity> {
        return buildList {
            input.results.forEach { movie ->
                add(
                    FavoriteMoviesEntity(
                        id = movie.id ?: 0,
                        title = movie.title.orEmpty(),
                        posterPath = movie.posterPath.formatFullCDNUrl(),
                        overview = movie.overview.orEmpty(),
                        releaseDate = movie.releaseDate.formatUsDateToBrDate()
                    )
                )
            }
        }
    }
}