package com.speakwithai.basestructure.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.speakwithai.basestructure.common.globalExt.getNavOptions
import com.speakwithai.basestructure.common.globalExt.getNavigationResult
import com.speakwithai.basestructure.common.globalExt.setCurrentNavigationResult
import com.speakwithai.basestructure.common.globalExt.setNavigationResult
import com.speakwithai.basestructure.ui.privacypolicy.PrivacyPolicyFragmentArgs

abstract class BaseNavigator : com.speakwithai.basestructure.base.Navigation {
    lateinit var navController: NavController

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    private fun getBundleByNavArgs(args: NavArgs): Bundle {
        val toBundleMethod = args::class.java.getMethod("toBundle")
        return toBundleMethod.invoke(args) as Bundle
    }
    fun navigate(@IdRes destinationId: Int, args: NavArgs? = null, navOptions: NavOptions? = null) {
        navController.navigate(destinationId, args?.let { getBundleByNavArgs(it) }, navOptions ?: navController.getNavOptions().build())
    }

    fun <T> getNavigationResult(key: String = "result") = navController.getNavigationResult<T>(key)

    fun <T> setNavigationResult(result: T, key: String = "result") { navController.setNavigationResult<T>(result, key) }

    fun <T> setCurrentNavigationResult(result: T, key: String = "result") { navController.setCurrentNavigationResult(result, key) }

    fun <T> setNavigationResult(result: T, key: String = "result", destinationId: Int) {
        navController.setNavigationResult(result, key, destinationId)
    }

}

