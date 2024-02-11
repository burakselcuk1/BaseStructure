package com.speakwithai.basestructure.ui.pickUp.navigation

import com.speakwithai.basestructure.base.Navigation

interface PickUpNavigation : Navigation {


    fun navigateToLoginFragment()
    fun navigateToSettingsFragment()
    fun navigateToQrGeneratorFragment()
    fun navigateToQrReaderFragment()
    fun navigateToCrpytoNewsFragment()
    fun navigateToCryptoFragment()
    fun navigateToChatGptFragment()
    fun navigateToMetaAiFragment()

    /*
    fun navigateToGoogleBardFragment()
    fun navigateToBingBardFragment()
    fun navigateToWallpaperFragment()
    fun navigateToMusicFragment()
    fun navigateToTextToImageFragment()
    fun navigateToGeminiFragment()

*/

}