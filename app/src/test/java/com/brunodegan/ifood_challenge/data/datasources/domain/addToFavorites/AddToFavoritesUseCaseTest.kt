package com.brunodegan.ifood_challenge.data.datasources.domain.addToFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.addToFavorites.AddToFavoritesUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class AddToFavoritesUseCaseTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: AddToFavoritesUseCase
    private val movieId = 1

    @Before
    fun setUp() {
        useCase = AddToFavoritesUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN favorite movies WHEN invoke is called THEN emit Resource_Success and call addFavorite once`() = runTest {

        //Given
        val expectedData = Resource.Success(MockUtils.mockAddToFavoriteMoviesData())
        coEvery { repository.addFavorite(id = movieId) } returns flow {
            emit(expectedData)
        }

        // When
        val result = useCase.invoke(id = movieId)

        // Then
        coVerify(exactly = 1) {
            repository.addFavorite(id = movieId)
        }
        assertEquals(
            expectedData,
            result.first()
        )
    }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runTest {
        // GIVEN
        val exception = Exception("Error adding favorite movie")
        val resourceError = getResourceError<AddToFavoriteMoviesData>(exception)

        coEvery { repository.addFavorite(id = movieId) } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke(id = movieId)

        // THEN
        assertTrue {
            result.first() is Resource.Error<AddToFavoriteMoviesData>
        }
        assertEquals("Error adding favorite movie", (result.first() as Resource.Error).error.message)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}