package com.brunodegan.androidplayground.ui.screen.favoriteMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.androidplayground.base.network.base.ErrorType
import com.brunodegan.androidplayground.data.datasources.local.entities.FavoriteMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface FavoriteMoviesUiState {
    data object Initial : FavoriteMoviesUiState

    data object Loading : FavoriteMoviesUiState

    data class Success(
        val viewData: ImmutableList<FavoriteMoviesEntity>,
    ) : FavoriteMoviesUiState

    data class Error(
        val error: ErrorType,
    ) : FavoriteMoviesUiState
}
