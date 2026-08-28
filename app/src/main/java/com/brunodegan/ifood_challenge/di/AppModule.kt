package com.brunodegan.ifood_challenge.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@KoinApplication
@Module
@Configuration
@ComponentScan(
    "com.brunodegan.ifood_challenge.ui",
    "com.brunodegan.ifood_challenge.domain",
    "com.brunodegan.ifood_challenge.data",
    "com.brunodegan.ifood_challenge.base.dispatchers"
)
class AppModule