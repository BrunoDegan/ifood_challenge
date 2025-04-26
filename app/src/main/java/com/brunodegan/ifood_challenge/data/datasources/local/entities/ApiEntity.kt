package com.brunodegan.ifood_challenge.data.datasources.local.entities

import android.os.Parcelable
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.MEDIA_TYPE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MoviesApiDataResponse(@SerializedName("results") val results: List<Movies>) :
    ApiData()

data class Movies(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
) : ApiData()

data class AddToFavoritesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: String,
    @SerializedName("status_message") val statusMessage: String,
) : ApiData()

data class AddToFavoritesRequest(
    @SerializedName("media_type") val mediaType: String = MEDIA_TYPE,
    @SerializedName("media_id") val mediaId: Int,
    @SerializedName("favorite") val favorite: Boolean,
) : ApiData()

data class FavoriteMoviesResponse(
    val success: Boolean,
    val statusMessage: String,
    val statusCode: String
) : ApiData()

@Parcelize
open class BaseApiData : Parcelable

typealias ApiData = BaseApiData

