package com.brunodegan.ifood_challenge.data.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunodegan.ifood_challenge.data.datasources.local.entities.FavoriteMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getFavoriteMovies(): Flow<List<FavoriteMoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteMovie: List<FavoriteMoviesEntity>)

    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteFavoriteById(id: Int)
}