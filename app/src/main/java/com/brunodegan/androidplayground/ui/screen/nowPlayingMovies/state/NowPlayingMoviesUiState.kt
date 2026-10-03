package com.brunodegan.androidplayground.ui.screen.nowPlayingMovies.state

import androidx.compose.runtime.Immutable
import com.brunodegan.androidplayground.base.network.base.ErrorType
import com.brunodegan.androidplayground.data.datasources.local.entities.NowPlayingMoviesEntity
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface NowPlayingMoviesUiState {
    data object Initial : NowPlayingMoviesUiState

    data object Loading : NowPlayingMoviesUiState

    data class Success(
        val viewData: ImmutableList<NowPlayingMoviesEntity>,
    ) : NowPlayingMoviesUiState

    data class Error(
        val error: ErrorType,
    ) : NowPlayingMoviesUiState
}
