package com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.ifood_challenge.base.dispatchers.DispatchersProviderInterface
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.getNowPlaying.GetNowPlayingUseCase
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.events.NowPlayingMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.state.NowPlayingMoviesUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NowPlayingMoviesViewModel(
    private val useCase: GetNowPlayingUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val dispatcher: DispatchersProviderInterface
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState =
        MutableStateFlow<NowPlayingMoviesUiState>(NowPlayingMoviesUiState.Initial)
    val uiState = _uiState
        .onStart {
            getNowPlayingMovies()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NowPlayingMoviesUiState.Initial,
        )

    fun onUiEvent(event: NowPlayingMoviesUiEvents) {
        when (event) {
            is NowPlayingMoviesUiEvents.OnRetryButtonClickedUiEvent ->
                getNowPlayingMovies()

            is NowPlayingMoviesUiEvents.OnAddFavButtonClickedUiEvent ->
                addMovieToFavorites(movieId = event.id)

            is NowPlayingMoviesUiEvents.OnRemoveFavButtonClickedUiEvent ->
                removeMovieFromFavorites(movieId = event.id)

        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            useCase().flowOn(dispatcher.io)
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { NowPlayingMoviesUiState.Loading }
                }
                .catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.value = NowPlayingMoviesUiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _uiState.value = NowPlayingMoviesUiState.Error(result.error)
                        }
                    }
                }
        }
    }

    private fun addMovieToFavorites(movieId: Int) {
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(movieId)
                .flowOn(dispatcher.io)
                .distinctUntilChanged()
                .catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(result.data.statusMessage))
                        }

                        is Resource.Error -> {
                            _uiState.value = NowPlayingMoviesUiState.Error(result.error)
                        }
                    }
                }
        }
    }

    private fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            removeFromFavoritesUseCase.invoke(movieId)
                .flowOn(dispatcher.io)
                .distinctUntilChanged()
                .catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            if (result.data.success) {
                                _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(result.data.statusMessage))
                            }
                        }

                        is Resource.Error -> {
                            result.error.message?.let {
                                _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                            }
                        }
                    }
                }
        }
    }

}