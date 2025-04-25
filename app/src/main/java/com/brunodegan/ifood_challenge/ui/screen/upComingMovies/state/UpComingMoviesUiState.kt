package com.brunodegan.ifood_challenge.ui.screen.upComingMovies.state

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity

sealed interface UpComingMoviesUiState {
    data object Initial : UpComingMoviesUiState
    data object Loading : UpComingMoviesUiState
    data class Success(val viewData: List<UpcomingMoviesEntity>) : UpComingMoviesUiState
    data class Error(val error: ErrorType) : UpComingMoviesUiState
}