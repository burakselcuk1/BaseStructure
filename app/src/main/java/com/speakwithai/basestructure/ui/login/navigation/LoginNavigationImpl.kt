package com.speakwithai.basestructure.ui.login.navigation

import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator
import com.speakwithai.basestructure.ui.login.navigation.LoginNavigation

class LoginNavigationImpl: BaseNavigator(), LoginNavigation {
    override fun navigateToRegisterFragment() {
        navigate(R.id.signUpFragment)
    }

    override fun navigateToLoginFragment() {
        navigate(R.id.signInFragment)
    }

    override fun navigateToPickUpFragment() {
        navigate(R.id.pickUpFragment)
    }
}