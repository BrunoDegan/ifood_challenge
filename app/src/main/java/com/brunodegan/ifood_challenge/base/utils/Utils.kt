package com.brunodegan.ifood_challenge.base.utils

import java.text.SimpleDateFormat
import java.util.Locale

private const val MOVIES_POSTER_CDN_URL = "https://image.tmdb.org/t/p/original"

fun String?.formatFullCDNUrl(): String {
    if (this.isNullOrEmpty()) return ""
    return "$MOVIES_POSTER_CDN_URL$this"
}

fun String?.formatUsDateToBrDate(): String {
    if (this.isNullOrEmpty()) return ""
    return try {
        val usDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val brDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        brDateFormat.format(usDateFormat.parse(this) ?: return "")
    } catch (e: Exception) {
        ""
    }
}

fun isCacheValid(lastUpdated: Long, expirationTime: Long = 10L): Boolean {
    return System.currentTimeMillis() - lastUpdated < expirationTime
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}