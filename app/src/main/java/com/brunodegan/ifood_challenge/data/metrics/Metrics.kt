package com.brunodegan.ifood_challenge.data.metrics

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.core.annotation.Single

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


@Stable
interface Metrics {
    fun logEvent(event: AnalyticsData)
    fun onEvent(event: String)
}

@Single
class MetricsImpl : Metrics {
    override fun onEvent(
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
        this.logEvent(eventData)
    }

    override fun logEvent(event: AnalyticsData) {
        Log.d(
            event.eventType.name,
            "${event.extras.first().key} - ${event.extras.first().value}"
        )
    }
}