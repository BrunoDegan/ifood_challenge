package com.brunodegan.ifood_challenge.data.metrics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun TrackScreen(
    screenName: String,
    analyticsHelper: Metrics = LocalMetrics.current,
) = DisposableEffect(screenName) {
    analyticsHelper.onEnteredScreen(screenName = screenName)
    onDispose {
        // No-op
    }
}