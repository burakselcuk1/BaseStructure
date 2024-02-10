package com.speakwithai.basestructure.repository

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiModel

interface CryptoNewsRepository {

    suspend fun getCryptoNews(): Resoource<NewsUiModel>


}