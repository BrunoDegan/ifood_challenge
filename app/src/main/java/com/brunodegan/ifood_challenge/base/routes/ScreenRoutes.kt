package com.brunodegan.ifood_challenge.base.routes

sealed class ScreenRoutes(val route: String) {
    data object NowPlayingScreen : ScreenRoutes("now_playing")
    data object PopularScreen : ScreenRoutes("popular")
    data object TopRatedScreen : ScreenRoutes("top_rated")
    data object UpComingScreen : ScreenRoutes("upcoming")
}
