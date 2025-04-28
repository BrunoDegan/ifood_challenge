package com.brunodegan.ifood_challenge.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.brunodegan.ifood_challenge.base.navigation.AppNavHost
import com.brunodegan.ifood_challenge.data.metrics.LocalMetrics
import com.brunodegan.ifood_challenge.ui.theme.IfoodChallengeTheme
import org.junit.Rule
import org.junit.Test

class AppNavHostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val bottomNavItem1: String = "Em cartaz"
    private val bottomNavItem2: String = "Por vir"
    private val bottomNavItem3: String = "Top Ranking"
    private val bottomNavItem4: String = "Populares"
    private val bottomNavItem5: String = "Favoritos"

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun testFavoriteMoviesListIsDisplayed() {
        composeTestRule.setContent {
            CompositionLocalProvider {
                LocalMetrics
                IfoodChallengeTheme {
                    AppNavHost()
                }
            }
        }

        composeTestRule.apply {
            onNodeWithText(bottomNavItem1).assertIsSelected()
        }

        composeTestRule.onNodeWithText(bottomNavItem1).assertExists()
        composeTestRule.onNodeWithText(bottomNavItem2).assertExists()
        composeTestRule.onNodeWithText(bottomNavItem3).assertExists()
        composeTestRule.onNodeWithText(bottomNavItem4).assertExists()
        composeTestRule.onNodeWithText(bottomNavItem5).assertExists()
    }
}