package com.speakwithai.basestructure.ui.wallpaper.navigation

import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo
import com.speakwithai.basestructure.ui.wallpaper.detail.DetailFragmentArgs
import com.speakwithai.basestructure.ui.wallpaper.result.WallpaperResultFragmentArgs

class WallpaperNavigationImpl: BaseNavigator(), WallpaperNavigation {

    override fun navigateToWallpaperDetailFragment(photo: Photo) {
        navigate(R.id.detailFragment, DetailFragmentArgs(photo))
    }
    override fun navigateToWallpaperResultFragment(url: String) {
        navigate(R.id.wallpaperResultFragment, WallpaperResultFragmentArgs(url))
    }
}