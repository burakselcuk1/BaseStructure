package com.speakwithai.basestructure.di

import com.speakwithai.basestructure.common.utils.Constans.Companion.WALLPAPER_URL
import com.speakwithai.basestructure.domain.WallpaperService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Ekledik
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WallpaperModule {

    @Singleton
    @Provides
    @Named("WallpaperRetrofit")
    fun provideOkHttpWallpaperClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor() // Ekledik
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Ekledik

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor) // Ekledik
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "3lzolcwbXK1s657xIE7BenmA7VTJF5cmIkPzKYA56tIdOmcnf8WCUzed")
                    .build()
                chain.proceed(request)
            }
            .build()
        return client
    }

    @Singleton
    @Provides
    @Named("WallpaperRetrofit")
    fun provideRetrofit(@Named("WallpaperRetrofit") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WALLPAPER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(@Named("WallpaperRetrofit") retrofit: Retrofit): WallpaperService {
        return retrofit.create(WallpaperService::class.java)
    }
}
