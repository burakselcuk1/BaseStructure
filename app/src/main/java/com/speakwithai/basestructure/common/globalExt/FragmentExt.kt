package com.speakwithai.basestructure.common.globalExt

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.speakwithai.basestructure.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.launchRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

fun NavController.getNavOptions(): NavOptions.Builder {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
}


fun <T> NavController.getNavigationResult(key: String = "result") = currentBackStackEntry?.savedStateHandle?.get<T>(key)

fun <T> NavController.setNavigationResult(result: T, key: String = "result") { previousBackStackEntry?.savedStateHandle?.set(key, result) }

fun <T> NavController.setCurrentNavigationResult(result: T, key: String = "result") { currentBackStackEntry?.savedStateHandle?.set(key, result) }

fun <T> NavController.setNavigationResult(result: T, key: String = "result", destinationId: Int) {
    try {
        getBackStackEntry(destinationId).savedStateHandle[key] = result
    } catch (_: Exception) { }
}

fun <T> NavController.peekNavigationResult(key: String = "result") = getNavigationResult<T>(key)?.let {
    setCurrentNavigationResult(null, key); it
}