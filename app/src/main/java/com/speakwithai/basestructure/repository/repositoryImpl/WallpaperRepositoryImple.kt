package com.speakwithai.basestructure.repository.repositoryImpl

import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.speakwithai.basestructure.ui.wallpaper.model.WallpaperUiMapper
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.domain.WallpaperService
import com.speakwithai.basestructure.repository.WallpaperRepository
import javax.inject.Inject

class WallpaperRepositoryImple @Inject constructor(
    private val service: WallpaperService,
    private val mapper: WallpaperUiMapper
): WallpaperRepository {
    override suspend fun getCuratedPhotos(): Resoource<ResultUiModel> = try {
        val apiModel = service.getCuratedPhotos()
        val uiModel = mapper.wallpaperMapToUiModel(apiModel)
        Resoource.Success(uiModel)
    }catch (e: Exception){
        Resoource.Error(e)
    }

    override suspend fun getSearchPhotos(query: String): Resoource<ResultUiModel> = try {
        val apiModel = service.searchPhotos(query)
        val uiModel = mapper.wallpaperMapToUiModel(apiModel)
        Resoource.Success(uiModel)
    }catch (e: Exception){
        Resoource.Error(e)
    }
}