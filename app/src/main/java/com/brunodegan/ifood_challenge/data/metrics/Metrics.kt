package com.brunodegan.ifood_challenge.data.metrics

import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf

private const val SCREEN_NAME = "ScreenName"
private const val EVENT_TYPE = "EventType"

val LocalMetrics = staticCompositionLocalOf<Metrics> {
    MetricsImpl()
}

fun Metrics.onEnteredScreen(
    screenName: String
) {
    val eventData = AnalyticsData(
        eventType = AnalyticsData.EventType.SCREEN_NAME,
        extras = listOf(
            AnalyticsData.Param(
                key = SCREEN_NAME,
                value = screenName
            )
        )
    )
    logEvent(eventData)
}

fun Metrics.onEvent(
    event: String
) {
    val eventData = AnalyticsData(
        eventType = AnalyticsData.EventType.EVENT_TYPE,
        extras = listOf(
            AnalyticsData.Param(
                key = EVENT_TYPE,
                value = event
            )
        )
    )
    logEvent(eventData)
}

interface Metrics {
    fun logEvent(event: AnalyticsData)
}

class MetricsImpl : Metrics {
    override fun logEvent(analyticsData: AnalyticsData) {
        Log.d(
            analyticsData.eventType.name,
            "${analyticsData.extras.first().key} - ${analyticsData.extras.first().value}"
        )
    }
}