package com.speakwithai.basestructure.ui.wallpaper.model

import com.speakwithai.basestructure.data.model.response.wallpaper.ResultApiModel

class WallpaperUiMapper {

    fun wallpaperMapToUiModel(apiModel: ResultApiModel): ResultUiModel {
        return ResultUiModel(
            next_page = apiModel.next_page,
            page = apiModel.page,
            per_page = apiModel.per_page,
            total_results = apiModel.total_results,
            photos = apiModel.photos
        )
    }

}