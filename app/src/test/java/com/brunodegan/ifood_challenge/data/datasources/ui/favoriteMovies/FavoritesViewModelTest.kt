package com.brunodegan.ifood_challenge.data.datasources.ui.favoriteMovies

import app.cash.turbine.test
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockFavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.domain.getFavorites.GetFavoritesUseCase
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.state.FavoriteMoviesUiState
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.viewModel.FavoritesViewModel
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

class FavoritesViewModelTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val useCase: GetFavoritesUseCase = mockk()

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        viewModel = FavoritesViewModel(useCase = useCase)
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

    @After
    fun tearDown() = unmockkAll()
}