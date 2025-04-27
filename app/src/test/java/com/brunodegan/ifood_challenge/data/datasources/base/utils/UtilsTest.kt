package com.brunodegan.ifood_challenge.data.datasources.base.utils

import com.brunodegan.ifood_challenge.base.utils.formatFullCDNUrl
import com.brunodegan.ifood_challenge.base.utils.formatUsDateToBrDate
import com.brunodegan.ifood_challenge.base.utils.isCacheValid
import io.mockk.every
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun `Test CDN URL formatter`() {
        val mockUrl = "/path/to/image.jpg"
        val result = mockUrl.formatFullCDNUrl()
        assert(result == "https://image.tmdb.org/t/p/original/path/to/image.jpg")
    }

    @Test
    fun `Test formatUsDateToBrDate utils`() {
        val mockDate = "2023-10-01"
        val result = mockDate.formatUsDateToBrDate()
        assert(result == "01/10/2023")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}