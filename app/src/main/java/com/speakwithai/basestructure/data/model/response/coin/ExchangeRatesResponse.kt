package com.speakwithai.basestructure.data.model.response.coin

import com.speakwithai.basestructure.data.model.response.coin.ExchangeRate

data class ExchangeRatesResponse(
    val rates: Map<String, ExchangeRate>
)
