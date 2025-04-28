package com.brunodegan.ifood_challenge.data.datasources.domain.removeFromFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.AddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.removeFromFavorites.RemoveFromFavoritesUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class RemoveFromFavoritesUseCaseTest {

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: RemoveFromFavoritesUseCase
    private val movieId = 1

    @Before
    fun setUp() {
        useCase = RemoveFromFavoritesUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN favorite movies WHEN invoke is called THEN emit Resource_Success and call removeFavorite once`() = runBlocking {

        //Given
        val expectedData = Resource.Success(MockUtils.mockAddToFavoriteMoviesData())
        coEvery { repository.removeFavorite(id = movieId) } returns flow {
            emit(expectedData)
        }

        // When
        val result = useCase.invoke(id = movieId)

        // Then
        coVerify(exactly = 1) {
            repository.removeFavorite(id = movieId)
        }
        assertEquals(
            expectedData,
            result.first()
        )
    }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runBlocking {
        // GIVEN
        val exception = Exception("Error removing favorite movie")
        val resourceError = getResourceError<AddToFavoriteMoviesData>(exception)

        coEvery { repository.removeFavorite(id = movieId) } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke(id = movieId)

        // THEN
        assertTrue {
            result.first() is Resource.Error<AddToFavoriteMoviesData>
        }
        assertEquals(
            "Error removing favorite movie",
            (result.first() as Resource.Error).error.message
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}