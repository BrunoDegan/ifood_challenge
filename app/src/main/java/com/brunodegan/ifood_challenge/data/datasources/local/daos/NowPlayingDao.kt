package com.brunodegan.ifood_challenge.data.datasources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NowPlayingDao {
    @Query("SELECT * FROM now_playing")
    fun getAllNowPlaying(): Flow<List<NowPlayingMoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNowPlayingMovies(nowPlayingMovies: List<NowPlayingMoviesEntity>)
}