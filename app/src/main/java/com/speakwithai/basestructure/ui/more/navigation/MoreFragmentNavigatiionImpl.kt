package com.speakwithai.basestructure.ui.more.navigation

import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseNavigator

class MoreFragmentNavigatiionImpl : BaseNavigator(), MoreFragmentNavigatiion {


    override fun navigateToPrivacy(isim: String) {
        navigate(
            R.id.privacyPolicyFragment,
            com.speakwithai.basestructure.ui.privacypolicy.PrivacyPolicyFragmentArgs(isim)
        )
    }
}