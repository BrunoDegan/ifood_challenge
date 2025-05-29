package com.brunodegan.ifood_challenge.base.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes(val route: String): NavKey {
    @Serializable
    data object NowPlayingScreen : ScreenRoutes("NowPlayingScreen")
    @Serializable
    data object PopularScreen : ScreenRoutes("PopularScreen")
    @Serializable
    data object TopRatedScreen : ScreenRoutes("TopRatedScreen")
    @Serializable
    data object UpComingScreen : ScreenRoutes("UpComingScreen")
    @Serializable
    data object FavoritesScreen: ScreenRoutes("FavoritesScreen")

    companion object {
        fun route(route: String): ScreenRoutes {
            return when(route) {
                NowPlayingScreen.route -> return NowPlayingScreen
                PopularScreen.route -> return PopularScreen
                TopRatedScreen.route -> return TopRatedScreen
                UpComingScreen.route -> return UpComingScreen
                FavoritesScreen.route -> return FavoritesScreen
                else -> FavoritesScreen
            }
        }
    }
}
