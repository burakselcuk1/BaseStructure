package com.speakwithai.basestructure.ui.wallpaper.navigation

import com.speakwithai.basestructure.base.Navigation
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo

interface WallpaperNavigation: Navigation {

    fun navigateToWallpaperDetailFragment(photo: Photo)

}