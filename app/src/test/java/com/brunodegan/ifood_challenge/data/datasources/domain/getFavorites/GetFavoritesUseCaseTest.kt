package com.brunodegan.ifood_challenge.data.datasources.domain.getFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.getFavorites.GetFavoritesUseCase
import com.brunodegan.ifood_challenge.domain.getFavorites.GetFavoritesUseCaseImpl
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

class GetFavoritesUseCaseTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetFavoritesUseCase

    @Before
    fun setUp() {
        useCase = GetFavoritesUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN favorite movies WHEN invoke is called THEN emit Resource_Success`() = runTest {

        //Given
        val expectedData = Resource.Success(MockUtils.mockFavoriteMoviesEntity())
        coEvery { repository.getFavorites() } returns flow {
            emit(expectedData)
        }

        // When
        val result = useCase.invoke()

        // Then
        coVerify(exactly = 1) {
            repository.getFavorites()
        }
        assertEquals(
            expectedData,
            result.first()
        )
    }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runTest {
        // GIVEN
        val exception = Exception("Error fetching favorites")
        val resourceError = getResourceError<List<FavoriteMoviesEntity>>(exception)

        coEvery { repository.getFavorites() } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertTrue {
            result.first() is Resource.Error<List<FavoriteMoviesEntity>>
        }
        assertEquals("Error fetching favorites", (result.first() as Resource.Error).error.message)
    }

    @Test
    fun `GIVEN no favorite movies WHEN invoke is called THEN emit Resource_Success with empty list`() =
        runTest {
            // GIVEN
            coEvery { repository.getFavorites() } returns flow {
                emit(Resource.Success(emptyList()))
            }

            // WHEN
            val result = useCase.invoke()

            // THEN
            assertEquals(Resource.Success(emptyList<FavoriteMoviesEntity>()), result.first())
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}