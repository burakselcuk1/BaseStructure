package com.example.basestructure.ui.mainActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.adapter.MessageAdapter
import com.example.basestructure.base.BaseActivity
import com.example.basestructure.databinding.ActivityMainBinding
import com.example.basestructure.model.local.MessageEntity
import com.example.chatgptapp.model.Message
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    layoutId = R.layout.activity_main, viewModelClass = MainViewModel::class.java
) {
    @SuppressLint("RestrictedApi")
    override fun onInitDataBinding() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        binding.recyclerView.layoutManager = llm

        val adapter = MessageAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter

        viewModel.allMessages.observe(this) { messages ->
            adapter.updateData(messages)
            scrollToBottom() // Veri değiştiğinde listenin en altına kaydır
        }

        viewModel.botTyping.observe(this) { isTyping ->
            if (isTyping) {
                adapter.addMessage(Message("Typing...", Message.SENT_BY_BOT, viewModel.getCurrentTimestamp()))
            } else {
                adapter.removeTypingIndicator()
            }
            scrollToBottom()
        }

        binding.sendBtn.setOnClickListener {
            hideKeyboard(it)
            val question = binding.messageEditText.text.toString()
            viewModel.sendMessage(question)
            binding.messageEditText.setText("")
        }

        viewModel.botTyping.observe(this) { isTyping ->
            if (isTyping) {
                binding.typingIndicator.visibility = View.VISIBLE
            } else {
                binding.typingIndicator.visibility = View.GONE
            }
        }
    }

    fun scrollToBottom() {
        val targetPosition = viewModel.allMessages.value?.size?.minus(1) ?: 0
        if (targetPosition >= 0) {
            binding.recyclerView.post {
                binding.recyclerView.smoothScrollToPosition(targetPosition)
            }
        }
    }
}
