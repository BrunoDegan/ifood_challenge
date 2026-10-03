package com.brunodegan.androidplayground.ui.screen.favoriteMovies.events

sealed interface FavoriteMoviesUiEvents {
    data object OnRetryButtonClickedUiEvent : FavoriteMoviesUiEvents
}
