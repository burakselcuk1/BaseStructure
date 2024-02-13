package com.speakwithai.basestructure.repository.repositoryImpl

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.domain.MusicService
import com.speakwithai.basestructure.repository.MusicRepository
import com.speakwithai.basestructure.ui.music.model.MusicUiMapper
import com.speakwithai.basestructure.ui.music.model.SongSampleUiModel
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val service: MusicService,
    private var mapper: MusicUiMapper
): MusicRepository {

    override suspend fun getMusic(): Resoource<SongSampleUiModel> = try {
        val apiModel = service.getSongData()
        val uiModel = mapper.musicMapToUiModel(apiModel)
        Resoource.Success(uiModel)
    } catch (e: Exception){
        Resoource.Error(e)
    }

    override suspend fun getSearcMusic(query: String, entity: String): Resoource<SongSampleUiModel> = try {
        val apiModel = service.searchMusic(query,entity)
        val uiModel = mapper.musicMapToUiModel(apiModel)
        Resoource.Success(uiModel)
    }catch (e: Exception){
        Resoource.Error(e)
    }

}