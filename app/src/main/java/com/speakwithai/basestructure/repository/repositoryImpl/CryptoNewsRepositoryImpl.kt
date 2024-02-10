package com.speakwithai.basestructure.repository.repositoryImpl


import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.domain.CryptoNewsApi
import com.speakwithai.basestructure.repository.CryptoNewsRepository
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiMapper
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiModel
import javax.inject.Inject

class CryptoNewsRepositoryImpl @Inject constructor(
    private val newService: CryptoNewsApi,
    private val newsMapper: NewsUiMapper,
) : CryptoNewsRepository {
    override suspend fun getCryptoNews(): Resoource<NewsUiModel> = try {
        val response = newService.getLatestNews()
        val uiModel = newsMapper.mapToNewsUiModel(response)
        Resoource.Success(uiModel)
    }catch (e: Exception){
        Resoource.Error(e)
    }
}