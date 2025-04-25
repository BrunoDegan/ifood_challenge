package com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.state

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity

sealed interface NowPlayingMoviesUiState {
    data object Initial : NowPlayingMoviesUiState
    data object Loading : NowPlayingMoviesUiState
    data class Success(val viewData: List<NowPlayingMoviesEntity>) : NowPlayingMoviesUiState
    data class Error(val error: ErrorType) : NowPlayingMoviesUiState
}