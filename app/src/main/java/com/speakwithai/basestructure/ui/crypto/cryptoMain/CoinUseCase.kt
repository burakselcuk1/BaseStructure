package com.speakwithai.basestructure.ui.crypto.cryptoMain

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.repository.CoinsRepository
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CoinUseCase @Inject constructor(
    private val coinsRepository: CoinsRepository
) {
    suspend fun getCoins() : Flow<Resoource<List<CoinUiModel>>> =
        flow {
            val  resource = coinsRepository.getCoins()
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else{
            }
    }.flowOn(Dispatchers.IO)
}