package com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.ifood_challenge.base.dispatchers.DispatchersProviderInterface
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.domain.getFavorites.GetFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.events.FavoriteMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.state.FavoriteMoviesUiState
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
class FavoritesViewModel(
    private val useCase: GetFavoritesUseCase,
    private val dispatcher: DispatchersProviderInterface,
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState =
        MutableStateFlow<FavoriteMoviesUiState>(FavoriteMoviesUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavoriteMoviesUiState.Initial,
    )

    fun onUiEvent(event: FavoriteMoviesUiEvents) {
        when (event) {
            is FavoriteMoviesUiEvents.OnRetryButtonClickedUiEvent ->
                getFavoriteMovies()
        }
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            useCase().flowOn(dispatcher.io)
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { FavoriteMoviesUiState.Loading }
                }
                .catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.value = FavoriteMoviesUiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _uiState.value = FavoriteMoviesUiState.Error(result.error)
                        }
                    }
                }
        }
    }
}