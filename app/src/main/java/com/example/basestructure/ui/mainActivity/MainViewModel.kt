package com.example.basestructure.ui.mainActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.di.NetworkModule
import com.example.chatgptapp.model.CompletionRequest
import com.example.chatgptapp.model.CompletionResponse
import com.example.chatgptapp.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel : BaseViewModel() {

    private val _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> get() = _messageList

    init {
        _messageList.value = mutableListOf()
    }

    fun addToChat(message: String, sentBy: String, timestamp: String) {
        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(Message(message, sentBy, timestamp))
        _messageList.postValue(currentList)
    }

    private fun addResponse(response: String) {
        _messageList.value?.removeAt(_messageList.value?.size?.minus(1) ?: 0)
        addToChat(response, Message.SENT_BY_BOT, getCurrentTimestamp())
    }

    fun callApi(question: String) {
        addToChat("Typing....", Message.SENT_BY_BOT, getCurrentTimestamp())

        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getCompletions(completionRequest)
                handleApiResponse(response)
            } catch (e: SocketTimeoutException) {
                addResponse("Timeout :  $e")
            }
        }
    }



    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null) {
                        addResponse(result.trim())
                        displayResponseLetterByLetter(result.trim())
                    } else {
                        addResponse("No choices found")
                    }
                }
            } else {
                addResponse("Failed to get response ${response.errorBody()}")
            }
        }
    }

    private suspend fun displayResponseLetterByLetter(response: String) {
        var currentText = ""
        val lastIndex = _messageList.value?.size?.minus(1) ?: 0

        for (char in response) {
            currentText += char
            val message = Message(currentText, Message.SENT_BY_BOT, getCurrentTimestamp())
            updateMessage(message, lastIndex)
            delay(50) // Her bir karakter arasında 100ms bekleme süresi
        }
    }

    private fun updateMessage(message: Message, index: Int) {
        _messageList.value?.let { messages ->
            if (index in messages.indices) {
                messages[index] = message
                _messageList.postValue(messages)
            }
        }
    }

    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    }
}