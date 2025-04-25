package com.brunodegan.ifood_challenge.data.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PopularDao {
    @Query("SELECT * FROM popular")
    fun getAllPopular(): Flow<List<PopularMoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopularMovies(upcomingMovies: List<PopularMoviesEntity>)
}