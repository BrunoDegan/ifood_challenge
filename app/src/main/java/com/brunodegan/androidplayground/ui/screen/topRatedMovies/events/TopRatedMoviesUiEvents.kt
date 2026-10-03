package com.brunodegan.androidplayground.ui.screen.topRatedMovies.events

sealed interface TopRatedMoviesUiEvents {
    data class OnRemoveFavButtonClickedUiEvent(
        val id: Int,
    ) : TopRatedMoviesUiEvents

    data class OnAddFavButtonClickedUiEvent(
        val id: Int,
    ) : TopRatedMoviesUiEvents

    data object OnRetryButtonClickedUiEvent : TopRatedMoviesUiEvents
}
