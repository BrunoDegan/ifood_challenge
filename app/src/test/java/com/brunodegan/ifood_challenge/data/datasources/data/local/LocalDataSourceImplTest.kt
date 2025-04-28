package com.brunodegan.ifood_challenge.data.datasources.data.local

import com.brunodegan.ifood_challenge.data.datasources.local.LocalDataSource
import com.brunodegan.ifood_challenge.data.datasources.local.LocalDataSourceImpl
import com.brunodegan.ifood_challenge.data.datasources.local.daos.FavoritesDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.NowPlayingDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.PopularDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.TopRatedDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.UpComingDao
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.TestDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class LocalDataSourceTest {
    @get:Rule
    val mainDispatcher = TestDispatcherRule()

    private lateinit var localDataSource: LocalDataSource
    private val favoritesDao: FavoritesDao = mockk(relaxed = true)
    private val nowPlayingDao: NowPlayingDao = mockk(relaxed = true)
    private val topRatedDao: TopRatedDao = mockk(relaxed = true)
    private val upComingDao: UpComingDao = mockk(relaxed = true)
    private val popularDao: PopularDao = mockk(relaxed = true)

    @Before
    fun setup() {
        localDataSource = LocalDataSourceImpl(
            favoriteDao = favoritesDao,
            nowPlayingDao = nowPlayingDao,
            topRatedDao = topRatedDao,
            upComingDao = upComingDao,
            popularDao = popularDao
        )
    }

    @Test
    fun `GIVEN favorite movies entity mocks WHEN local storage saves favorite movies THEN assert favoriteDao inserts is called`() =
        runTest {
            val mockFavoritesMovies = MockUtils.mockFavoriteMoviesEntity()

            localDataSource.saveFavorites(mockFavoritesMovies)

            coVerify(exactly = 1) {
                favoritesDao.insertFavorite(mockFavoritesMovies)
            }
        }

    @Test
    fun `GIVEN favorite movies entity mocks WHEN local storage gets favorite movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockFavorites = MockUtils.mockFavoriteMoviesEntity()
            coEvery { favoritesDao.getFavoriteMovies() } returns flowOf(mockFavorites)

            val result = localDataSource.getFavoriteMovies()

            result.collect { favorites ->
                assertEquals(mockFavorites, favorites)
            }
            coVerify(exactly = 1) {
                favoritesDao.getFavoriteMovies()
            }
        }

    @Test
    fun `GIVEN upcoming movies entity mocks WHEN local storage saves upcoming movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockUpComingMovies = MockUtils.mockUpcomingMoviesEntity()

            coEvery { upComingDao.getAllUpcoming() } returns flowOf(mockUpComingMovies)
            val result = localDataSource.getUpcoming()

            result.collect { upcomingMovies ->
                assertEquals(mockUpComingMovies, upcomingMovies)
            }
            coVerify(exactly = 1) {
                upComingDao.getAllUpcoming()
            }
        }

    @Test
    fun `GIVEN now playing movies entity mocks WHEN local storage saves now playing movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockNowPlayingMovies = MockUtils.mockNowPlayingMoviesEntity()

            coEvery { nowPlayingDao.getAllNowPlaying() } returns flowOf(mockNowPlayingMovies)
            val result = localDataSource.getNowPlaying()

            result.collect { nowPlaying ->
                assertEquals(mockNowPlayingMovies, nowPlaying)
            }
            coVerify(exactly = 1) {
                nowPlayingDao.getAllNowPlaying()
            }
        }

    @Test
    fun `GIVEN now popular movies entity mocks WHEN local storage saves popular movies THEN  asserts mocks equality and calling once`() =
        runTest {
            val mockPopularMovies = MockUtils.mockPopularMoviesEntity()

            coEvery { popularDao.getAllPopular() } returns flowOf(mockPopularMovies)
            val result = localDataSource.getPopular()

            result.collect { popular ->
                assertEquals(mockPopularMovies, popular)
            }
            coVerify(exactly = 1) {
                popularDao.getAllPopular()
            }
        }

    @Test
    fun `GIVEN top rated movies entity mocks WHEN local storage saves popular movies THEN  asserts mocks equality and calling once`() =
        runTest {
            val mockTopRatedMovies = MockUtils.mockTopRatedMoviesEntity()

            coEvery { topRatedDao.getAllTopRated() } returns flowOf(mockTopRatedMovies)
            val result = localDataSource.getTopRated()

            result.collect { topRatedMovies ->
                assertEquals(mockTopRatedMovies, topRatedMovies)
            }
            coVerify(exactly = 1) {
                topRatedDao.getAllTopRated()
            }
        }

    @Test
    fun `GIVEN now playing movies entity mocks WHEN local storage saves now playing movie THEN asserts mocks equality and calling once`() =
        runTest {
            val mockNowPlayingMovies = MockUtils.mockNowPlayingMoviesEntity()
            coEvery { localDataSource.saveNowPlaying(mockNowPlayingMovies) } returns Unit

            nowPlayingDao.insertNowPlayingMovies(mockNowPlayingMovies)
            val result = localDataSource.getNowPlaying()

            result.collect { nowPlayingMovies ->
                assertEquals(mockNowPlayingMovies, nowPlayingMovies)
            }
            coVerify(exactly = 1) {
                nowPlayingDao.getAllNowPlaying()
            }
        }

    @Test
    fun `GIVEN popular movies entity mocks WHEN local storage saves popular movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockPopularMovies = MockUtils.mockPopularMoviesEntity()
            coEvery { localDataSource.savePopular(mockPopularMovies) } returns Unit

            popularDao.insertPopularMovies(mockPopularMovies)
            val result = localDataSource.getPopular()

            result.collect { popularMovies ->
                assertEquals(mockPopularMovies, popularMovies)
            }
            coVerify(exactly = 1) {
                popularDao.getAllPopular()
            }
        }

    @Test
    fun `GIVEN top rated movies entity mocks WHEN local storage top rated movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockTopRatedMovies = MockUtils.mockTopRatedMoviesEntity()
            coEvery { localDataSource.saveTopRated(mockTopRatedMovies) } returns Unit

            topRatedDao.insertTopRatedMovies(mockTopRatedMovies)
            val result = localDataSource.getTopRated()

            result.collect { topRatedMovies ->
                assertEquals(mockTopRatedMovies, topRatedMovies)
            }
            coVerify(exactly = 1) {
                topRatedDao.getAllTopRated()
            }
        }

    @Test
    fun `GIVEN upcoming movies entity mocks WHEN local storage upcoming movies THEN asserts mocks equality and calling once`() =
        runTest {
            val mockUpcomingMovies = MockUtils.mockUpcomingMoviesEntity()
            coEvery { localDataSource.saveUpcoming(mockUpcomingMovies) } returns Unit

            upComingDao.insertUpcomingMovies(mockUpcomingMovies)
            val result = localDataSource.getUpcoming()

            result.collect { upcomingMovies ->
                assertEquals(mockUpcomingMovies, upcomingMovies)
            }
            coVerify(exactly = 1) {
                upComingDao.getAllUpcoming()
            }
        }

    @After
    fun tearDown() {
        unmockkAll()
    }
}