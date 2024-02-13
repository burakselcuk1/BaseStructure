package com.speakwithai.basestructure.ui.music.model

import com.speakwithai.basestructure.data.model.response.music.SongSampleApiModel

class MusicUiMapper {

    fun musicMapToUiModel(apiModel: SongSampleApiModel): SongSampleUiModel {
        return SongSampleUiModel(
            resultCount = apiModel.resultCount,
            results = apiModel.results
        )
    }

}