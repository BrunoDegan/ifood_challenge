package com.brunodegan.androidplayground.base.network

import com.brunodegan.androidplayground.BuildConfig
import com.brunodegan.androidplayground.data.api.RestApiService
import com.brunodegan.androidplayground.data.api.RestApiService.Companion.ACCEPT
import com.brunodegan.androidplayground.data.api.RestApiService.Companion.APPLICATION_JSON
import com.brunodegan.androidplayground.data.api.RestApiService.Companion.AUTHORIZATION_HEADER
import com.brunodegan.androidplayground.data.api.RestApiService.Companion.BASE_URL
import com.brunodegan.androidplayground.data.api.RestApiService.Companion.CONTENT_TYPE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private inline fun <reified T : Any> Retrofit.createApi(): T = create(T::class.java)

@Module
@Configuration
@ComponentScan("com.brunodegan.androidplayground.base.network")
class NetworkModule {
    @Singleton
    fun provideRestClient(): RestApiService =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .createApi<RestApiService>()

    private fun provideHttpInterceptor(): HttpLoggingInterceptor =
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

    private fun provideHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(provideHttpInterceptor())
            .addInterceptor { chain ->
                with(chain) {
                    val request =
                        request()
                            .newBuilder()
                            .addHeader(ACCEPT, APPLICATION_JSON)
                            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                            .addHeader(AUTHORIZATION_HEADER, BuildConfig.TMDB_BEARER_TOKEN)
                            .build()
                    proceed(request)
                }
            }.readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()

    private fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    companion object {
        private const val REQUEST_TIMEOUT = 60L
    }
}
