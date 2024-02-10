package com.speakwithai.basestructure.data.model.response.coin


data class ExchangeRate(
    val name: String,
    val unit: String,
    val value: Float,
    val type: String
)
