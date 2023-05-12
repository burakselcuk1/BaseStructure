package com.example.basestructure.ui.mainActivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.adapter.MessageAdapter
import com.example.basestructure.base.BaseActivity
import com.example.basestructure.databinding.ActivityMainBinding
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

        viewModel.messageList.observe(this) { messages ->
            val adapter = MessageAdapter(messages)
            binding.recyclerView.adapter = adapter
            scrollToBottom() // Veri değiştiğinde listenin en altına kaydır
        }

        binding.sendBtn.setOnClickListener {
            hideKeyboard(it)
            val question = binding.messageEditText.text.toString()
            viewModel.addToChat(question, Message.SENT_BY_ME, viewModel.getCurrentTimestamp())
            binding.messageEditText.setText("")
            viewModel.callApi(question)
        }
    }

    fun scrollToBottom() {
        val targetPosition = viewModel.messageList.value?.size?.minus(1) ?: 0
        if (targetPosition >= 0) {
            binding.recyclerView.post {
                binding.recyclerView.smoothScrollToPosition(targetPosition)
            }
        }
    }

}