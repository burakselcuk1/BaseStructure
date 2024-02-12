package com.speakwithai.basestructure.ui.wallpaper

import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WallpaperUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun getCuratedPhotos(): Flow<Resoource<ResultUiModel>> =
        flow {
            val resource = repository.getCuratedPhotos()
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else if (resource is Resoource.Error) {
                emit(Resoource.Error(resource.throwable))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getSearchPhotos(query: String): Flow<Resoource<ResultUiModel>> =
        flow {
            val resource = repository.getSearchPhotos(query)
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else if (resource is Resoource.Error) {
                emit(Resoource.Error(resource.throwable))
            }
        }.flowOn(Dispatchers.IO)

}