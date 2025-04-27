package com.brunodegan.ifood_challenge.data.datasources.data.remote

import com.brunodegan.ifood_challenge.data.api.RestApiService
import com.brunodegan.ifood_challenge.data.datasources.remote.RemoteDataSource
import com.brunodegan.ifood_challenge.data.datasources.remote.RemoteDataSourceImpl
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoteDataSourceImplTest {
    private val restApiService: RestApiService = mockk(relaxed = true)

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSourceImpl(restApi = restApiService)
    }

    @Test
    fun `GIVEN movies api data response mocks WHEN fetchNowPlaying is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockMoviesApiDataResponse()

            coEvery { restApiService.fetchNowPlaying() } returns mockResponse

            val result = remoteDataSource.fetchNowPlaying()

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.fetchNowPlaying()
            }
        }

    @Test
    fun `GIVEN movies api data response mocks WHEN fetchPopular is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockMoviesApiDataResponse()

            coEvery { restApiService.fetchPopular() } returns mockResponse

            val result = remoteDataSource.fetchPopular()

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.fetchPopular()
            }
        }

    @Test
    fun `GIVEN movies api data response mocks WHEN fetchTopRated is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockMoviesApiDataResponse()

            coEvery { restApiService.fetchTopRated() } returns mockResponse

            val result = remoteDataSource.fetchTopRated()

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.fetchTopRated()
            }
        }

    @Test
    fun `GIVEN movies api data response mocks WHEN fetchUpcoming is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockMoviesApiDataResponse()

            coEvery { restApiService.fetchUpcoming() } returns mockResponse

            val result = remoteDataSource.fetchUpcoming()

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.fetchUpcoming()
            }
        }

    @Test
    fun `GIVEN movies api data response mocks WHEN getFavorites is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockMoviesApiDataResponse()

            coEvery { restApiService.getFavorites() } returns mockResponse

            val result = remoteDataSource.getFavorites()

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.getFavorites()
            }
        }

    @Test
    fun `GIVEN movies api data response mocks WHEN addToFavorites is invoked THEN asserts response equality`() =
        runBlocking {
            val mockResponse = MockUtils.mockAddToFavoritesResponse()
            val mockRequest = MockUtils.mockAddToFavoritesRequest()

            coEvery { restApiService.addToFavorites(addToFavoritesRequest = mockRequest) } returns mockResponse

            val result = remoteDataSource.addOrRemoveFromFavorites(requestData = mockRequest)

            assertEquals(mockResponse, result)
            coVerify(exactly = 1) {
                restApiService.addToFavorites(any(), any())
            }
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}