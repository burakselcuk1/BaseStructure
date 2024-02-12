package com.speakwithai.basestructure.repository

import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.speakwithai.basestructure.common.Resoource

interface WallpaperRepository {
    suspend fun getCuratedPhotos(): Resoource<ResultUiModel>
    suspend fun getSearchPhotos(query : String):Resoource<ResultUiModel>

}