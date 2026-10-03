package com.brunodegan.androidplayground.data.mappers

import com.brunodegan.androidplayground.base.utils.formatFullCDNUrl
import com.brunodegan.androidplayground.base.utils.formatUsDateToBrDate
import com.brunodegan.androidplayground.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.androidplayground.data.datasources.local.entities.MoviesApiDataResponse
import org.koin.core.annotation.Factory
import kotlin.random.Random

@Factory
class FavoritesDataMapper : BaseMapper<MoviesApiDataResponse, List<FavoriteMoviesEntity>> {
    override fun map(input: MoviesApiDataResponse): List<FavoriteMoviesEntity> =
        buildList {
            input.results.forEach { movie ->
                add(
                    FavoriteMoviesEntity(
                        id = movie.id ?: Random.nextInt(),
                        lastUpdated = System.currentTimeMillis(),
                        title = movie.title.orEmpty(),
                        posterPath = movie.posterPath.formatFullCDNUrl(),
                        overview = movie.overview.orEmpty(),
                        releaseDate = movie.releaseDate.formatUsDateToBrDate(),
                    ),
                )
            }
        }
}
