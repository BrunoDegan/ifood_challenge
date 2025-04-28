package com.brunodegan.ifood_challenge.data.datasources.domain.getTopRated

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.getTopRated.GetTopRatedUseCase
import com.brunodegan.ifood_challenge.domain.getTopRated.GetTopRatedUseCaseImpl
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

class GetTopRatedUseCaseTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetTopRatedUseCase

    @Before
    fun setUp() {
        useCase = GetTopRatedUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN top rated movies WHEN invoke is called THEN emit Resource Success`() =
        runTest {

            //Given
            val expectedData = Resource.Success(MockUtils.mockTopRatedMoviesEntity())
            coEvery { repository.getTopRateMovies() } returns flow {
                emit(expectedData)
            }

            // When
            val result = useCase.invoke()

            // Then
            coVerify(exactly = 1) {
                repository.getTopRateMovies()
            }
            assertEquals(
                expectedData,
                result.first()
            )
        }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runTest {
        // GIVEN
        val exception = Exception("Error fetching now top rated movies")
        val resourceError = getResourceError<List<TopRatedMoviesEntity>>(exception)

        coEvery { repository.getTopRateMovies() } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertTrue {
            result.first() is Resource.Error<List<TopRatedMoviesEntity>>
        }
        assertEquals(
            "Error fetching now top rated movies",
            (result.first() as Resource.Error).error.message
        )
    }

    @Test
    fun `GIVEN no top rated movies WHEN invoke is called THEN emit Resource_Success with empty list`() =
        runTest {
            // GIVEN
            coEvery { repository.getTopRateMovies() } returns flow {
                emit(Resource.Success(emptyList()))
            }

            // WHEN
            val result = useCase.invoke()

            // THEN
            assertEquals(Resource.Success(emptyList<TopRatedMoviesEntity>()), result.first())
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}