package com.speakwithai.basestructure.ui.settings.navigation

import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator
import com.speakwithai.basestructure.ui.settings.navigation.SettingsNavigation

class SettingsNavigationImpl: BaseNavigator(), SettingsNavigation {
    override fun navigateToProfileFragment() {
        navigate(R.id.profileFragment)
    }
}