package com.example.basestructure.repository

import androidx.lifecycle.LiveData
import com.example.basestructure.db.MessageDao
import com.example.basestructure.model.local.MessageEntity
import javax.inject.Inject

class MessageRepository @Inject constructor(private val messageDao: MessageDao) {

    val messages: LiveData<List<MessageEntity>> = messageDao.getAll()

    suspend fun insert(message: MessageEntity) {
        messageDao.insert(message)
    }

    suspend fun deleteAll() {
        messageDao.deleteAll()
    }

    suspend fun getFirstMessageForDate(date: String): MessageEntity? {
        return messageDao.getFirstMessageForDate(date)
    }
}

