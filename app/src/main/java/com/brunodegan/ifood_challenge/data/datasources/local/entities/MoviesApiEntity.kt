package com.brunodegan.ifood_challenge.data.datasources.local.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MoviesApiDataModel(@SerializedName("results") val results: List<Movies>) :
    ApiData()

data class Movies(
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
) : ApiData()

@Parcelize
open class BaseApiData : Parcelable

typealias ApiData = BaseApiData

