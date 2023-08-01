package com.speakwithai.basestructure.model

import com.speakwithai.basestructure.model.MessageRequest

data class CompletionRequest(
    val model: String,
    val messages: List<MessageRequest>,
    val max_tokens: Int,
    val temperature: Float = 0.4f
)
