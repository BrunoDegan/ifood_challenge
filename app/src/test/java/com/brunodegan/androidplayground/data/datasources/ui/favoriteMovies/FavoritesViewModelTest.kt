package com.brunodegan.androidplayground.data.datasources.ui.favoriteMovies

import app.cash.turbine.test
import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.base.ui.SnackbarUiStateHolder
import com.brunodegan.androidplayground.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.androidplayground.domain.getFavorites.GetFavoritesUseCase
import com.brunodegan.androidplayground.testfixtures.MockUtils.getResourceError
import com.brunodegan.androidplayground.testfixtures.MockUtils.mockFavoriteMoviesEntity
import com.brunodegan.androidplayground.testfixtures.TestDispatcherRule
import com.brunodegan.androidplayground.ui.screen.favoriteMovies.events.FavoriteMoviesUiEvents
import com.brunodegan.androidplayground.ui.screen.favoriteMovies.state.FavoriteMoviesUiState
import com.brunodegan.androidplayground.ui.screen.favoriteMovies.viewModel.FavoritesViewModel
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

class FavoritesViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val useCase: GetFavoritesUseCase = mockk()

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        viewModel = FavoritesViewModel(useCase = useCase, dispatcher = testDispatcher)
    }

    @Test
    fun `GIVEN view model collects view entity succssefully WEN getFavoriteMovies is invoked THEN asserts correct states is emitted`() =
        runTest {
            val mockData = mockFavoriteMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { useCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.getFavoriteMovies()

            viewModel.uiState.test {
                assertEquals(FavoriteMoviesUiState.Initial, awaitItem())
                assertEquals(FavoriteMoviesUiState.Loading, awaitItem())
                assertEquals(FavoriteMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @Test
    fun `GIVEN view model collects view entity with error state propagated WEN getFavoriteMovies is invoked THEN asserts business error occurs`() =
        runTest {
            val exception = Exception("Error retrieving favorites movies")
            val resourceError = getResourceError<ImmutableList<FavoriteMoviesEntity>>(exception)

            coEvery { useCase.invoke() } returns flow { emit(resourceError) }

            viewModel.getFavoriteMovies()

            viewModel.uiState.test {
                assertEquals(FavoriteMoviesUiState.Initial, awaitItem())
                assertEquals(FavoriteMoviesUiState.Loading, awaitItem())
                assertEquals(FavoriteMoviesUiState.Error(resourceError.error), awaitItem())
            }
        }

    @Test
    fun `GIVEN system error WHEN getFavoriteMovies is invoked THEN asserts flow emits SnackbarUiState`() =
        runTest {
            val errorMsg = "message"
            val systemError = Throwable(errorMsg)

            coEvery { useCase.invoke() } returns flow { throw systemError }

            viewModel.getFavoriteMovies()

            viewModel.snackbarState.test {
                assertEquals(SnackbarUiStateHolder.SnackbarUi(errorMsg), awaitItem())
            }
        }

    @Test
    fun `GIVEN previous error happens and users clicks onRetry button WHEN viewModel receives OnRetryButtonClickedUiEvent THEN asserts successfully state occurs`() =
        runTest {
            val mockData = mockFavoriteMoviesEntity()
            val successfullyResponse = Resource.Success(mockData)

            coEvery { useCase.invoke() } returns flow { emit(successfullyResponse) }

            viewModel.onUiEvent(event = FavoriteMoviesUiEvents.OnRetryButtonClickedUiEvent)

            viewModel.uiState.test {
                assertEquals(FavoriteMoviesUiState.Initial, awaitItem())
                assertEquals(FavoriteMoviesUiState.Loading, awaitItem())
                assertEquals(FavoriteMoviesUiState.Success(mockData), awaitItem())
            }
        }

    @After
    fun tearDown() = unmockkAll()
}
