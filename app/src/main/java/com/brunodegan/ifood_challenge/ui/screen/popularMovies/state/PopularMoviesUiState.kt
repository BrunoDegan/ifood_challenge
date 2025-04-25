package com.brunodegan.ifood_challenge.ui.screen.popularMovies.state

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity

sealed interface PopularMoviesUiState {
    data object Initial : PopularMoviesUiState
    data object Loading : PopularMoviesUiState
    data class Success(val viewData: List<PopularMoviesEntity>) : PopularMoviesUiState
    data class Error(val error: ErrorType) : PopularMoviesUiState
}