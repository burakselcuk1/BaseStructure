package com.speakwithai.basestructure.ui.wallpaper.adapter

import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

interface ResultClickListener {
    fun editorResultItemClick(photo: Photo)
}