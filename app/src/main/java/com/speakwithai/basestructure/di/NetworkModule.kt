package com.speakwithai.basestructure.di

import com.speakwithai.basestructure.common.API_KEY
import com.speakwithai.basestructure.common.utils.Constans.Companion.BASE_URL
import com.speakwithai.basestructure.services.Api
import com.google.gson.Gson
import com.speakwithai.basestructure.common.utils.Constans.Companion.CRYPTO_API_URL
import com.speakwithai.basestructure.common.utils.Constans.Companion.CRYPTO_NEWS_API_URL
import com.speakwithai.basestructure.domain.CoinGeckoService
import com.speakwithai.basestructure.domain.CryptoNewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import okhttp3.Interceptor

import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val authInterceptor = AuthInterceptor()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor) // Loglama ekleyin
        .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
        .readTimeout(30, TimeUnit.SECONDS) // socket timeout
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingIntercepter: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingIntercepter)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    @Named("cryptoNewsRetrofit")
    fun provideCryptoNewsRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CRYPTO_NEWS_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCryptoNewsAPI(@Named("cryptoNewsRetrofit") retrofit: Retrofit): CryptoNewsApi {
        return retrofit.create(CryptoNewsApi::class.java)
    }

    @Provides
    @Singleton
    @Named("cryptoRetrofit")
    fun provideSecondRetrofit() = Retrofit.Builder()
        .baseUrl(CRYPTO_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideSecondService(@Named("cryptoRetrofit") retrofit: Retrofit) = retrofit.create(
        CoinGeckoService::class.java)

}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${API_KEY.MY_API_KEY}")
            .build()
        return chain.proceed(request)
    }
}

