package com.brunodegan.ifood_challenge.ui.screen.popularMovies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.domain.getPopular.GetPopularUseCase
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.events.PopularMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.state.PopularMoviesUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
class PopularMoviesViewModel(
    private val useCase: GetPopularUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState = MutableStateFlow<PopularMoviesUiState>(PopularMoviesUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PopularMoviesUiState.Initial,
    )

    fun onUiEvent(event: PopularMoviesUiEvents) {
        when (event) {
            is PopularMoviesUiEvents.OnRetryButtonClickedUiEvent -> {
                getPopularMovies()
            }

            is PopularMoviesUiEvents.OnAddFavButtonClickedUiEvent -> {
                // TODO Add favorite button clicked
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            useCase()
                .flowOn(dispatcher)
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { PopularMoviesUiState.Loading }
                }.catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }.collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.value = PopularMoviesUiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _uiState.value = PopularMoviesUiState.Error(result.error)
                        }
                    }
                }
        }
    }

}