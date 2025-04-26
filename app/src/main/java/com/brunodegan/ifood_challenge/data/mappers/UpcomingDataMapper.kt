package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.base.utils.formatFullCDNUrl
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.base.utils.orZero
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataModel
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import org.koin.core.annotation.Factory

@Factory
class UpcomingDataMapper : BaseMapper<MoviesApiDataModel, List<UpcomingMoviesEntity>> {
    override fun map(input: MoviesApiDataModel): List<UpcomingMoviesEntity> {
        return buildList {
            input.results.forEach { movie ->
                add(
                    UpcomingMoviesEntity(
                        title = movie.title.orEmpty(),
                        posterPath =  movie.posterPath.formatFullCDNUrl(),
                        overview = movie.overview.orEmpty(),
                        originalLanguage = movie.originalLanguage.orEmpty(),
                        popularity = movie.popularity.orZero(),
                        voteAverage = movie.voteAverage.orZero(),
                        releaseDate = movie.releaseDate.formatUsDateToBrDate()
                    )
                )
            }
        }
    }
}