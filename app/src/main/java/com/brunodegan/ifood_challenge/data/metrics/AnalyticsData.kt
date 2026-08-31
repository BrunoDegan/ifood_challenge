package com.brunodegan.ifood_challenge.data.metrics

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.persistentListOf

@Stable
data class AnalyticsData(
    val eventType: EventType,
    val extras: List<Param> = persistentListOf(),
) {
    data class Param(val key: String, val value: String)

    enum class EventType(eventType: String) {
        EVENT_TYPE("event_type"),
        SCREEN_NAME("screen_name"),
    }
}