package com.speakwithai.basestructure.ui.login.navigation

import com.speakwithai.basestructure.base.Navigation

interface LoginNavigation: Navigation {

    fun navigateToRegisterFragment()
    fun navigateToLoginFragment()
    fun navigateToPickUpFragment()
}