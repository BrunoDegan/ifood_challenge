package com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.events

sealed interface NowPlayingMoviesUiEvents {
    data class OnAddFavButtonClickedUiEvent(val id: Int) : NowPlayingMoviesUiEvents
    data class OnRemoveFavButtonClickedUiEvent(val id: Int) : NowPlayingMoviesUiEvents
    data object OnRetryButtonClickedUiEvent : NowPlayingMoviesUiEvents
}