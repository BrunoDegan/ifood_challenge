package com.brunodegan.ifood_challenge.data.datasources.ui.popularMovies

import app.cash.turbine.test
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockPopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.getPopular.GetPopularUseCase
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.events.PopularMoviesUiEvents
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.state.PopularMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.viewModel.PopularMoviesViewModel
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

class PopularMoviesViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val getPopularUseCase: GetPopularUseCase = mockk()
    private val addToFavoritesUseCase: AddToFavoritesUseCase = mockk()
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase = mockk()

    private lateinit var viewModel: PopularMoviesViewModel

    @Before
    fun setup() {
        viewModel = PopularMoviesViewModel(
            useCase = getPopularUseCase,
            addToFavoritesUseCase = addToFavoritesUseCase,
            removeFromFavoritesUseCase = removeFromFavoritesUseCase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `GIVEN view model collects view entity succssefully WEN getPopularMovies is invoked THEN asserts correct states is emitted`() =
        runTest {
            val mockData = mockPopularMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getPopularUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.getPopularMovies()

            viewModel.uiState.test {
                assertEquals(PopularMoviesUiState.Initial, awaitItem())
                assertEquals(PopularMoviesUiState.Loading, awaitItem())
                assertEquals(PopularMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN view model collects view entity with error state propagated WEN getPopularMovies is invoked THEN asserts business error occurs`() =
        runTest {
            val exception = Exception("Error retrieving upComing movies")
            val resourceError = getResourceError<List<PopularMoviesEntity>>(exception)

            coEvery { getPopularUseCase.invoke() } returns flow { emit(resourceError) }

            viewModel.getPopularMovies()

            viewModel.uiState.test {
                assertEquals(PopularMoviesUiState.Initial, awaitItem())
                assertEquals(PopularMoviesUiState.Loading, awaitItem())
                assertEquals(PopularMoviesUiState.Error(resourceError.error), awaitItem())
            }
        }

    @Test
    fun `GIVEN system error WHEN getPopularMovies is invoked THEN asserts flow emits SnackbarUiState`() =
        runTest {
            val errorMsg = "message"
            val systemError = Throwable(errorMsg)

            coEvery { getPopularUseCase.invoke() } returns flow { throw systemError }

            viewModel.getPopularMovies()

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(errorMsg), awaitItem())
            }
        }

    @Test
    fun `GIVEN previous error happens and users clicks onRetry button WHEN viewModel receives OnRetryButtonClickedUiEvent THEN asserts successfully state occurs`() =
        runTest {
            val mockData = mockPopularMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getPopularUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.onUiEvent(event = PopularMoviesUiEvents.OnRetryButtonClickedUiEvent)

            viewModel.uiState.test {
                assertEquals(PopularMoviesUiState.Initial, awaitItem())
                assertEquals(PopularMoviesUiState.Loading, awaitItem())
                assertEquals(PopularMoviesUiState.Success(mockData), awaitItem())
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
                event = PopularMoviesUiEvents.OnAddFavButtonClickedUiEvent(
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
                event = PopularMoviesUiEvents.OnRemoveFavButtonClickedUiEvent(
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