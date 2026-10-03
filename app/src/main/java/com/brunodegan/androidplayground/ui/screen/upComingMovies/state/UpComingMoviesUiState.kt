package com.brunodegan.androidplayground.ui.screen.upComingMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.androidplayground.base.network.base.ErrorType
import com.brunodegan.androidplayground.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface UpComingMoviesUiState {
    data object Initial : UpComingMoviesUiState

    data object Loading : UpComingMoviesUiState

    data class Success(
        val viewData: ImmutableList<UpcomingMoviesEntity>,
    ) : UpComingMoviesUiState

    data class Error(
        val error: ErrorType,
    ) : UpComingMoviesUiState
}
