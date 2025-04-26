package com.brunodegan.ifood_challenge.ui.screen.popularMovies.events

sealed interface PopularMoviesUiEvents {
    data class OnRemoveFavButtonClickedUiEvent(val id: Int) : PopularMoviesUiEvents
    data class OnAddFavButtonClickedUiEvent(val id: Int) : PopularMoviesUiEvents
    data object OnRetryButtonClickedUiEvent : PopularMoviesUiEvents
}