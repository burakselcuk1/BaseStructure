package com.speakwithai.basestructure.ui.cryptoNews.model

import com.speakwithai.basestructure.data.model.response.cryptoNews.NewsApiModel

class NewsUiMapper {
    fun  mapToNewsUiModel(apiModel: NewsApiModel): NewsUiModel {
        return NewsUiModel(
            data = apiModel.data,
            message = apiModel.message,
            type = apiModel.type,
            promoted = apiModel.promoted
        )
    }

    fun coinNewsListMapToUiModelList(apiModelList: List<NewsApiModel>): List<NewsUiModel> {
        return apiModelList.map { mapToNewsUiModel(it) }
    }
}