package com.speakwithai.basestructure.ui.mainFragment.navigation


import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator
import com.speakwithai.basestructure.ui.mainFragment.navigation.MainNavigation

class MainNavigationImpl: BaseNavigator(), MainNavigation {
    override fun navigateToPickUpFragment() {
        navigate(R.id.pickUpFragment)
    }
}