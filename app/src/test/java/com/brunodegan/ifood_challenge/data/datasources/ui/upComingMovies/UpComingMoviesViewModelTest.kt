package com.brunodegan.ifood_challenge.data.datasources.ui.upComingMovies

import app.cash.turbine.test
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.base.ui.SnackbarUiStateHolder
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockUpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.getUpComing.GetUpComingUseCase
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.events.UpcomingMoviesUiEvent
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.state.UpComingMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.viewModel.UpComingMoviesViewModel
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

class UpComingMoviesViewModelTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val getUpComingUseCase: GetUpComingUseCase = mockk()
    private val addToFavoritesUseCase: AddToFavoritesUseCase = mockk()
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase = mockk()

    private lateinit var viewModel: UpComingMoviesViewModel

    @Before
    fun setup() {
        viewModel = UpComingMoviesViewModel(
            useCase = getUpComingUseCase,
            addToFavoritesUseCase = addToFavoritesUseCase,
            removeFromFavoritesUseCase = removeFromFavoritesUseCase,
        )
    }

    @Test
    fun `GIVEN view model collects view entity succssefully WEN getUpComingUseCase is invoked THEN asserts correct states is emitted`() =
        runTest {
            val mockData = mockUpcomingMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getUpComingUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.getUpCommingMovies()

            viewModel.uiState.test {
                assertEquals(UpComingMoviesUiState.Initial, awaitItem())
                assertEquals(UpComingMoviesUiState.Loading, awaitItem())
                assertEquals(UpComingMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN view model collects view entity with error state propagated WEN getUpComingUseCase is invoked THEN asserts business error occurs`() =
        runTest {
            val exception = Exception("Error retrieving upComing movies")
            val resourceError = getResourceError<List<UpcomingMoviesEntity>>(exception)

            coEvery { getUpComingUseCase.invoke() } returns flow { emit(resourceError) }

            viewModel.getUpCommingMovies()

            viewModel.uiState.test {
                assertEquals(UpComingMoviesUiState.Initial, awaitItem())
                assertEquals(UpComingMoviesUiState.Loading, awaitItem())
                assertEquals(UpComingMoviesUiState.Error(resourceError.error), awaitItem())
            }
        }

    @Test
    fun `GIVEN system error WHEN getUpComingMovies is invoked THEN asserts flow emits SnackbarUiState`() =
        runTest {
            val errorMsg = "message"
            val systemError = Throwable(errorMsg)

            coEvery { getUpComingUseCase.invoke() } returns flow { throw systemError }

            viewModel.getUpCommingMovies()

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(errorMsg), awaitItem())
            }
        }

    @Test
    fun `GIVEN previous error happens and users clicks onRetry button WHEN viewModel receives OnRetryButtonClickedUiEvent THEN asserts successfully state occurs`() =
        runTest {
            val mockData = mockUpcomingMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { getUpComingUseCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.onUiEvent(event = UpcomingMoviesUiEvent.OnRetryButtonClickedUiEvent)

            viewModel.uiState.test {
                assertEquals(UpComingMoviesUiState.Initial, awaitItem())
                assertEquals(UpComingMoviesUiState.Loading, awaitItem())
                assertEquals(UpComingMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN success add to favorite movie data WHEN OnAddFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar ui event is called`() =
        runTest {
            val mockData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockData)
            val movieId = 1

            coEvery { addToFavoritesUseCase.invoke(id = movieId) } returns flow {
                emit(
                    successfullyResponse
                )
            }

            viewModel.onUiEvent(event = UpcomingMoviesUiEvent.OnAddFavButtonClickedUiEvent(movieId))

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(mockData.statusMessage), awaitItem())
            }
        }

    @Test
    fun `GIVEN success remove from favorite movie data WHEN OnRemoveFavButtonClickedUiEvent is dispatched THEN asserts Initial and loading state and snackbar state is propagated`() =
        runTest {
            val mockData = mockAddToFavoriteMoviesData()
            val successfullyResponse = Resource.Success(mockData)
            val movieId = 1

            coEvery { removeFromFavoritesUseCase.invoke(id = movieId) } returns flow {
                emit(
                    successfullyResponse
                )
            }

            viewModel.onUiEvent(
                event = UpcomingMoviesUiEvent.OnRemoveFavButtonClickedUiEvent(
                    movieId
                )
            )

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(mockData.statusMessage), awaitItem())
            }
        }

    @After
    fun tearDown() = unmockkAll()

}

