package com.example.basestructure

import com.example.basestructure.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class Application: android.app.Application(){

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            val messageDao = AppDatabase.getInstance(this@Application).messageDao()
            // Clear the database
            messageDao.deleteAll()
        }
    }
}