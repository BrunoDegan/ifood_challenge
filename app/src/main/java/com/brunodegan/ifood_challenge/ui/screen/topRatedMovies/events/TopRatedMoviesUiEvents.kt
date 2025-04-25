package com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.events

sealed interface TopRatedMoviesUiEvents {
    data class OnAddFavButtonClickedUiEvent(val id: Int) : TopRatedMoviesUiEvents
    data object OnRetryButtonClickedUiEvent : TopRatedMoviesUiEvents
}