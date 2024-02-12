package com.speakwithai.basestructure.ui.wallpaper.adapter

import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

interface EditorPicksClickListener {
    fun editorPicksItemClick(photo: Photo)
}