package com.speakwithai.basestructure.common.globalExt

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

inline fun <R> R?.orElse(block: () -> R): R {
    return this?:block()
}
const val ARGS_KEY = "__ARGS__"
inline fun <reified T : Parcelable> Fragment.getArgs(): T? {
    return arguments?.getParcelable(ARGS_KEY)
}

fun Fragment.putArgs(args: Parcelable?): Bundle = (arguments ?: bundleOf()).apply {
    putParcelable(ARGS_KEY, args)
    arguments = this
}

