package com.speakwithai.basestructure.data.model.response.coin

import java.io.Serializable


data class SearchedCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val large: String,
    val market_cap_rank: Int?
): Serializable {
    val marketCapRank: String?
        get() = if(market_cap_rank != null) "#$market_cap_rank" else null
}
