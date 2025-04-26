package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.base.utils.formatFullCDNUrl
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.base.utils.orZero
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataModel
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import org.koin.core.annotation.Factory

@Factory
class NowPlayingDataMapper : BaseMapper<MoviesApiDataModel, List<NowPlayingMoviesEntity>> {
    override fun map(input: MoviesApiDataModel): List<NowPlayingMoviesEntity> {
        return buildList {
            input.results.forEach { movie ->
                add(
                    NowPlayingMoviesEntity(
                        title = movie.title.orEmpty(),
                        posterPath = movie.posterPath.formatFullCDNUrl(),
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