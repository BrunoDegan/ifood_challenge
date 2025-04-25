package com.brunodegan.ifood_challenge.data.metrics

data class AnalyticsData(
    val eventType: EventType,
    val extras: List<Param> = emptyList(),
) {
    data class Param(val key: String, val value: String)

    enum class EventType(eventType: String) {
        EVENT_TYPE("event_type"),
        SCREEN_NAME("screen_name"),
    }
}