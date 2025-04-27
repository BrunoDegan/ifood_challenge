package com.brunodegan.ifood_challenge.data.datasources.domain.getUpcoming

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.getUpComing.GetUpComingUseCase
import com.brunodegan.ifood_challenge.domain.getUpComing.GetUpComingUseCaseImpl
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

class GetUpComingUseCaseTest {

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetUpComingUseCase

    @Before
    fun setUp() {
        useCase = GetUpComingUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN get upcoming movies WHEN invoke is called THEN emit Resource Success`() =
        runBlocking {

            //Given
            val expectedData = Resource.Success(MockUtils.mockUpcomingMoviesEntity())
            coEvery { repository.getUpcomingMovies() } returns flow {
                emit(expectedData)
            }

            // When
            val result = useCase.invoke()

            // Then
            coVerify(exactly = 1) {
                repository.getUpcomingMovies()
            }
            assertEquals(
                expectedData,
                result.first()
            )
        }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runBlocking {
        // GIVEN
        val exception = Exception("Error fetching upcoming movies")
        val resourceError = getResourceError<List<UpcomingMoviesEntity>>(exception)

        coEvery { repository.getUpcomingMovies() } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertTrue {
            result.first() is Resource.Error<List<UpcomingMoviesEntity>>
        }
        assertEquals(
            "Error fetching upcoming movies",
            (result.first() as Resource.Error).error.message
        )
    }

    @Test
    fun `GIVEN no upcoming movies WHEN invoke is called THEN emit Resource_Success with empty list`() =
        runBlocking {
            // GIVEN
            coEvery { repository.getUpcomingMovies() } returns flow {
                emit(Resource.Success(emptyList()))
            }

            // WHEN
            val result = useCase.invoke()

            // THEN
            assertEquals(Resource.Success(emptyList<UpcomingMoviesEntity>()), result.first())
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}