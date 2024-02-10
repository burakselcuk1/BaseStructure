package com.speakwithai.basestructure.data.model.response.cryptoNews

import com.google.gson.annotations.SerializedName
import com.speakwithai.basestructure.data.model.response.cryptoNews.NewsData

data class NewsApiModel(
    @SerializedName("Data")
    val data: List<NewsData>?,

    @SerializedName("Message")
    val message: String?,

    @SerializedName("Promoted")
    val promoted: List<Any>?,

    @SerializedName("Type")
    val type: Int?
)
