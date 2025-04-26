package com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.events

sealed interface FavoriteMoviesUiEvents {
    data object OnRetryButtonClickedUiEvent : FavoriteMoviesUiEvents
}