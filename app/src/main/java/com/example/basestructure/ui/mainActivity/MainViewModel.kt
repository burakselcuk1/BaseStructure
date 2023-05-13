package com.example.basestructure.ui.mainActivity

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val messageRepository: MessageRepository) : BaseViewModel() {

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

    fun callApi(question: String) {

        val messageHistory = createMessageHistory().toMutableList()
        messageHistory.add(MessageRequest("user", question))

        val completionRequest = CompletionRequest(
            model = "gpt-3.5-turbo",
            messages = messageHistory.takeLast(3),
            max_tokens = 4000
        )

        _botTyping.value = true

        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getCompletions(completionRequest)
                handleApiResponse(response, question)
            } catch (e: SocketTimeoutException) {
                addToChat("Timeout :  $e", Message.SENT_BY_BOT, getCurrentTimestamp())
            }
        }
    }





    private suspend fun handleApiResponse(response: Response<CompletionResponse>, question: String) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    Log.d("APIResponse", "Completion Response: $completionResponse")
                    val result = completionResponse.choices.firstOrNull()?.message?.content
                    if (result != null) {
                        addToChat(question, Message.SENT_BY_ME, getCurrentTimestamp())
                        addToChat(result.trim(), Message.SENT_BY_BOT, getCurrentTimestamp())
                    } else {
                        addToChat("No choices found", Message.SENT_BY_BOT, getCurrentTimestamp())
                    }
                }
            } else {
                Log.d("APIResponse", "Failed response: ${response.errorBody()}")
                addToChat("Failed to get response ${response.errorBody()}", Message.SENT_BY_BOT, getCurrentTimestamp())
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

}
