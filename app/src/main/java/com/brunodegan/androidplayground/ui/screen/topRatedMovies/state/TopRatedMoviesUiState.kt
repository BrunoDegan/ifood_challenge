package com.brunodegan.androidplayground.ui.screen.topRatedMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.androidplayground.base.network.base.ErrorType
import com.brunodegan.androidplayground.data.datasources.local.entities.TopRatedMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface TopRatedMoviesUiState {
    data object Initial : TopRatedMoviesUiState

    data object Loading : TopRatedMoviesUiState

    data class Success(
        val viewData: ImmutableList<TopRatedMoviesEntity>,
    ) : TopRatedMoviesUiState

    data class Error(
        val error: ErrorType,
    ) : TopRatedMoviesUiState
}
