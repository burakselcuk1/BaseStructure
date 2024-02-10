package com.speakwithai.basestructure.repository

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import retrofit2.Response

interface CoinsRepository {
    suspend fun getCoins(): Resoource<List<CoinUiModel>>?
}