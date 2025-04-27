package com.brunodegan.ifood_challenge.data.datasources.data.mappers

import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils
import com.brunodegan.ifood_challenge.data.datasources.utils.MockUtils.MOVIES_POSTER_CDN_URL
import com.brunodegan.ifood_challenge.data.mappers.PopularDataMapper
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PopularDataMapperTest {

    private lateinit var mapper: PopularDataMapper

    @Before
    fun setup() {
        mapper = PopularDataMapper()
    }

    @Test
    fun `GIVEN mock add to favorites response WHEN map THEN asserts equality`() {
        val response = MockUtils.mockMoviesApiDataResponse()
        val result = mapper.map(response)

        assertEquals(expected = response.results.first().id, actual = result.first().id)
        assertEquals(expected = response.results.first().title, actual = result.first().title)
        assertEquals(expected = response.results.first().overview, actual = result.first().overview)
        assertEquals(
            expected = response.results.first().releaseDate.formatUsDateToBrDate(),
            actual = result.first().releaseDate
        )
        assertEquals(
            expected = MOVIES_POSTER_CDN_URL + response.results.first().posterPath,
            actual = result.first().posterPath
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}