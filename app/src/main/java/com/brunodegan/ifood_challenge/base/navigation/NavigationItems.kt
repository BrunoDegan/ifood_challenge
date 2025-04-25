package com.brunodegan.ifood_challenge.base.navigation

import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.routes.ScreenRoutes

data class BottomNavItem(
    val position: Int,
    val route: String,
    val label: Int,
    val iconResourceId: Int,
)

sealed class BottomNavRoutes(
    val position: Int,
    val route: String,
    val title: Int,
    val imgResId: Int
) {
    data object NowPlaying :
        BottomNavRoutes(
            NOW_PLAYING,
            ScreenRoutes.NowPlayingScreen.route,
            R.string.now_playing_movies,
            R.drawable.today_movies
        )

    data object UpComming :
        BottomNavRoutes(
            UP_COMMING,
            ScreenRoutes.UpComingScreen.route,
            R.string.upcoming_movies,
            R.drawable.upcoming_movies
        )

    data object TopRated :
        BottomNavRoutes(
            TOP_RATED,
            ScreenRoutes.TopRatedScreen.route,
            R.string.top_rated_movies,
            R.drawable.top_rated_movies
        )

    data object Popular :
        BottomNavRoutes(
            POPULAR,
            ScreenRoutes.PopularScreen.route,
            R.string.popular_movies,
            R.drawable.popular_movies
        )
}

fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            position = BottomNavRoutes.NowPlaying.position,
            route = BottomNavRoutes.NowPlaying.route,
            label = BottomNavRoutes.NowPlaying.title,
            iconResourceId = BottomNavRoutes.NowPlaying.imgResId,
        ),
        BottomNavItem(
            position = BottomNavRoutes.UpComming.position,
            route = BottomNavRoutes.UpComming.route,
            label = BottomNavRoutes.UpComming.title,
            iconResourceId = BottomNavRoutes.UpComming.imgResId,
        ),
        BottomNavItem(
            position = BottomNavRoutes.TopRated.position,
            route = BottomNavRoutes.TopRated.route,
            label = BottomNavRoutes.TopRated.title,
            iconResourceId = BottomNavRoutes.TopRated.imgResId,
        ),
        BottomNavItem(
            position = BottomNavRoutes.Popular.position,
            route = BottomNavRoutes.Popular.route,
            label = BottomNavRoutes.Popular.title,
            iconResourceId = BottomNavRoutes.Popular.imgResId,
        )
    )
}

private const val NOW_PLAYING = 0
private const val UP_COMMING = 1
private const val TOP_RATED = 2
private const val POPULAR = 3
