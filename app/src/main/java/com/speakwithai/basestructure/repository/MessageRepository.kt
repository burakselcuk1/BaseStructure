package com.speakwithai.basestructure.repository

import androidx.lifecycle.LiveData
import com.speakwithai.basestructure.db.MessageDao
import com.speakwithai.basestructure.model.local.MessageEntity
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

    suspend fun getAllMessagesForDate(date: String): List<MessageEntity>? {
        return messageDao.getAllMessagesForDate(date)
    }

}

