package com.speakwithai.basestructure.ui.chat

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.ChildOption
import com.speakwithai.basestructure.di.NetworkModule
import com.speakwithai.basestructure.model.MessageRequest
import com.speakwithai.basestructure.model.local.MessageEntity
import com.speakwithai.basestructure.repository.MessageRepository
import com.speakwithai.basestructure.model.CompletionRequest
import com.speakwithai.basestructure.model.CompletionResponse
import com.speakwithai.basestructure.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository,
                                        @ApplicationContext private val context: Context
): BaseViewModel() {


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

    private val _currentSessionMessages = MutableLiveData<List<Message>>()
    val currentSessionMessages: LiveData<List<Message>> get() = _currentSessionMessages


    private val _botTyping = MutableLiveData<Boolean>()
    val botTyping: LiveData<Boolean> get() = _botTyping
    private val _botWritingMessage = MutableLiveData<String>() // yeni eklendi
    val botWritingMessage: LiveData<String> get() = _botWritingMessage

    private val _isMessageComplete = MutableLiveData<Boolean>()
    val isMessageComplete: LiveData<Boolean> get() = _isMessageComplete


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
            callApi(content, numOfMessages = 3) // default numOfMessages is 3
            // After inserting the message into the database, we add it to the _currentSessionMessages list
            _currentSessionMessages.value = _currentSessionMessages.value?.plus(
                Message(
                    message = userMessage.content,
                    sentBy = Message.SENT_BY_ME,
                    timestamp = userMessage.timestamp
                )
            ) ?: listOf(
                Message(
                    message = userMessage.content,
                    sentBy = Message.SENT_BY_ME,
                    timestamp = userMessage.timestamp
                )
            )
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
            // After inserting the message into the database, we add it to the _currentSessionMessages list
            _currentSessionMessages.value = _currentSessionMessages.value?.plus(
                Message(
                    message = messageEntity.content,
                    sentBy = when (messageEntity.sender) {
                        MessageEntity.Sender.USER -> Message.SENT_BY_ME
                        MessageEntity.Sender.BOT -> Message.SENT_BY_BOT
                    },
                    timestamp = messageEntity.timestamp
                )
            ) ?: listOf(
                Message(
                    message = messageEntity.content,
                    sentBy = when (messageEntity.sender) {
                        MessageEntity.Sender.USER -> Message.SENT_BY_ME
                        MessageEntity.Sender.BOT -> Message.SENT_BY_BOT
                    },
                    timestamp = messageEntity.timestamp
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun callApi(question: String, retryCount: Int = 0, numOfMessages: Int = 3) {
        val messageHistory = createMessageHistory().toMutableList()
        messageHistory.add(MessageRequest("user", question))

        val completionRequest = CompletionRequest(
            model = "gpt-3.5-turbo",
            messages = messageHistory.takeLast(numOfMessages),
            max_tokens = 4000
        )

        viewModelScope.launch {
            try {
                val response = NetworkModule.apiService.getCompletions(completionRequest)
                handleApiResponse(response, retryCount, question, numOfMessages)
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
    private suspend fun handleApiResponse(response: Response<CompletionResponse>, retryCount: Int = 0, question: String, numOfMessages: Int) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.message?.content
                    if (!result.isNullOrEmpty()) {
                        _botWritingMessage.value = ""  // Clear the previous message
                        _botTyping.value = false // AI has finished "typing", response is successful

                        val delay: Long = 60  // Bot "typing" time in milliseconds per character
                        val handler = Handler(Looper.getMainLooper())

                        // Check the GPT-3 response and append a Google search link if necessary
                        val finalResult = checkAndAppendGoogleSearchLink(result, question)

                        for (i in finalResult.indices) {
                            handler.postDelayed({
                                val partialMessage = _botWritingMessage.value + finalResult[i]
                                _botWritingMessage.value = partialMessage
                                if (i == finalResult.length - 1) { // Eğer son harfe ulaştıysak
                                    addToChat(_botWritingMessage.value!!, Message.SENT_BY_BOT, getCurrentTimestamp())
                                    // give a slight delay before setting _isMessageComplete.value to true
                                    handler.postDelayed({
                                        _isMessageComplete.value = true
                                    }, 100)
                                }
                            }, delay * i)
                        }
                    } else {
                        addToChat(R.string.no_choices_found.toString(), Message.SENT_BY_BOT, getCurrentTimestamp())
                    }
                } ?: run {
                    addToChat(R.string.response_body_is_null.toString(), Message.SENT_BY_BOT, getCurrentTimestamp())
                }
            } else {
                if (retryCount < 10) { // Max retry count is 10

                    if (response.code() == 400 && "context_length_exceeded" in (response.errorBody()?.string() ?: "")) {
                        if (numOfMessages > 1) {
                            callApi(question, retryCount, numOfMessages - 1)
                        } else {
                            Log.d("APIResponse", "Failed response after reducing number of messages: ${response.errorBody()}")
                        }
                    } else {
                        callApi(question, retryCount + 1, numOfMessages) // Here we increment the retryCount by 1 for each retry
                    }
                } else {
                    Log.d("APIResponse", "Failed response after ${retryCount} attempts: ${response.errorBody()}")
                }
            }
        }
    }

    // Check the GPT-3 response and append a Google search link if necessary
    fun checkAndAppendGoogleSearchLink(gptResponse: String, question: String): String {
        val limitedInfoStrings = listOf(
            "${R.string.limited_until_2021}",
            "${R.string.limited_until_2021_one}",
            "${R.string.limited_until_2021_two}",
            "${R.string.limited_until_2021_three}"
        )

        val isLimited = limitedInfoStrings.any { it in gptResponse }

        return if (isLimited) {
            "$gptResponse ${R.string.data_after_2021_here} https://www.google.com/search?q=${URLEncoder.encode(question, "utf-8")}"
        } else {
            gptResponse
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addToChat(message: Message) {
        val messageEntity = MessageEntity(
            content = message.message,
            sender = when(message.sentBy) {
                Message.SENT_BY_ME -> MessageEntity.Sender.USER
                else -> MessageEntity.Sender.BOT
            },
            timestamp = message.timestamp,
            dateTime = currentDateTime

        )
        viewModelScope.launch {
            messageRepository.insert(messageEntity)
        }
    }

    fun handleChildOption(clickedChildName: String?): String {
        val clickedChildOption = ChildOption.values().find { it.displayName == clickedChildName }
        return when(clickedChildOption) {
            ChildOption.EV_KIRALAMA, ChildOption.HOUSE_RENT -> context.getString(R.string.house_rent)
            ChildOption.BILET_ALMA, ChildOption.TICKET -> context.getString(R.string.take_ticket)
            ChildOption.RESTORAN_TAVSIYESI, ChildOption.RESTAURANT_SUGESTIONS -> context.getString(R.string.suggest_restaurant)
            ChildOption.GEZILECEK_YERLER, ChildOption.PLACES_TO_VISIT -> context.getString(R.string.travel)
            ChildOption.ISIM_URETICI, ChildOption.NAME_CREATOR -> context.getString(R.string.name_crator)
            ChildOption.ILISKI_TAVSIYELERI, ChildOption.RELATIONSHOP_ADVICE -> context.getString(R.string.suggest_relationship)
            ChildOption.BASLIK_FIKIRLERI, ChildOption.TITLE_IDEAS -> context.getString(R.string.suggest_title)
            ChildOption.SIIR_YAZMA, ChildOption.POEM_WRITING -> context.getString(R.string.write_document)
            ChildOption.IS_ILANI, ChildOption.JOB_POSTING -> context.getString(R.string.job_posts)
            ChildOption.HUCRE_ORGANELLERI, ChildOption.CELL_ORGANELLES -> context.getString(R.string.cell)
            ChildOption.IKLIM_DEGISIKLIGI, ChildOption.CLIMATE_CHANGE -> context.getString(R.string.climate)
            ChildOption.EVRIM_TEORISI, ChildOption.EVOLUTION_HISTORY -> context.getString(R.string.evolution)
            ChildOption.SAC_UZATMAK, ChildOption.HAIR_GROWN -> context.getString(R.string.hair)
            ChildOption.DAHA_IYI_UYKU, ChildOption.BETTER_SLEEP -> context.getString(R.string.sleep)
            ChildOption.SABAH_RUTINI, ChildOption.MORNING_ROUTINE -> context.getString(R.string.routine)
            ChildOption.KITAP_ONERILERI, ChildOption.BOOK_SUGGESTIONS -> context.getString(R.string.book)
            else -> ""
        }
    }

}