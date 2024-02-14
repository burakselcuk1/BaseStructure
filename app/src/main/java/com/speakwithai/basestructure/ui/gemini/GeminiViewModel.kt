package com.speakwithai.basestructure.ui.gemini

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.speakwithai.basestructure.di.GeminiPro
import com.speakwithai.basestructure.di.GeminiProVision
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(
    @GeminiPro private val geminiPro : GenerativeModel,
    @GeminiProVision private val geminiProVision : GenerativeModel,

    ) : ViewModel() {
    private val _messageResponse = MutableLiveData<GenerateContentResponse?>()
    val messageResponse : LiveData<GenerateContentResponse?> get() = _messageResponse
    private val chat : Chat = geminiPro.startChat()

    fun geminiChat(
        message : String
    ){
        viewModelScope.launch {
            _messageResponse.value = chat.sendMessage(message)
        }
    }

    private val _promptResponse = MutableLiveData<GenerateContentResponse>()
    val promptResponse : LiveData<GenerateContentResponse> get() = _promptResponse

    fun geminiPromptResponse(
        inputContent : Content
    ){
        viewModelScope.launch {
            geminiProVision.generateContentStream(inputContent).collect { response ->
                _promptResponse.value = response
            }
        }
    }
}