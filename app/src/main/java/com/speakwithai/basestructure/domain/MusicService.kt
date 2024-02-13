package com.speakwithai.basestructure.domain

import com.speakwithai.basestructure.data.model.response.music.SongSampleApiModel
import com.speakwithai.basestructure.common.api.MusicEndPoints.GET_MUSICS
import com.speakwithai.basestructure.common.api.MusicEndPoints.GET_SEARCH_MUSIC
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicService {

    @GET(GET_MUSICS)
    suspend fun getSongData(): SongSampleApiModel

    @GET(GET_SEARCH_MUSIC)
    suspend fun searchMusic(
        @Query("term") query: String,
        @Query("entity") entity: String): SongSampleApiModel

}