package com.brunodegan.ifood_challenge.data.datasources.domain.getNowPlaying

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.getResourceError
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.domain.getNowPlaying.GetNowPlayingUseCase
import com.brunodegan.ifood_challenge.domain.getNowPlaying.GetNowPlayingUseCaseImpl
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

class GetNowPlayingUseCaseTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val repository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetNowPlayingUseCase

    @Before
    fun setUp() {
        useCase = GetNowPlayingUseCaseImpl(repository = repository)
    }

    @Test
    fun `GIVEN now playing movies WHEN invoke is called THEN emit Resource Success`() =
        runTest {

            //Given
            val expectedData = Resource.Success(MockUtils.mockNowPlayingMoviesEntity())
            coEvery { repository.getNowPlayingMovies() } returns flow {
                emit(expectedData)
            }

            // When
            val result = useCase.invoke()

            // Then
            coVerify(exactly = 1) {
                repository.getNowPlayingMovies()
            }
            assertEquals(
                expectedData,
                result.first()
            )
        }

    @Test
    fun `GIVEN an exception WHEN invoke is called THEN emit ResourceError`() = runTest {
        // GIVEN
        val exception = Exception("Error fetching now playing movies")
        val resourceError = getResourceError<List<NowPlayingMoviesEntity>>(exception)

        coEvery { repository.getNowPlayingMovies() } returns flow {
            emit(resourceError)
        }

        // WHEN
        val result = useCase.invoke()

        // THEN
        assertTrue {
            result.first() is Resource.Error<List<NowPlayingMoviesEntity>>
        }
        assertEquals(
            "Error fetching now playing movies",
            (result.first() as Resource.Error).error.message
        )
    }

    @Test
    fun `GIVEN no now playing movies WHEN invoke is called THEN emit Resource_Success with empty list`() =
        runTest {
            // GIVEN
            coEvery { repository.getNowPlayingMovies() } returns flow {
                emit(Resource.Success(emptyList()))
            }

            // WHEN
            val result = useCase.invoke()

            // THEN
            assertEquals(Resource.Success(emptyList<NowPlayingMoviesEntity>()), result.first())
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}