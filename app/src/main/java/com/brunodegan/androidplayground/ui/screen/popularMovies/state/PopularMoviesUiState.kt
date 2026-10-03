package com.brunodegan.androidplayground.ui.screen.popularMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.androidplayground.base.network.base.ErrorType
import com.brunodegan.androidplayground.data.datasources.local.entities.PopularMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface PopularMoviesUiState {
    data object Initial : PopularMoviesUiState

    data object Loading : PopularMoviesUiState

    data class Success(
        val viewData: ImmutableList<PopularMoviesEntity>,
    ) : PopularMoviesUiState

    data class Error(
        val error: ErrorType,
    ) : PopularMoviesUiState
}
