package com.example.basestructure.ui.chat

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.di.NetworkModule
import com.example.basestructure.model.MessageRequest
import com.example.basestructure.model.local.MessageEntity
import com.example.basestructure.repository.MessageRepository
import com.example.chatgptapp.model.CompletionRequest
import com.example.chatgptapp.model.CompletionResponse
import com.example.chatgptapp.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository): BaseViewModel() {

    val allMessages: LiveData<List<Message>> = messageRepository.messages.map { messageEntityList ->
        messageEntityList.map { messageEntity ->
            Message(
                message = messageEntity.content,
                sentBy = when (messageEntity.sender) {
                    MessageEntity.Sender.USER -> Message.SENT_BY_ME
                    MessageEntity.Sender.BOT -> Message.SENT_BY_BOT
                },
                timestamp = messageEntity.timestamp
            )
        }
    }

    private val _botTyping = MutableLiveData<Boolean>()
    val botTyping: LiveData<Boolean> get() = _botTyping
    private val _botWritingMessage = MutableLiveData<String>() // yeni eklendi
    val botWritingMessage: LiveData<String> get() = _botWritingMessage

    private val _isMessageComplete = MutableLiveData<Boolean>()
    val isMessageComplete: LiveData<Boolean> get() = _isMessageComplete


    fun sendMessage(content: String) {
        _botTyping.value = true
        val userMessage = MessageEntity(
            sender = MessageEntity.Sender.USER,
            content = content,
            timestamp = getCurrentTimestamp()
        )
        viewModelScope.launch {
            messageRepository.insert(userMessage)
            callApi(content)
        }
    }

    fun addToChat(message: String, sentBy: String, timestamp: String) {
        val messageEntity = MessageEntity(
            content = message,
            sender = when(sentBy) {
                Message.SENT_BY_ME -> MessageEntity.Sender.USER
                else -> MessageEntity.Sender.BOT
            },
            timestamp = timestamp
        )
        viewModelScope.launch {
            messageRepository.insert(messageEntity)
        }
    }

    fun callApi(question: String, retryCount: Int = 0) {
        val messageHistory = createMessageHistory().toMutableList()
        messageHistory.add(MessageRequest("user", question))

        val completionRequest = CompletionRequest(
            model = "gpt-3.5-turbo",
            messages = messageHistory.takeLast(3),
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getCompletions(completionRequest)
                handleApiResponse(response, retryCount, question)
            } catch (e: SocketTimeoutException) {
                addToChat("Timeout :  $e", Message.SENT_BY_BOT, getCurrentTimestamp())
                _botTyping.value = false // AI has finished "typing"
            }
        }
    }


    fun clearAllMessages() {
        viewModelScope.launch {
            messageRepository.deleteAll()
        }
    }

    private suspend fun handleApiResponse(response: Response<CompletionResponse>, retryCount: Int = 0, question: String) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    Log.d("APIResponse", "Completion Response: $completionResponse")
                    val result = completionResponse.choices.firstOrNull()?.message?.content
                    if (!result.isNullOrEmpty()) {
                        _botWritingMessage.value = ""  // Clear the previous message
                        val delay: Long = 60  // Bot "typing" time in milliseconds per character
                        val handler = Handler(Looper.getMainLooper())
                        for (i in result.indices) {
                            handler.postDelayed({
                                val partialMessage = _botWritingMessage.value + result[i]
                                _botWritingMessage.value = partialMessage
                                if (i == result.length - 1) { // Eğer son harfe ulaştıysak
                                    addToChat(_botWritingMessage.value!!, Message.SENT_BY_BOT, getCurrentTimestamp())
                                    // give a slight delay before setting _isMessageComplete.value to true
                                    handler.postDelayed({
                                        _isMessageComplete.value = true
                                    }, 100)
                                }
                            }, delay * i)
                        }
                    } else {
                        addToChat("No choices found", Message.SENT_BY_BOT, getCurrentTimestamp())
                    }
                } ?: run {
                    addToChat("Response body is null", Message.SENT_BY_BOT, getCurrentTimestamp())
                }
            } else {
                if (retryCount < 10) { // Max retry count is 3
                    Log.d("APIResponse", "Failed response: ${response.errorBody()}, retrying... (${retryCount + 1})")
                    callApi(question, retryCount + 1) // Here we increment the retryCount by 1 for each retry
                } else {
                    Log.d("APIResponse", "Failed response after ${retryCount} attempts: ${response.errorBody()}")
                }
            }
            _botTyping.value = false // AI has finished "typing", regardless of success or failure
        }
    }



    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    }

    fun createMessageHistory(): List<MessageRequest> {
        val messageHistory = mutableListOf<MessageRequest>()
        // Her bir mesajı MessageRequest olarak dönüştür ve listeye ekle
        allMessages.value?.forEach { message ->
            val role = if (message.sentBy == Message.SENT_BY_ME) "user" else "system"
            messageHistory.add(MessageRequest(role, message.message))
        }
        return messageHistory
    }

    fun addToChat(message: Message) {
        val messageEntity = MessageEntity(
            content = message.message,
            sender = when(message.sentBy) {
                Message.SENT_BY_ME -> MessageEntity.Sender.USER
                else -> MessageEntity.Sender.BOT
            },
            timestamp = message.timestamp
        )
        viewModelScope.launch {
            messageRepository.insert(messageEntity)
        }
    }

}