package com.brunodegan.ifood_challenge.base.database

import android.content.Context
import androidx.room.Room
import com.brunodegan.ifood_challenge.base.database.AppDatabase.Companion.DATABASE_NAME
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.brunodegan.ifood_challenge")
object DatabaseModule {

    @Single
    fun provideNowPlayingDao(database: AppDatabase) = database.nowPlayingDao()

    @Single
    fun provideTopRatedDao(database: AppDatabase) = database.topRatedDao()

    @Single
    fun provideUpComingDao(database: AppDatabase) = database.upComingDao()

    @Single
    fun providePopularDao(database: AppDatabase) = database.popularDao()

    @Single
    fun provideFavoriteDao(database: AppDatabase) = database.favoritesDao()

    @Single
    fun initializeDb(context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()
}
