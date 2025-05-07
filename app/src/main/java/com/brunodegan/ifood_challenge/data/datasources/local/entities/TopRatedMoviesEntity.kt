package com.brunodegan.ifood_challenge.data.datasources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_rated")
data class TopRatedMoviesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "posterPath")
    val posterPath: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)

fun List<TopRatedMoviesEntity>.update(
    favoritesMovies: List<FavoriteMoviesEntity>
): List<TopRatedMoviesEntity> {
    return this.map { movie ->
        val isFavorite = favoritesMovies.any { it.id == movie.id }
        movie.copy(isFavorite = isFavorite == true)
    }
}