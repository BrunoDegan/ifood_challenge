package com.brunodegan.ifood_challenge.ui.screen.upComingMovies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.domain.getUpComing.GetUpComingUseCase
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.events.UpcomingMoviesUiEvent
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.state.UpComingMoviesUiState
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
class UpComingMoviesViewModel(
    private val useCase: GetUpComingUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _snackbarState = Channel<SnackbarUiStateHolder>()
    val snackbarState = _snackbarState.receiveAsFlow()

    private val _uiState =
        MutableStateFlow<UpComingMoviesUiState>(UpComingMoviesUiState.Initial)
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UpComingMoviesUiState.Initial,
    )

    fun onUiEvent(event: UpcomingMoviesUiEvent) {
        when (event) {
            is UpcomingMoviesUiEvent.OnRetryButtonClickedUiEvent -> {
                getUpCommingMovies()
            }

            is UpcomingMoviesUiEvent.OnAddFavButtonClickedUiEvent -> {
                // TODO Add favorite button clicked
            }
        }
    }

    fun getUpCommingMovies() {
        viewModelScope.launch {
            useCase().flowOn(dispatcher)
                .distinctUntilChanged()
                .onStart {
                    _uiState.update { UpComingMoviesUiState.Loading }
                }
                .catch { error ->
                    error.message?.let {
                        _snackbarState.send(SnackbarUiStateHolder.SnackbarUi(it))
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.value = UpComingMoviesUiState.Success(result.data)
                        }

                        is Resource.Error -> {
                            _uiState.value = UpComingMoviesUiState.Error(result.error)
                        }
                    }
                }
        }
    }

}