package com.speakwithai.basestructure.di

import com.speakwithai.basestructure.domain.CryptoNewsApi
import com.speakwithai.basestructure.repository.CryptoNewsRepository
import com.speakwithai.basestructure.repository.repositoryImpl.CryptoNewsRepositoryImpl
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
    fun provideCryptoNewsRepository(
        cryptoNewsService: CryptoNewsApi,
        newsUiMapper: NewsUiMapper
    ): CryptoNewsRepository {
        return CryptoNewsRepositoryImpl(cryptoNewsService, newsUiMapper)
    }
}
