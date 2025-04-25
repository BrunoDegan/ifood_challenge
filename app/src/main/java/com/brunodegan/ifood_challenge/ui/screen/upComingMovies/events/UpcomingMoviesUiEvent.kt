package com.brunodegan.ifood_challenge.ui.screen.upComingMovies.events

sealed interface UpcomingMoviesUiEvent {
    data class OnAddFavButtonClickedUiEvent(val id: Int) : UpcomingMoviesUiEvent
    data object OnRetryButtonClickedUiEvent : UpcomingMoviesUiEvent
}