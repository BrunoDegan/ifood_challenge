package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.base.utils.formatFullCDNUrl
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.base.utils.orZero
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataResponse
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import org.koin.core.annotation.Factory
import kotlin.random.Random

@Factory
class PopularDataMapper: BaseMapper<MoviesApiDataResponse, List<PopularMoviesEntity>> {
    override fun map(input: MoviesApiDataResponse): List<PopularMoviesEntity> {
        return buildList {
            input.results.forEach { movie ->
                add(
                    PopularMoviesEntity(
                        id = movie.id ?: Random.nextInt(),
                        title = movie.title.orEmpty(),
                        posterPath = movie.posterPath.formatFullCDNUrl(),
                        overview = movie.overview.orEmpty(),
                        originalLanguage = movie.originalLanguage.orEmpty(),
                        popularity = movie.popularity.orZero(),
                        voteAverage = movie.voteAverage.orZero(),
                        releaseDate = movie.releaseDate.formatUsDateToBrDate(),
                        isFavorite = false
                    )
                )
            }
        }
    }
}