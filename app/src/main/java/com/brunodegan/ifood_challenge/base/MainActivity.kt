package com.brunodegan.ifood_challenge.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.brunodegan.ifood_challenge.base.navigation.AppNavHost
import com.brunodegan.ifood_challenge.data.metrics.LocalMetrics
import com.brunodegan.ifood_challenge.ui.theme.IfoodChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider {
                LocalMetrics
                IfoodChallengeTheme {
                    AppNavHost()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IfoodChallengeTheme {
        AppNavHost()
    }
}