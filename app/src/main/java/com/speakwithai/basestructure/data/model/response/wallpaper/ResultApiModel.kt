package com.speakwithai.basestructure.data.model.response.wallpaper

import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

data class ResultApiModel(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: ArrayList<Photo>,
    val total_results: Int
)
