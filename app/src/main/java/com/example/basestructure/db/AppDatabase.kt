package com.example.basestructure.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basestructure.model.local.MessageEntity

@Database(entities = arrayOf(MessageEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
