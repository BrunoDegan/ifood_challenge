package com.brunodegan.ifood_challenge.base

import android.app.Application
import android.graphics.Bitmap
import coil.Coil
import coil.ImageLoader
import com.brunodegan.ifood_challenge.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.plugin.module.dsl.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(
            ImageLoader
                .Builder(this)
                .crossfade(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build(),
        )

        startKoin<AppModule> {
            androidLogger()
            androidContext(this@MainApplication)
        }
    }
}
