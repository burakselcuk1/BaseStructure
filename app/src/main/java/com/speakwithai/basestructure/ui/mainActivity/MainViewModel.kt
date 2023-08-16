package com.speakwithai.basestructure.ui.mainActivity

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.BillingManager
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.di.NetworkModule
import com.speakwithai.basestructure.model.MessageRequest
import com.speakwithai.basestructure.model.local.MessageEntity
import com.speakwithai.basestructure.repository.MessageRepository
import com.speakwithai.basestructure.model.CompletionRequest
import com.speakwithai.basestructure.model.CompletionResponse
import com.speakwithai.basestructure.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val messageRepository: MessageRepository,
    private val billingManager: BillingManager) : BaseViewModel() {
    init {
            billingManager.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    // Bağlantı başarılı bir şekilde kurulduğunda yapılacak işlemler
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        // Bağlantı başarılı bir şekilde kurulduğunda yapılacak işlemler
                        Log.d("BillingConnection", "Bağlantı başarılı!")
                    } else {
                        Log.d("BillingConnection", "Bağlantı başarısız. Hata kodu: ${billingResult.responseCode}")
                    }                }

                override fun onBillingServiceDisconnected() {
                    // Bağlantı kesildiğinde yapılacak işlemler
                }
            })
        setPurchasesUpdatedListener()
        checkUserStatus()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))


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

    private fun setPurchasesUpdatedListener() {
        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            // Satın alma işlemlerini burada işleyebilirsiniz. Gerekliyse LiveData'yı güncelleyebilirsiniz.
        }
        billingManager.setPurchasesUpdatedListener(purchasesUpdatedListener)
    }

    private fun checkUserStatus() {
        billingManager.queryPurchases()
    }

    val userStatus: LiveData<UserStatus> get() = billingManager.userStatus


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(content: String) {
        _botTyping.value = true
        val userMessage = MessageEntity(
            sender = MessageEntity.Sender.USER,
            content = content,
            timestamp = getCurrentTimestamp(),
            dateTime = currentDateTime

        )
        viewModelScope.launch {
            messageRepository.insert(userMessage)
            callApi(content)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addToChat(message: String, sentBy: String, timestamp: String) {
        val messageEntity = MessageEntity(
            content = message,
            sender = when(sentBy) {
                Message.SENT_BY_ME -> MessageEntity.Sender.USER
                else -> MessageEntity.Sender.BOT
            },
            timestamp = timestamp,
            dateTime = currentDateTime

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

        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getCompletions(completionRequest)
                handleApiResponse(response)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    Log.d("APIResponse", "Completion Response: $completionResponse")
                    val result = completionResponse.choices.firstOrNull()?.message?.content
                    if (!result.isNullOrEmpty()) {
                        addToChat(result.trim(), Message.SENT_BY_BOT, getCurrentTimestamp())
                    } else {
                        addToChat("No choices found", Message.SENT_BY_BOT, getCurrentTimestamp())
                    }
                } ?: run {
                    addToChat("Response body is null", Message.SENT_BY_BOT, getCurrentTimestamp())
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
