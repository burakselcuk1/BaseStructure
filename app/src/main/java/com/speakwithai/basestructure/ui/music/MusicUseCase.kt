package com.speakwithai.basestructure.ui.music

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.repository.MusicRepository
import com.speakwithai.basestructure.ui.music.model.SongSampleUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MusicUseCase @Inject constructor(
    private val repository: MusicRepository
) {
    suspend fun getMusic(): Flow<Resoource<SongSampleUiModel>> =
        flow {
            val resource = repository.getMusic()
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else if (resource is Resoource.Error) {
                emit(Resoource.Error(resource.throwable))
            }
        }.flowOn(Dispatchers.IO)


    suspend fun getSearchMusic(query: String, entity: String): Flow<Resoource<SongSampleUiModel>> =
        flow {
            val resource = repository.getSearcMusic(query, entity)
            if (resource is Resoource.Success){
                emit(Resoource.Success(resource.data))
            }else if (resource is Resoource.Error) {
                emit(Resoource.Error(resource.throwable))
            }
        }.flowOn(Dispatchers.IO)
}