package com.speakwithai.basestructure.common.utils

import android.content.Context
import android.content.SharedPreferences

object MessageManager {
    private const val ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L
    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    }

    var messageCount: Int
        get() = prefs.getInt("messageCount", 0)
        set(value) {
            prefs.edit().putInt("messageCount", value).apply()
        }

    var lastMessageTime: Long
        get() = prefs.getLong("lastMessageTime", 0)
        set(value) {
            prefs.edit().putLong("lastMessageTime", value).apply()
        }

    fun canSendMessage(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastMessageTime > ONE_DAY_MILLIS) {
            resetCounter()
            return true
        }

        return messageCount < 2
    }

    fun messageSent() {
        val currentTimeMillis = System.currentTimeMillis()
        messageCount++
        lastMessageTime = currentTimeMillis
    }

    private fun resetCounter() {
        messageCount = 0
        lastMessageTime = System.currentTimeMillis()
    }

    fun isOneDayPassed(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return currentTimeMillis - lastMessageTime > ONE_DAY_MILLIS
    }

}
