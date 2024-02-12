package com.speakwithai.basestructure.ui.wallpaper.model

import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

data class ResultUiModel(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: ArrayList<Photo>,
    val total_results: Int
)
