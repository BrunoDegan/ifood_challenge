package com.brunodegan.ifood_challenge.data.mappers

import com.brunodegan.ifood_challenge.base.utils.MOVIES_POSTER_CDN_URL
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.data.datasources.local.entities.MoviesApiDataModel
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import org.koin.core.annotation.Factory
import java.text.DateFormat

@Factory
class TopRatedDataMapper : BaseMapper<MoviesApiDataModel, List<TopRatedMoviesEntity>> {
    override fun map(input: MoviesApiDataModel): List<TopRatedMoviesEntity> {
        return buildList {
            input.results.forEach { movie ->
                add(
                    TopRatedMoviesEntity(
                        title = movie.title.orEmpty(),
                        posterPath = MOVIES_POSTER_CDN_URL + movie.posterPath.orEmpty(),
                        overview = movie.overview.orEmpty(),
                        originalLanguage = movie.originalLanguage.orEmpty(),
                        popularity = movie.popularity ?: 0.0,
                        voteAverage = movie.voteAverage ?: 0.0,
                        releaseDate = movie.releaseDate?.let { formatUsDateToBrDate(it) }.orEmpty()
                    )
                )
            }
        }
    }
}