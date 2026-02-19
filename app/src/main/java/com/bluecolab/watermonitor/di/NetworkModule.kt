package com.bluecolab.watermonitor.di

import com.bluecolab.watermonitor.data.remote.api.BlueColabApi
import com.bluecolab.watermonitor.data.remote.api.UsgsFloodApi
import com.bluecolab.watermonitor.data.remote.api.UsgsWaterApi
import com.bluecolab.watermonitor.util.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BLUE_COLAB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideBlueColabApi(retrofit: Retrofit): BlueColabApi {
        return retrofit.create(BlueColabApi::class.java)
    }

    @Provides
    @Singleton
    @Named("usgsWater")
    fun provideUsgsWaterApi(okHttpClient: OkHttpClient, moshi: Moshi): UsgsWaterApi {
        return Retrofit.Builder()
            .baseUrl(Constants.USGS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(UsgsWaterApi::class.java)
    }

    @Provides
    @Singleton
    @Named("usgsFlood")
    fun provideUsgsFloodApi(okHttpClient: OkHttpClient, moshi: Moshi): UsgsFloodApi {
        return Retrofit.Builder()
            .baseUrl(Constants.USGS_RTFI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(UsgsFloodApi::class.java)
    }
}
