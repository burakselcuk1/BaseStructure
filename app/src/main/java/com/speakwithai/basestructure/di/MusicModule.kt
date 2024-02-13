package com.speakwithai.basestructure.di

import com.speakwithai.basestructure.common.utils.Constans
import com.speakwithai.basestructure.domain.MusicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Singleton
    @Provides
    @Named("MusicRetrofit")
    fun provideOkHttpWallpaperClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor() // Ekledik
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Ekledik

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor) // Ekledik
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0")
                    .build()
                chain.proceed(request)
            }
            .build()
        return client
    }

    @Singleton
    @Provides
    @Named("MusicRetrofit")
    fun provideRetrofit(@Named("WallpaperRetrofit") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constans.MUSIC_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(@Named("MusicRetrofit") retrofit: Retrofit): MusicService {
        return retrofit.create(MusicService::class.java)
    }
}