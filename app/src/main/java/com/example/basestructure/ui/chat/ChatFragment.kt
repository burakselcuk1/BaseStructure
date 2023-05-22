package com.example.basestructure.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.adapter.MessageAdapter
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.common.ChildOption
import com.example.basestructure.databinding.FragmentChatBinding
import com.example.chatgptapp.model.Message
import com.google.android.material.internal.ViewUtils
import dagger.hilt.android.lifecycle.HiltViewModel

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(
    layoutId = R.layout.fragment_chat,
    viewModelClass = ChatViewModel::class.java
) {
    override fun onInitDataBinding() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.clearAllMessages()

        val llm = LinearLayoutManager(requireContext())
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

        viewModel.botWritingMessage.observe(viewLifecycleOwner) { partialMessage ->
            val message = Message(partialMessage, Message.SENT_BY_BOT, viewModel.getCurrentTimestamp())
            adapter.updateLastMessage(message)
        }

        viewModel.botWritingMessage.observe(viewLifecycleOwner) { _ ->
            scrollToBottom()
        }

        // Get the clicked child name from the arguments
        val clickedChildName = arguments?.getString("clickedChildName")

        val clickedChildOption = ChildOption.values().find { it.displayName == clickedChildName }
        when(clickedChildOption) {
            ChildOption.EV_KIRALAMA -> {
                binding.messageEditText.setText(getString(R.string.house_rent))
            }
            ChildOption.BILET_ALMA -> {
                binding.messageEditText.setText(getString(R.string.take_ticket))
            }
            ChildOption.RESTORAN_TAVSIYESI -> {
                binding.messageEditText.setText(getString(R.string.suggest_restaurant))
            }
            ChildOption.GEZILECEK_YERLER -> {
                binding.messageEditText.setText(getString(R.string.travel))
            }
            ChildOption.ISIM_URETICI -> {
                binding.messageEditText.setText(getString(R.string.name_crator))
            }
            ChildOption.ILISKI_TAVSIYELERI -> {
                binding.messageEditText.setText(getString(R.string.suggest_relationship))
            }
            ChildOption.BASLIK_FIKIRLERI -> {
                binding.messageEditText.setText(getString(R.string.suggest_title))
            }
            ChildOption.SIIR_YAZMA -> {
                binding.messageEditText.setText(getString(R.string.write_document))
            }
            ChildOption.IS_ILANI -> {
                binding.messageEditText.setText(getString(R.string.job_posts))
            }
            ChildOption.HUCRE_ORGANELLERI -> {
                binding.messageEditText.setText(getString(R.string.cell))
            }
            ChildOption.IKLIM_DEGISIKLIGI -> {
                binding.messageEditText.setText(getString(R.string.climate))
            }
            ChildOption.EVRIM_TEORISI -> {
                binding.messageEditText.setText(getString(R.string.evolution))
            }
            ChildOption.SAC_UZATMAK -> {
                binding.messageEditText.setText(getString(R.string.hair))
            }
            ChildOption.DAHA_IYI_UYKU -> {
                binding.messageEditText.setText(getString(R.string.sleep))
            }
            ChildOption.SABAH_RUTINI -> {
                binding.messageEditText.setText(getString(R.string.routine))
            }
            ChildOption.KITAP_ONERILERI -> {
                binding.messageEditText.setText(getString(R.string.book))
            }
            else -> {
                // Beklenmeyen bir durum
                Toast.makeText(requireContext(), "Beklenmeyen bir seçenek", Toast.LENGTH_SHORT).show()
            }
        }


        binding.sendBtn.setOnClickListener {
            ViewUtils.hideKeyboard(it)
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