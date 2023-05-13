package com.example.chatgptapp.model

import com.example.basestructure.model.ApiMessage

data class CompletionResponse(
    val id: String,
    val responseObject : String,
    val created: Int,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Choice(
    val message: ApiMessage,
    val finish_reason: String,
    val index: Int
)


