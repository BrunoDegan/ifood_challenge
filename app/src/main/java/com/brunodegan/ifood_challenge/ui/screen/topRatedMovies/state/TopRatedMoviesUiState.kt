package com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.state

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.model.FavoriteMoviesViewData

sealed interface TopRatedMoviesUiState {
    data object Initial : TopRatedMoviesUiState
    data object Loading : TopRatedMoviesUiState
    data class Success(val viewData: List<TopRatedMoviesEntity>) : TopRatedMoviesUiState
    data class Error(val error: ErrorType) : TopRatedMoviesUiState
}