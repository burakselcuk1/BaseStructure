package com.speakwithai.basestructure

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class Application: android.app.Application(){

    override fun onCreate() {
        super.onCreate()

    /*    CoroutineScope(Dispatchers.IO).launch {
            val messageDao = AppDatabase.getInstance(this@Application).messageDao()
            // Clear the database
            messageDao.deleteAll()
        }
*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}