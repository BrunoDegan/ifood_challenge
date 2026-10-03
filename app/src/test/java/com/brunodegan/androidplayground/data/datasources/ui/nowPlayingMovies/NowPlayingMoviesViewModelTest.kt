package com.brunodegan.androidplayground.data.datasources.ui.nowPlayingMovies

import app.cash.turbine.test
import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.base.ui.SnackbarUiStateHolder
import com.brunodegan.androidplayground.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.androidplayground.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.androidplayground.domain.getNowPlaying.GetNowPlayingUseCase
import com.brunodegan.androidplayground.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.androidplayground.testfixtures.MockUtils.getResourceError
import com.brunodegan.androidplayground.testfixtures.MockUtils.mockAddToFavoriteMoviesData
import com.brunodegan.androidplayground.testfixtures.MockUtils.mockNowPlayingMoviesEntity
import com.brunodegan.androidplayground.testfixtures.TestDispatcherRule
import com.brunodegan.androidplayground.ui.screen.nowPlayingMovies.events.NowPlayingMoviesUiEvents
import com.brunodegan.androidplayground.ui.screen.nowPlayingMovies.state.NowPlayingMoviesUiState
import com.brunodegan.androidplayground.ui.screen.nowPlayingMovies.viewModel.NowPlayingMoviesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class NowPlayingMoviesViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val getNowPlayingUseCase: GetNowPlayingUseCase = mockk()
    private val addToFavoritesUseCase: AddToFavoritesUseCase = mockk()
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase = mockk()

    private lateinit var viewModel: NowPlayingMoviesViewModel

    @Before
    fun setup() {
        viewModel =
            NowPlayingMoviesViewModel(
                useCase = getNowPlayingUseCase,
                addToFavoritesUseCase = addToFavoritesUseCase,
                removeFromFavoritesUseCase = removeFromFavoritesUseCase,
                dispatcher = testDispatcher,
            )
    }

    @Test
    fun `GIVEN view model collects view entity succssefully WEN getNowPlayingMovies is invoked THEN asserts correct states is emitted`() =
        runTest {
            val mockData = mockNowPlayingMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getNowPlayingUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.getNowPlayingMovies()

            viewModel.uiState.test {
                assertEquals(NowPlayingMoviesUiState.Initial, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Loading, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN view model collects view entity with error state propagated WEN getNowPlayingMovies is invoked THEN asserts business error occurs`() =
        runTest {
            val exception = Exception("Error retrieving upComing movies")
            val resourceError = getResourceError<ImmutableList<NowPlayingMoviesEntity>>(exception)

            coEvery { getNowPlayingUseCase.invoke() } returns flow { emit(resourceError) }

            viewModel.getNowPlayingMovies()

            viewModel.uiState.test {
                assertEquals(NowPlayingMoviesUiState.Initial, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Loading, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Error(resourceError.error), awaitItem())
            }
        }

    @Test
    fun `GIVEN system error WHEN getNowPlayingMovies is invoked THEN asserts flow emits SnackbarUiState`() =
        runTest {
            val errorMsg = "message"
            val systemError = Throwable(errorMsg)

            coEvery { getNowPlayingUseCase.invoke() } returns flow { throw systemError }

            viewModel.getNowPlayingMovies()

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(errorMsg), awaitItem())
            }
        }

    @Test
    fun `GIVEN previous error happens and users clicks onRetry button WHEN viewModel receives OnRetryButtonClickedUiEvent THEN asserts successfully state occurs`() =
        runTest {
            val mockData = mockNowPlayingMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getNowPlayingUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.onUiEvent(event = NowPlayingMoviesUiEvents.OnRetryButtonClickedUiEvent)

            viewModel.uiState.test {
                assertEquals(NowPlayingMoviesUiState.Initial, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Loading, awaitItem())
                assertEquals(NowPlayingMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN success now playing movie data WHEN OnAddFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar ui event is called`() =
        runTest {
            val mockAddFavoriteData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockAddFavoriteData)
            val movieId = 1

            coEvery { addToFavoritesUseCase.invoke(movieId) } returns
                flow {
                    emit(
                        successfullyResponse,
                    )
                }

            viewModel.onUiEvent(
                event =
                    NowPlayingMoviesUiEvents.OnAddFavButtonClickedUiEvent(
                        movieId,
                    ),
            )

            viewModel.snackbarState.test {
                assertEquals(
                    SnackbarUiStateHolder.SnackbarUi(mockAddFavoriteData.statusMessage),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `GIVEN success remove from now playing movies data WHEN OnRemoveFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar state is propagated`() =
        runTest {
            val mockAddFavoriteData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockAddFavoriteData)
            val movieId = 1

            coEvery { removeFromFavoritesUseCase.invoke(movieId) } returns
                flow {
                    emit(successfullyResponse)
                }

            viewModel.onUiEvent(
                event =
                    NowPlayingMoviesUiEvents.OnRemoveFavButtonClickedUiEvent(
                        movieId,
                    ),
            )

            viewModel.snackbarState.test {
                assertEquals(
                    SnackbarUiStateHolder.SnackbarUi(mockAddFavoriteData.statusMessage),
                    awaitItem(),
                )
            }
        }

    @After
    fun tearDown() = unmockkAll()
}
