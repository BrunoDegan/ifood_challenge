package utils

import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity

object AndroidTestUtils {

    fun mockFavoriteMoviesEntity(): List<FavoriteMoviesEntity> {
        return listOf(
            FavoriteMoviesEntity(
                id = 1,
                title = "Favorite Movie 1",
                posterPath = "/mock/favorite1.jpg",
                overview = "Overview for Favorite Movie 1",
                releaseDate = "2023-01-01",
                lastUpdated = System.currentTimeMillis()
            ),
            FavoriteMoviesEntity(
                id = 2,
                title = "Favorite Movie 2",
                posterPath = "/mock/favorite2.jpg",
                overview = "Overview for Favorite Movie 2",
                releaseDate = "2023-02-01",
                lastUpdated = System.currentTimeMillis()
            )
        )
    }

    fun mockTopRatedMoviesEntity(): List<TopRatedMoviesEntity> {
        return listOf(
            TopRatedMoviesEntity(
                id = 1,
                title = "Top Rated Movie 1",
                posterPath = "/mock/toprated1.jpg",
                overview = "Overview for Top Rated Movie 1",
                originalLanguage = "en",
                popularity = 9.5,
                voteAverage = 8.9,
                releaseDate = "2023-03-01",
                isFavorite = true
            ),
            TopRatedMoviesEntity(
                id = 2,
                title = "Top Rated Movie 2",
                posterPath = "/mock/toprated2.jpg",
                overview = "Overview for Top Rated Movie 2",
                originalLanguage = "fr",
                popularity = 8.8,
                voteAverage = 8.5,
                releaseDate = "2023-04-01",
                isFavorite = true
            )
        )
    }

    fun mockNowPlayingMoviesEntity(): List<NowPlayingMoviesEntity> {
        return listOf(
            NowPlayingMoviesEntity(
                id = 1,
                title = "Now Playing Movie 1",
                posterPath = "/mock/nowplaying1.jpg",
                overview = "Overview for Now Playing Movie 1",
                originalLanguage = "es",
                popularity = 7.5,
                voteAverage = 7.0,
                releaseDate = "2023-05-01",
                isFavorite = true
            ),
            NowPlayingMoviesEntity(
                id = 2,
                title = "Now Playing Movie 2",
                posterPath = "/mock/nowplaying2.jpg",
                overview = "Overview for Now Playing Movie 2",
                originalLanguage = "it",
                popularity = 8.0,
                voteAverage = 7.5,
                releaseDate = "2023-06-01",
                isFavorite = true
            )
        )
    }

    fun mockUpcomingMoviesEntity(): List<UpcomingMoviesEntity> {
        return listOf(
            UpcomingMoviesEntity(
                id = 1,
                title = "Upcoming Movie 1",
                posterPath = "/mock/upcoming1.jpg",
                overview = "Overview for Upcoming Movie 1",
                originalLanguage = "de",
                popularity = 6.5,
                voteAverage = 6.8,
                releaseDate = "2023-07-01",
                isFavorite = true
            ),
            UpcomingMoviesEntity(
                id = 2,
                title = "Upcoming Movie 2",
                posterPath = "/mock/upcoming2.jpg",
                overview = "Overview for Upcoming Movie 2",
                originalLanguage = "ja",
                popularity = 7.2,
                voteAverage = 7.1,
                releaseDate = "2023-08-01",
                isFavorite = true
            )
        )
    }

    fun mockPopularMoviesEntity(): List<PopularMoviesEntity> {
        return listOf(
            PopularMoviesEntity(
                id = 1,
                title = "Popular Movie 1",
                posterPath = "/mock/upcoming1.jpg",
                overview = "Overview for Upcoming Movie 1",
                originalLanguage = "de",
                popularity = 6.5,
                voteAverage = 6.8,
                releaseDate = "2023-07-01",
                isFavorite = true
            ),
            PopularMoviesEntity(
                id = 2,
                title = "Popular Movie 2",
                posterPath = "/mock/upcoming2.jpg",
                overview = "Overview for Upcoming Movie 2",
                originalLanguage = "ja",
                popularity = 7.2,
                voteAverage = 7.1,
                releaseDate = "2023-08-01",
                isFavorite = true
            )
        )
    }
}