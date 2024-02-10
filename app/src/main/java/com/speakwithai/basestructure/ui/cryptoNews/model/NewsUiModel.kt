package com.speakwithai.basestructure.ui.cryptoNews.model

import com.speakwithai.basestructure.data.model.response.cryptoNews.NewsData

data class NewsUiModel(
    val data: List<NewsData>?,
    val message: String?,
    val promoted: List<Any>?,
    val type: Int?
)
