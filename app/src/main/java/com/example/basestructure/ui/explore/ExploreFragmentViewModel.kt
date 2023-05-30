package com.example.basestructure.ui.explore

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.common.Event
import com.example.basestructure.model.local.MessageEntity
import com.example.basestructure.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ExploreFragmentViewModel @Inject constructor(private val repository: MessageRepository): BaseViewModel() {


    private val _dailyUserMessages = MutableLiveData<List<MessageEntity>>()
    val dailyUserMessages: LiveData<List<MessageEntity>> = _dailyUserMessages

    private val _clickedDateMessages = MutableLiveData<Event<List<MessageEntity>>>()
    val clickedDateMessages: LiveData<Event<List<MessageEntity>>> get() = _clickedDateMessages

    suspend fun getAllMessagesForDate(date: String): List<MessageEntity>? {
        return repository.getAllMessagesForDate(date)
    }

    fun fetchAllMessagesForClickedDate(date: String) = viewModelScope.launch {
        val allMessagesForDate = getAllMessagesForDate(date)
        allMessagesForDate?.let {
            _clickedDateMessages.value = Event(it) // Event ile wrap ediliyor
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchDailyUserMessages() = viewModelScope.launch {
        val messagesForDays = mutableListOf<MessageEntity>()
        // assuming you have a way to generate or get these dates
        val dates = getDates()
        for (date in dates) {
            val firstMessageForDate = getFirstMessageForDate(date)
            firstMessageForDate?.let {
                messagesForDays.add(it)
            }
        }
        _dailyUserMessages.value = messagesForDays
    }

    private suspend fun getFirstMessageForDate(date: String): MessageEntity? {
        return repository.getFirstMessageForDate(date)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getDates(): List<String> {
        val dates = mutableListOf<String>()
        val startDate = LocalDate.of(2023, 1, 1) // Başlangıç tarihi
        val endDate = LocalDate.now() // Bitiş tarihi

        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.toString())
            currentDate = currentDate.plusDays(1)
        }
        return dates
    }

}