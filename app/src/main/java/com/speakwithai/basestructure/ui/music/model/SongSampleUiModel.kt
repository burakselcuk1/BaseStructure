package com.speakwithai.basestructure.ui.music.model

import com.speakwithai.basestructure.data.model.response.music.ResultX

data class SongSampleUiModel(
    val resultCount: Int,
    val results: ArrayList<ResultX>
)
