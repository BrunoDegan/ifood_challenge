package com.brunodegan.ifood_challenge.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brunodegan.ifood_challenge.data.datasources.local.daos.NowPlayingDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.PopularDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.TopRatedDao
import com.brunodegan.ifood_challenge.data.datasources.local.daos.UpComingDao
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity

@Database(
    entities = [
        NowPlayingMoviesEntity::class,
        PopularMoviesEntity::class,
        TopRatedMoviesEntity::class,
        UpcomingMoviesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nowPlayingDao(): NowPlayingDao
    abstract fun popularDao(): PopularDao
    abstract fun topRatedDao(): TopRatedDao
    abstract fun upComingDao(): UpComingDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}