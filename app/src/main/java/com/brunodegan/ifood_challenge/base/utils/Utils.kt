package com.brunodegan.ifood_challenge.base.utils

import java.text.SimpleDateFormat
import java.util.Locale

const val MOVIES_POSTER_CDN_URL = "https://image.tmdb.org/t/p/original"

fun formatUsDateToBrDate(usDate: String): String {
    val usDateFormat = SimpleDateFormat("mm-dd-yyyy", Locale.US)
    val brDateFormat = SimpleDateFormat("dd/mm/yyyy", Locale("pt", "BR"))
    val date = usDateFormat.parse(usDate)
    return brDateFormat.format(date ?: return "")
}