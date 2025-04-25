package com.brunodegan.ifood_challenge.base.network

import com.brunodegan.ifood_challenge.data.api.RestApiService
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.ACCEPT
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.APPLICATION_JSON
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.AUTHORIZATION_HEADER
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.BASE_URL
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.BEARER_TOKEN
import com.brunodegan.ifood_challenge.data.api.RestApiService.Companion.CONTENT_TYPE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private inline fun <reified T : Any> Retrofit.createApi(): T = create(T::class.java)

@Module
@ComponentScan("com.brunodegan.ifood_challenge")
class NetworkModule {

    @Single
    fun provideRestClient(): RestApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .createApi<RestApiService>()
    }

    private fun provideHttpInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addNetworkInterceptor(provideHttpInterceptor())
            .addInterceptor { chain ->
                with(chain) {
                    val request = request().newBuilder()
                        .addHeader(ACCEPT, APPLICATION_JSON)
                        .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN)
                        .build()
                    proceed(request)
                }
            }
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    companion object {
        private const val REQUEST_TIMEOUT = 60L
    }
}
