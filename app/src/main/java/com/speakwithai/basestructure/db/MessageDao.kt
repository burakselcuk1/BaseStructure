package com.speakwithai.basestructure.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speakwithai.basestructure.model.local.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM message_table ORDER BY timestamp ASC")
    fun getAll(): LiveData<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: MessageEntity)

    @Query("DELETE FROM message_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM message_table WHERE date(dateTime) = :date ORDER BY dateTime ASC LIMIT 1")
    suspend fun getFirstMessageForDate(date: String): MessageEntity?


    @Query("SELECT * FROM message_table WHERE date(dateTime) = :date ORDER BY dateTime ASC")
    suspend fun getAllMessagesForDate(date: String): List<MessageEntity>?


}
