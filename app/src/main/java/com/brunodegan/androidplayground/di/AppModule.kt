package com.brunodegan.androidplayground.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@KoinApplication
@Module
@Configuration
@ComponentScan(
    "com.brunodegan.androidplayground.ui",
    "com.brunodegan.androidplayground.domain",
    "com.brunodegan.androidplayground.data",
    "com.brunodegan.androidplayground.base.dispatchers",
)
class AppModule
