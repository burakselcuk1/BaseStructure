package com.speakwithai.basestructure.common

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


object AnalyticsHelper {
    fun logScreenView(screenName: String, className: String, context: Context) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
        val analytics = FirebaseAnalytics.getInstance(context)
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}