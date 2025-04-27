package com.brunodegan.ifood_challenge.data.datasources.domain.getPopular

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.getPopular.GetPopularUseCase
import com.brunodegan.ifood_challenge.domain.getPopular.GetPopularUseCaseImpl
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

class GetPopularUseCaseTest {

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetPopularUseCase

    @Before
    fun setUp() {
        useCase = GetPopularUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN popular movies WHEN invoke is called THEN emit Resource_Success`() = runBlocking {

        //Given
        val expectedData = Resource.Success(MockUtils.mockPopularMoviesEntity())
        coEvery { repository.getPopularMovies() } returns flow {
            emit(expectedData)
        }

        // When
        val result = useCase.invoke()

        // Then
        coVerify(exactly = 1) {
            repository.getPopularMovies()
        }
        assertEquals(
            expectedData,
            result.first()
        )
    }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runBlocking {
        // GIVEN
        val exception = Exception("Error fetching popular movies")
        val resourceError = getResourceError<List<PopularMoviesEntity>>(exception)

        coEvery { repository.getPopularMovies() } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertTrue {
            result.first() is Resource.Error<List<PopularMoviesEntity>>
        }
        assertEquals(
            "Error fetching popular movies",
            (result.first() as Resource.Error).error.message
        )
    }

    @Test
    fun `GIVEN no popular movies WHEN invoke is called THEN emit Resource_Success with empty list`() =
        runBlocking {
            // GIVEN
            coEvery { repository.getPopularMovies() } returns flow {
                emit(Resource.Success(emptyList()))
            }

            // WHEN
            val result = useCase.invoke()

            // THEN
            assertEquals(Resource.Success(emptyList<PopularMoviesEntity>()), result.first())
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}