package com.brunodegan.ifood_challenge.data.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UpComingDao {
    @Query("SELECT * FROM now_playing")
    fun getAllUpcoming(): Flow<List<UpcomingMoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUpcomingMovies(upcomingMovies: List<UpcomingMoviesEntity>)
}