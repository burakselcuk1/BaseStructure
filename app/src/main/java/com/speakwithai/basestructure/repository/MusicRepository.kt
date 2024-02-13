package com.speakwithai.basestructure.repository

import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.music.model.SongSampleUiModel

interface MusicRepository {

    suspend fun getMusic(): Resoource<SongSampleUiModel>
    suspend fun getSearcMusic(query: String, entity: String): Resoource<SongSampleUiModel>

}