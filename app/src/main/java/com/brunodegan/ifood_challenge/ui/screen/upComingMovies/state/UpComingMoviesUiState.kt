package com.brunodegan.ifood_challenge.ui.screen.upComingMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
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
