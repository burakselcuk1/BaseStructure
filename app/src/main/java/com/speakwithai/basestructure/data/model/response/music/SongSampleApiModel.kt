package com.speakwithai.basestructure.data.model.response.music

import com.speakwithai.basestructure.data.model.response.music.ResultX

data class SongSampleApiModel(
    val resultCount: Int,
    val results: ArrayList<ResultX>
)
