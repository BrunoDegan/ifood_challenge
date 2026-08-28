package com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable

sealed interface FavoriteMoviesUiState {
    data object Initial : FavoriteMoviesUiState
    data object Loading : FavoriteMoviesUiState
    data class Success(val viewData: ImmutableList<FavoriteMoviesEntity>) : FavoriteMoviesUiState
    data class Error(val error: ErrorType) : FavoriteMoviesUiState
}