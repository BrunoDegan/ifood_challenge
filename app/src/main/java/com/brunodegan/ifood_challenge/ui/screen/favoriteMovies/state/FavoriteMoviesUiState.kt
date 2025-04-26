package com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.state

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity

sealed interface FavoriteMoviesUiState {
    data object Initial : FavoriteMoviesUiState
    data object Loading : FavoriteMoviesUiState
    data class Success(val viewData: List<FavoriteMoviesEntity>) : FavoriteMoviesUiState
    data class Error(val error: ErrorType) : FavoriteMoviesUiState
}