package com.brunodegan.ifood_challenge.ui.screen.popularMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface PopularMoviesUiState {
    data object Initial : PopularMoviesUiState
    data object Loading : PopularMoviesUiState
    data class Success(val viewData: ImmutableList<PopularMoviesEntity>) : PopularMoviesUiState
    data class Error(val error: ErrorType) : PopularMoviesUiState
}