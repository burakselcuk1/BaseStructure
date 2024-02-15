package com.speakwithai.basestructure.ui.pickUp.navigation

import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator

class PickUpNavigationImple: BaseNavigator(), PickUpNavigation {

    override fun navigateToLoginFragment() {
        navigate(R.id.signInFragment)
    }

    override fun navigateToSettingsFragment() {
        navigate(R.id.settingsFragment)
    }

    override fun navigateToQrGeneratorFragment() {
        navigate(R.id.qrCreaterFragment)
    }

    override fun navigateToQrReaderFragment() {
        navigate(R.id.qrReaderFragment)
    }

    override fun navigateToCrpytoNewsFragment() {
        navigate(R.id.cryptoNewsFragment)
    }

    override fun navigateToCryptoFragment() {
        navigate(R.id.cryptoMainFragment)
    }

    override fun navigateToChatGptFragment() {
        navigate(R.id.chatGptFragment)
    }

    override fun navigateToMetaAiFragment() {
        navigate(R.id.metaFragment)
    }
    override fun navigateToWallpaperFragment() {
        navigate(R.id.wallpaperFragment)
    }

    override fun navigateToMusicFragment() {
        navigate(R.id.musicFragment)
    }

    override fun navigateToGeminiFragment() {
        navigate(R.id.geminiFragment)
    }

    override fun navigateToTextToImageFragment() {
        navigate(R.id.textToImageFragment)
    }
}