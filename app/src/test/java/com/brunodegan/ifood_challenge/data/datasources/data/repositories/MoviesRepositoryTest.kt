package com.brunodegan.ifood_challenge.data.datasources.data.repositories

import com.brunodegan.ifood_challenge.base.network.base.ErrorType
import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.LocalDataSource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.remote.RemoteDataSource
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoriteMoviesData
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoritesApiResponse
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockAddToFavoritesRequest
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockFavoriteMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockMoviesApiDataResponse
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockNowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockPopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.mockTopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import com.brunodegan.ifood_challenge.data.mappers.AddOrRemoveToFavoritesResponseDataMapper
import com.brunodegan.ifood_challenge.data.mappers.FavoritesDataMapper
import com.brunodegan.ifood_challenge.data.mappers.NowPlayingDataMapper
import com.brunodegan.ifood_challenge.data.mappers.PopularDataMapper
import com.brunodegan.ifood_challenge.data.mappers.TopRatedDataMapper
import com.brunodegan.ifood_challenge.data.mappers.UpcomingDataMapper
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesRepositoryTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private val localDataSource: LocalDataSource = mockk(relaxed = true)
    private val remoteDataSource: RemoteDataSource = mockk(relaxed = true)
    private val topRatedDataMapper: TopRatedDataMapper = mockk(relaxed = true)
    private val upcomingMoviesDataMapper: UpcomingDataMapper = mockk(relaxed = true)
    private val popularMoviesDataMapper: PopularDataMapper = mockk(relaxed = true)
    private val nowPlayingMoviesDataMapper: NowPlayingDataMapper = mockk(relaxed = true)
    private val addOrRemoveToFavoritesResponseDataMapper: AddOrRemoveToFavoritesResponseDataMapper =
        mockk(relaxed = true)
    private val favoritesDataMapper: FavoritesDataMapper = mockk(relaxed = true)

    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        repository = MoviesRepositoryImpl(
            addOrRemoveToFavoritesResponseDataMapper = addOrRemoveToFavoritesResponseDataMapper,
            favoritesDataMapper = favoritesDataMapper,
            nowPlayingMoviesDataMapper = nowPlayingMoviesDataMapper,
            popularMoviesDataMapper = popularMoviesDataMapper,
            topRatedMoviesDataMapper = topRatedDataMapper,
            upcomingMoviesDataMapper = upcomingMoviesDataMapper,
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `GIVEN local data WHEN getTopRateMovies is called THEN emit Resource_Success`() =
        runTest {
            // Given
            val topRatedMovies = mockTopRatedMoviesEntity()
            val favoritesMovies = mockFavoriteMoviesEntity()
            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getTopRated() } returns flow { emit(topRatedMovies) }

            // When
            val result = repository.getTopRateMovies()

            // Then
            assertEquals(Resource.Success(topRatedMovies), result.first())
        }

    @Test
    fun `GIVEN empty local data and fetchs remote data successfully WHEN getTopRateMovies is called THEN emit Resource_Success`() =
        runTest {
            // GIVEN
            val topRatedMovies = mockTopRatedMoviesEntity()
            val apiData = mockMoviesApiDataResponse()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getTopRated() } returns flow { emit(emptyList()) }
            coEvery { remoteDataSource.fetchTopRated() } returns apiData
            coEvery { topRatedDataMapper.map(any()) } returns topRatedMovies

            // WHEN
            val result = repository.getTopRateMovies().toList()

            // THEN
            assertEquals(Resource.Success(topRatedMovies), result.first())
        }

    @Test
    fun `GIVEN NULL local data and fetchs remote data with error WHEN getTopRateMovies is called THEN emit Resource_Error`() =
        runTest {
            // GIVEN
            val errorMessage = "Network error"
            val genericError = ErrorType.Generic(errorMessage)
            val expectedError =
                Resource.Error<TopRatedMoviesEntity>(genericError)
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getTopRated() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchTopRated() } throws Exception(errorMessage)

            // WHEN
            val result = repository.getTopRateMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedError, result.first())
        }

    @Test
    fun `GIVEN TopRated mock api response data WHEN local data source answers with null and remote data source fetchs data successfully THEN asserts Success Data`() =
        runTest {
            // GIVEN
            val topRatedMoviesApiResponse = mockMoviesApiDataResponse()
            val expectedViewData = mockTopRatedMoviesEntity()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getTopRated() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchTopRated() } returns topRatedMoviesApiResponse
            every { topRatedDataMapper.map(any()) } returns expectedViewData

            // WHEN
            val result = repository.getTopRateMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedViewData, (result.first() as Resource.Success).data)
        }


    @Test
    fun `GIVEN favorites local data WHEN getFavorites is called THEN emit Resource_Success`() =
        runTest {
            // Given
            val favoritesMovies = mockFavoriteMoviesEntity()
            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }

            // When
            val result = repository.getFavorites()

            // Then
            assertEquals(Resource.Success(favoritesMovies), result.first())
        }

    @Test
    fun `GIVEN empty favorites local data and fetchs remote data successfully WHEN getFavorites is called THEN emit Resource_Success`() =
        runTest {
            // GIVEN
            val favoriteMoviesApiResponse = mockFavoriteMoviesEntity()
            val apiData = mockMoviesApiDataResponse()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(emptyList()) }
            coEvery { remoteDataSource.fetchFavorites() } returns apiData
            coEvery { favoritesDataMapper.map(any()) } returns favoriteMoviesApiResponse

            // WHEN
            val result = repository.getFavorites().toList()

            // THEN
            assertEquals(Resource.Success(favoriteMoviesApiResponse), result.first())
        }

    @Test
    fun `GIVEN NULL favorites local data and fetchs remote data with error WHEN getFavorites is called THEN emit Resource_Error`() =
        runTest {
            // GIVEN
            val errorMessage = "Network error"
            val genericError = ErrorType.Generic(errorMessage)
            val expectedError =
                Resource.Error<FavoriteMoviesEntity>(genericError)

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchFavorites() } throws Exception(errorMessage)

            // WHEN
            val result = repository.getFavorites().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedError, result.first())
        }

    @Test
    fun `GIVEN favorite movies mock api response data WHEN local data source answers with null and remote data source fetchs data successfully THEN asserts Success Data`() =
        runTest {
            // GIVEN
            val favoriteMoviesApiResponse = mockMoviesApiDataResponse()
            val expectedViewData = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchFavorites() } returns favoriteMoviesApiResponse
            every { favoritesDataMapper.map(any()) } returns expectedViewData

            // WHEN
            val result = repository.getFavorites().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedViewData, (result.first() as Resource.Success).data)
        }

    @Test
    fun `GIVEN popular local data WHEN getPopularMovies is called THEN emit Resource_Success`() =
        runTest {
            // Given
            val popularMovies = mockPopularMoviesEntity()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getPopular() } returns flow { emit(popularMovies) }

            // When
            val result = repository.getPopularMovies()

            // Then
            assertEquals(Resource.Success(popularMovies), result.first())
        }

    @Test
    fun `GIVEN empty popular local data and fetchs remote data successfully WHEN getPopularMovies is called THEN emit Resource_Success`() =
        runTest {
            // GIVEN
            val popularMovies = mockPopularMoviesEntity()
            val apiData = mockMoviesApiDataResponse()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getPopular() } returns flow { emit(emptyList()) }
            coEvery { remoteDataSource.fetchPopular() } returns apiData
            coEvery { popularMoviesDataMapper.map(any()) } returns popularMovies

            // WHEN
            val result = repository.getPopularMovies().toList()

            // THEN
            assertEquals(Resource.Success(popularMovies), result.first())
        }

    @Test
    fun `GIVEN NULL popular local data and fetchs remote data with error WHEN getPopularMovies is called THEN emit Resource_Error`() =
        runTest {
            // GIVEN
            val errorMessage = "Network error"
            val genericError = ErrorType.Generic(errorMessage)
            val expectedError =
                Resource.Error<PopularMoviesEntity>(genericError)
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getPopular() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchPopular() } throws Exception(errorMessage)

            // WHEN
            val result = repository.getPopularMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedError, result.first())
        }

    @Test
    fun `GIVEN popular movies mock api response data WHEN local data source answers with null and remote data source fetchs data successfully THEN asserts Success Data`() =
        runTest {
            // GIVEN
            val popularMoviesApiResponse = mockMoviesApiDataResponse()
            val expectedViewData = mockPopularMoviesEntity()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getPopular() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchPopular() } returns popularMoviesApiResponse
            every { popularMoviesDataMapper.map(any()) } returns expectedViewData

            // WHEN
            val result = repository.getPopularMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedViewData, (result.first() as Resource.Success).data)
        }

    @Test
    fun `GIVEN empty now playing local data and fetchs remote data successfully WHEN getNowPlayingMovies is called THEN emit Resource_Success`() =
        runTest {
            // GIVEN
            val nowPlaying = mockNowPlayingMoviesEntity()
            val apiData = mockMoviesApiDataResponse()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getNowPlaying() } returns flow { emit(emptyList()) }
            coEvery { remoteDataSource.fetchNowPlaying() } returns apiData
            coEvery { nowPlayingMoviesDataMapper.map(any()) } returns nowPlaying

            // WHEN
            val result = repository.getNowPlayingMovies().toList()

            // THEN
            assertEquals(Resource.Success(nowPlaying), result.first())
        }

    @Test
    fun `GIVEN NULL now playing local data and fetchs remote data with error WHEN getNowPlayingMovies is called THEN emit Resource_Error`() =
        runTest {
            // GIVEN
            val errorMessage = "Network error"
            val genericError = ErrorType.Generic(errorMessage)
            val expectedError =
                Resource.Error<PopularMoviesEntity>(genericError)
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getNowPlaying() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchNowPlaying() } throws Exception(errorMessage)

            // WHEN
            val result = repository.getNowPlayingMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedError, result.first())
        }

    @Test
    fun `GIVEN now playing movies mock api response data WHEN local data source answers with null and remote data source fetchs data successfully THEN asserts Success Data`() =
        runTest {
            // GIVEN
            val nowPlayingMoviesApiResponse = mockMoviesApiDataResponse()
            val expectedViewData = mockNowPlayingMoviesEntity()
            val favoritesMovies = mockFavoriteMoviesEntity()

            coEvery { localDataSource.getFavoriteMovies() } returns flow { emit(favoritesMovies) }
            coEvery { localDataSource.getNowPlaying() } returns flow { emit(null) }
            coEvery { remoteDataSource.fetchNowPlaying() } returns nowPlayingMoviesApiResponse
            every { nowPlayingMoviesDataMapper.map(any()) } returns expectedViewData

            // WHEN
            val result = repository.getNowPlayingMovies().toList()

            // THEN
            assertEquals(1, result.size)
            assertEquals(expectedViewData, (result.first() as Resource.Success).data)
        }

    @Test
    fun `GIVEN favorite movie data WHEN addFavorite THEN `() = runTest {
        val expectedViewData = mockAddToFavoriteMoviesData()
        val requestData = mockAddToFavoritesRequest()
        val mockAddToFavoritesApiResponse = mockAddToFavoritesApiResponse()

        coEvery { remoteDataSource.addOrRemoveFromFavorites(requestData) } returns mockAddToFavoritesApiResponse
        coEvery { addOrRemoveToFavoritesResponseDataMapper.map(mockAddToFavoritesApiResponse) } returns expectedViewData

        val result = repository.addFavorite(id = 1)

        assertEquals(expectedViewData, (result.first() as Resource.Success).data)
    }

    @Test
    fun `GIVEN network error when fetching favorite movies WHEN addOrRemoveFromFavorites on remote data source THEN asserts Resource_Error result`() =
        runTest {
            val expectedViewData = mockAddToFavoriteMoviesData()
            val mockAddToFavoritesApiResponse = mockAddToFavoritesApiResponse()
            val requestData = mockAddToFavoritesRequest()
            val errorMessage = "Network error"
            val genericError = ErrorType.Generic(errorMessage)
            val expectedError = Resource.Error<FavoriteMoviesEntity>(genericError)

            coEvery { addOrRemoveToFavoritesResponseDataMapper.map(mockAddToFavoritesApiResponse) } returns expectedViewData
            coEvery { remoteDataSource.addOrRemoveFromFavorites(requestData) } throws Exception(
                errorMessage
            )

            val result = repository.addFavorite(id = 1)

            assertEquals(expectedError, result.first())
        }

    @After
    fun tearDown() = unmockkAll()
}