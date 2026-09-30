package com.brunodegan.ifood_challenge.base.database

import android.content.Context
import androidx.room.Room
import com.brunodegan.ifood_challenge.base.database.AppDatabase.Companion.DATABASE_NAME
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@Configuration
@ComponentScan("com.brunodegan.ifood_challenge.base.database")
object DatabaseModule {
    @Singleton
    fun provideNowPlayingDao(database: AppDatabase) = database.nowPlayingDao()

    @Singleton
    fun provideTopRatedDao(database: AppDatabase) = database.topRatedDao()

    @Singleton
    fun provideUpComingDao(database: AppDatabase) = database.upComingDao()

    @Singleton
    fun providePopularDao(database: AppDatabase) = database.popularDao()

    @Singleton
    fun provideFavoriteDao(database: AppDatabase) = database.favoritesDao()

    @Singleton
    fun initializeDb(context: Context) =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME,
            ).build()
}
