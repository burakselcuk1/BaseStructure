package com.speakwithai.basestructure.repository.repositoryImpl

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.domain.CoinGeckoService
import com.speakwithai.basestructure.repository.CoinsRepository
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import com.speakwithai.basestructure.ui.crypto.model.CoinsMapper
import retrofit2.Response
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val coinGeckoService: CoinGeckoService,
    private val mapper: CoinsMapper
) : CoinsRepository {

    override suspend fun getCoins(): Resoource<List<CoinUiModel>>? = try {
        val response = coinGeckoService.getCoins("", "eur")
        val uiModel = mapper.coinListMapToUiModelList(response)
        Resoource.Success(uiModel)
    }catch (e: Exception){
        Resoource.Error(e)
    }

}