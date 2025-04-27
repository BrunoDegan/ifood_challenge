package com.brunodegan.ifood_challenge.data.datasources.data.api

import com.brunodegan.ifood_challenge.data.api.RestApiService
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RestApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApiService::class.java)
    }

    @Test
    fun `GIVEN mock response WHEN fetchNowPlaying is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockMoviesApiDataResponse()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.fetchNowPlaying()

        assertEquals(mockResponse, result)
    }

    @Test
    fun `GIVEN mock response WHEN fetchTopRated is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockMoviesApiDataResponse()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.fetchTopRated()

        assertEquals(mockResponse, result)
    }

    @Test
    fun `GIVEN mock response WHEN fetchPopular is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockMoviesApiDataResponse()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.fetchPopular()

        assertEquals(mockResponse, result)
    }

    @Test
    fun `GIVEN mock response WHEN fetchUpcoming is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockMoviesApiDataResponse()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.fetchUpcoming()

        assertEquals(mockResponse, result)
    }

    @Test
    fun `GIVEN mock response WHEN addToFavorites is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockAddToFavoritesResponse()
        val mockRequest = MockUtils.mockAddToFavoritesRequest()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.addToFavorites(addToFavoritesRequest = mockRequest)

        assertEquals(mockResponse, result)
    }

    @Test
    fun `GIVEN mock response WHEN getFavorites is called THEN verify response`() = runBlocking {
        val mockResponse = MockUtils.mockMoviesApiDataResponse()
        val jsonResponse = MockUtils.toJsonString(mockResponse)

        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        val result = apiService.getFavorites()

        assertEquals(mockResponse, result)
    }

    @After
    fun tearDown() {
        unmockkAll()
        mockWebServer.shutdown()
    }
}
