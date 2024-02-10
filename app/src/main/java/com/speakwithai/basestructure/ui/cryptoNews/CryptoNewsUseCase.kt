package com.speakwithai.basestructure.ui.cryptoNews


import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.repository.CryptoNewsRepository
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CryptoNewsUseCase @Inject constructor(
    private val repository: CryptoNewsRepository
) {
    suspend fun getCoinNews() : Flow<Resoource<NewsUiModel>> =
        flow {
            val  resource = repository.getCryptoNews()
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else{
            }
        }.flowOn(Dispatchers.IO)
}