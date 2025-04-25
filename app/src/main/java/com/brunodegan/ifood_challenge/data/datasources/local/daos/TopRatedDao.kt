package com.brunodegan.ifood_challenge.data.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopRatedDao {
    @Query("SELECT * FROM top_rated")
    fun getAllTopRated(): Flow<List<TopRatedMoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopRatedMovies(upcomingMovies: List<TopRatedMoviesEntity>)
}