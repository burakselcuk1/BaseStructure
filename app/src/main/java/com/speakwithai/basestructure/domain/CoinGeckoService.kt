package com.speakwithai.basestructure.domain


import com.speakwithai.basestructure.data.model.response.coin.CoinApiModel
import com.speakwithai.basestructure.data.model.response.coin.CoinChartResponse
import com.speakwithai.basestructure.data.model.response.coin.ExchangeRatesResponse
import com.speakwithai.basestructure.data.model.response.coin.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoService {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("ids") ids: String,
        @Query("vs_currency") currency: String
    ): List<CoinApiModel>

    @GET("coins/{id}/market_chart")
    suspend fun getCoinChartData(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") period: String
    ): Response<CoinChartResponse>


    @GET("search")
    suspend fun search(@Query("query") query: String): Response<SearchResult>


    @GET("exchange_rates")
    suspend fun getExchangeRates(): Response<ExchangeRatesResponse>

}