package com.speakwithai.basestructure.di

import com.speakwithai.basestructure.ui.wallpaper.model.WallpaperUiMapper
import com.speakwithai.basestructure.domain.CoinGeckoService
import com.speakwithai.basestructure.domain.CryptoNewsApi
import com.speakwithai.basestructure.domain.WallpaperService
import com.speakwithai.basestructure.repository.CoinsRepository
import com.speakwithai.basestructure.repository.CryptoNewsRepository
import com.speakwithai.basestructure.repository.WallpaperRepository
import com.speakwithai.basestructure.repository.repositoryImpl.CoinsRepositoryImpl
import com.speakwithai.basestructure.repository.repositoryImpl.CryptoNewsRepositoryImpl
import com.speakwithai.basestructure.repository.repositoryImpl.WallpaperRepositoryImple
import com.speakwithai.basestructure.ui.crypto.model.CoinsMapper
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCryptoNewsUiMapper(): NewsUiMapper {
        return NewsUiMapper()
    }

    @Provides
    @Singleton
    fun provideWallpaperUiMapper(): WallpaperUiMapper {
        return WallpaperUiMapper()
    }

    @Provides
    @Singleton
    fun wallpaperRepository(
        wallpaperService: WallpaperService,
        wallpaperUiMapper: WallpaperUiMapper
    ): WallpaperRepository {
        return WallpaperRepositoryImple(wallpaperService, wallpaperUiMapper)
    }

    @Provides
    @Singleton
    fun provideCryptoNewsRepository(
        cryptoNewsService: CryptoNewsApi,
        newsUiMapper: NewsUiMapper
    ): CryptoNewsRepository {
        return CryptoNewsRepositoryImpl(cryptoNewsService, newsUiMapper)
    }

    @Provides
    @Singleton
    fun provideCoinUiMapper(): CoinsMapper {
        return CoinsMapper()
    }

    @Provides
    @Singleton
    fun cryptoRepository(
        coinGeckoService: CoinGeckoService,
        coinsMapper: CoinsMapper
    ): CoinsRepository {
        return CoinsRepositoryImpl(coinGeckoService, coinsMapper)
    }
}
