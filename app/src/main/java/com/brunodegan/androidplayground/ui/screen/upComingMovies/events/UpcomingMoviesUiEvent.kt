package com.brunodegan.androidplayground.ui.screen.upComingMovies.events

sealed interface UpcomingMoviesUiEvent {
    data object OnRetryButtonClickedUiEvent : UpcomingMoviesUiEvent

    data class OnAddFavButtonClickedUiEvent(
        val id: Int,
    ) : UpcomingMoviesUiEvent

    data class OnRemoveFavButtonClickedUiEvent(
        val id: Int,
    ) : UpcomingMoviesUiEvent
}
