package com.example.basestructure.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val sender: Sender,
    val timestamp: String,
    val dateTime: String // yeni alan
) {
    enum class Sender { USER, BOT }

    companion object {
        const val SENT_BY_ME = "sent_me"
        const val SENT_BY_BOT = "sent_bot"
    }
}

