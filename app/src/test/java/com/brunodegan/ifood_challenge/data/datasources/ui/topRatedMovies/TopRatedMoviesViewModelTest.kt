package com.brunodegan.ifood_challenge.data.datasources.ui.topRatedMovies

import app.cash.turbine.test
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockTopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.getTopRated.GetTopRatedUseCase
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.events.TopRatedMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.state.TopRatedMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.viewModel.TopRatedMoviesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TopRatedMoviesViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val getTopRatedUseCase: GetTopRatedUseCase = mockk()
    private val addToFavoritesUseCase: AddToFavoritesUseCase = mockk()
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase = mockk()

    private lateinit var viewModel: TopRatedMoviesViewModel

    @Before
    fun setup() {
        viewModel = TopRatedMoviesViewModel(
            useCase = getTopRatedUseCase,
            addToFavoritesUseCase = addToFavoritesUseCase,
            removeFromFavoritesUseCase = removeFromFavoritesUseCase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `GIVEN view model collects view entity succssefully WEN getTopRatedMovies is invoked THEN asserts correct states is emitted`() =
        runTest {
            val mockData = mockTopRatedMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getTopRatedUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.getTopRatedMovies()

            viewModel.uiState.test {
                assertEquals(TopRatedMoviesUiState.Initial, awaitItem())
                assertEquals(TopRatedMoviesUiState.Loading, awaitItem())
                assertEquals(TopRatedMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN view model collects view entity with error state propagated WEN getTopRatedMovies is invoked THEN asserts business error occurs`() =
        runTest {
            val exception = Exception("Error retrieving upComing movies")
            val resourceError = getResourceError<List<TopRatedMoviesEntity>>(exception)

            coEvery { getTopRatedUseCase.invoke() } returns flow { emit(resourceError) }

            viewModel.getTopRatedMovies()

            viewModel.uiState.test {
                assertEquals(TopRatedMoviesUiState.Initial, awaitItem())
                assertEquals(TopRatedMoviesUiState.Loading, awaitItem())
                assertEquals(TopRatedMoviesUiState.Error(resourceError.error), awaitItem())
            }
        }

    @Test
    fun `GIVEN system error WHEN getTopRatedMovies is invoked THEN asserts flow emits SnackbarUiState`() =
        runTest {
            val errorMsg = "message"
            val systemError = Throwable(errorMsg)

            coEvery { getTopRatedUseCase.invoke() } returns flow { throw systemError }

            viewModel.getTopRatedMovies()

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(errorMsg), awaitItem())
            }
        }

    @Test
    fun `GIVEN previous error happens and users clicks onRetry button WHEN viewModel receives OnRetryButtonClickedUiEvent THEN asserts successfully state occurs`() =
        runTest {
            val mockData = mockTopRatedMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getTopRatedUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.onUiEvent(event = TopRatedMoviesUiEvents.OnRetryButtonClickedUiEvent)

            viewModel.uiState.test {
                assertEquals(TopRatedMoviesUiState.Initial, awaitItem())
                assertEquals(TopRatedMoviesUiState.Loading, awaitItem())
                assertEquals(TopRatedMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN success now top rated movie data WHEN OnAddFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar ui event is called`() =
        runTest {
            val mockAddFavoriteData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockAddFavoriteData)
            val movieId = 1

            coEvery { addToFavoritesUseCase.invoke(movieId) } returns flow {
                emit(
                    successfullyResponse
                )
            }

            viewModel.onUiEvent(
                event = TopRatedMoviesUiEvents.OnAddFavButtonClickedUiEvent(
                    movieId
                )
            )

            viewModel.snackbarState.test {
                assertEquals(
                    SnackbarUiStateHolder.SnackbarUi(mockAddFavoriteData.statusMessage),
                    awaitItem()
                )
            }
        }

    @Test
    fun `GIVEN success remove from top rated movies data WHEN OnRemoveFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar state is propagated`() =
        runTest {
            val mockAddFavoriteData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockAddFavoriteData)
            val movieId = 1

            coEvery { removeFromFavoritesUseCase.invoke(movieId) } returns flow {
                emit(successfullyResponse)
            }


            viewModel.onUiEvent(
                event = TopRatedMoviesUiEvents.OnRemoveFavButtonClickedUiEvent(
                    movieId
                )
            )

            viewModel.snackbarState.test {
                assertEquals(
                    SnackbarUiStateHolder.SnackbarUi(mockAddFavoriteData.statusMessage),
                    awaitItem()
                )
            }
        }

    @After
    fun tearDown() = unmockkAll()
}