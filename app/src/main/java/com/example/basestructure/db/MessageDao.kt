package com.example.basestructure.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basestructure.model.local.MessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM message_table ORDER BY timestamp ASC")
    fun getAll(): LiveData<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(message: MessageEntity)

    @Query("DELETE FROM message_table")
    suspend fun deleteAll()
}
