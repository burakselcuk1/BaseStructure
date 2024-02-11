package com.speakwithai.basestructure.ui.crypto.repository

import com.speakwithai.basestructure.domain.CoinGeckoService

import javax.inject.Inject

class CoinsRepository @Inject constructor(
    private val coinGeckoService: CoinGeckoService,
){
    suspend fun getCoins() = coinGeckoService.getCoins("", "eur")


    suspend fun getCoinChartData(id: String, period: String ) = coinGeckoService.getCoinChartData(id, "eur", period)


    suspend fun getExchangeRates() = coinGeckoService.getExchangeRates()


}