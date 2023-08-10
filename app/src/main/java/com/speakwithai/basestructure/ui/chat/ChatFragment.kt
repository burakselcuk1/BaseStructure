package com.speakwithai.basestructure.ui.chat

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.speakwithai.basestructure.adapter.MessageAdapter
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.ChildOption
import com.google.android.material.internal.ViewUtils
import java.util.Locale
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.common.utils.MessageManager
import com.speakwithai.basestructure.databinding.FragmentChatBinding
import com.speakwithai.basestructure.model.Message
import com.speakwithai.basestructure.model.local.MessageEntity

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(
    layoutId = R.layout.fragment_chat,
    viewModelClass = ChatViewModel::class.java

) {
    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 100
        private const val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101

    }
    private var speechRecognizer: SpeechRecognizer? = null

    private lateinit var adapter: MessageAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onInitDataBinding() {
        setupUI()
        adapter = MessageAdapter(mutableListOf())
        adapter.setMessages(emptyList())
        binding.recyclerView.adapter = adapter

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

      //  viewModel.clearAllMessages()

        val llm = LinearLayoutManager(requireContext())
        llm.stackFromEnd = true
        binding.recyclerView.layoutManager = llm

        binding.recyclerView.adapter = adapter

        val clickedMessages = arguments?.getSerializable("clickedMessages") as? ArrayList<MessageEntity>

        clickedMessages?.let { messages ->
            for (messageEntity in messages) {
                val message = messageEntityToMessage(messageEntity)
                adapter.addMessage(message)
            }
        }


        viewModel.currentSessionMessages.observe(this) { messages ->
            adapter.updateData(messages)
            scrollToBottom() // Veri değiştiğinde listenin en altına kaydır
        }

        val text = arguments?.getString("recognized_text")
        if (text != null) {
            if (text.isNotEmpty()){
                binding.messageEditText.setText(text)
                binding.microphone.visibility = View.GONE
                binding.sendBtn.visibility = View.VISIBLE
            }
        }

     /*   requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Her zaman MainFragment'a gitmek için Navigation Component'i kullanın
                findNavController().popBackStack(R.id.mainFragment, false)
            }
        })*/


        viewModel.botTyping.observe(this) { isTyping ->
            if (isTyping) {
                adapter.addMessage(Message("${R.string.typing}", Message.SENT_BY_BOT, viewModel.getCurrentTimestamp()))
            } else {
                adapter.removeTypingIndicator()
            }
                    //  scrollToBottom()
        }

        viewModel.botWritingMessage.observe(viewLifecycleOwner) { partialMessage ->
            val message = Message(partialMessage, Message.SENT_BY_BOT, viewModel.getCurrentTimestamp())
            adapter.updateLastMessage(message)
        }

      /*  viewModel.botWritingMessage.observe(viewLifecycleOwner) { _ ->
            scrollToBottom()
        }*/

        viewModel.isMessageComplete.observe(viewLifecycleOwner) { isComplete ->
            adapter.setCopyIconVisibility(isComplete)
        }

        viewModel.isMessageComplete.observe(viewLifecycleOwner) { isComplete ->
            if (isComplete) {
                scrollToBottom()
            }
        }


        // Get the clicked child name from the arguments

        binding.sendBtn.setOnClickListener {
            ViewUtils.hideKeyboard(it)
            viewModel.userStatus.observe(this, Observer { status ->
                when(status) {
                    UserStatus.PREMIUM -> {
                        val question = binding.messageEditText.text.toString()
                        viewModel.sendMessage(question)
                        binding.messageEditText.setText("")
                    }
                    UserStatus.NON_PREMIUM -> {
                        if (MessageManager.canSendMessage()) {
                            val question = binding.messageEditText.text.toString()
                            viewModel.sendMessage(question)
                            binding.messageEditText.setText("")
                            MessageManager.messageSent()
                        } else {
                            showPremiumRequiredDialog()
                        }                    }
                    UserStatus.UNKNOWN -> {
                        if (MessageManager.canSendMessage()) {
                            val question = binding.messageEditText.text.toString()
                            viewModel.sendMessage(question)
                            binding.messageEditText.setText("")
                            MessageManager.messageSent()
                        } else {
                            showPremiumRequiredDialog()
                        }                    }
                    else -> {

                    }
                }
            })

        }

        binding.microphone.setOnClickListener {
            startVoiceInput()
            val mp = MediaPlayer.create(requireContext(), R.raw.microphone)
            mp.start()
        }

        binding.microphone.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                stopListening()
            }
            false
        }


        val typingText = getString(R.string.typing)
        var dotCount = 0
        val typingHandler = Handler()

        val typingRunnable = object : Runnable {
            override fun run() {
                dotCount = (dotCount + 1) % 4
                val dots = when (dotCount) {
                    0 -> ""
                    1 -> "."
                    2 -> ".."
                    else -> "..."
                }
                val typingIndicatorText = "$typingText$dots"
                binding.typingIndicator.text = typingIndicatorText
                typingHandler.postDelayed(this, 500)
            }
        }

        viewModel.botTyping.observe(this) { isTyping ->
            if (isTyping) {
                binding.typingIndicator.visibility = View.VISIBLE
                dotCount = 0
                typingHandler.postDelayed(typingRunnable, 500)
            } else {
                binding.typingIndicator.visibility = View.GONE
                typingHandler.removeCallbacks(typingRunnable)
            }
        }

        binding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.microphone.visibility = View.VISIBLE
                    binding.sendBtn.visibility = View.GONE
                } else {
                    binding.microphone.visibility = View.GONE
                    binding.sendBtn.visibility = View.VISIBLE
                }
            }
        })
    }

    fun messageEntityToMessage(entity: MessageEntity): Message {
        return Message(
            message = entity.content,
            timestamp = entity.timestamp,
            sentBy = if(entity.sender == MessageEntity.Sender.USER) Message.SENT_BY_ME else Message.SENT_BY_BOT
        )
    }
    private fun showPremiumRequiredDialog() {
/*        val dialogFragment = PremiumRequiredDialogFragment()
        dialogFragment.show(requireActivity().supportFragmentManager, "premiumRequiredDialog")*/
        findNavController().navigate(R.id.action_chatFragment2_to_premiumRequiredDialogFragment)
    }

    fun scrollToBottom() {
        val targetPosition = viewModel.allMessages.value?.size?.minus(1) ?: 0
        if (targetPosition >= 0) {
            binding.recyclerView.post {
                binding.recyclerView.layoutManager?.scrollToPosition(targetPosition)
            }
        }
    }

    private fun playStartSound() {
        val mp = MediaPlayer.create(requireContext(), R.raw.microphone)
        playStartSound()
    }

    private fun startVoiceInput() {
        // İzin kontrolü
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // İzin verilmemişse izin iste
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
        } else {
            // İzin zaten verilmişse, ses tanıma işlemine devam et
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Merhaba, konuşunuzu metne çevirebilirsiniz.")

            if (speechRecognizer == null) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
                speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                    }

                    override fun onBeginningOfSpeech() {
                        binding.microphone.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))

                    }

                    override fun onRmsChanged(p0: Float) {
                    }

                    override fun onBufferReceived(p0: ByteArray?) {
                    }

                    override fun onEndOfSpeech() {
                        /*binding.microphone.visibility = View.GONE
                        binding.sendBtn.visibility = View.VISIBLE*/
                        binding.microphone.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

                    }

                    override fun onError(error: Int) {
                        Log.d("SpeechRecognition", "Error code: $error")
                    }


                    override fun onResults(bundle: Bundle?) {
                        val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches != null && matches.isNotEmpty()) {
                            val text = matches[0] // En iyi sonucu alır
                            binding.messageEditText.setText(text)
                        }
                    }

                    override fun onPartialResults(bundle: Bundle?) {
                        val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches != null && matches.isNotEmpty()) {
                            val text = matches[0] // En iyi sonucu alır
                            binding.messageEditText.setText(text)
                        }
                    }

                    override fun onEvent(p0: Int, p1: Bundle?) {
                        TODO("Not yet implemented")
                    }
                })
            }

            try {
                speechRecognizer?.startListening(intent)
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(requireContext(), getString(R.string.unsupport), Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.messageEditText.text = Editable.Factory.getInstance().newEditable(result.toString())
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startVoiceInput()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.no_microphone_permission), Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
            speechRecognizer = null
        }
    }
    fun setupUI() {
        val clickedChildName = arguments?.getString("clickedChildName")
        val resultText = viewModel.handleChildOption(clickedChildName)
        if (resultText.isNullOrEmpty()){

        }else{
            prepareEditText(resultText)

        }
    }
    fun prepareEditText(text: String) {
        with(binding){
            messageEditText.setText(text)
            microphone.visibility = View.GONE
            sendBtn.visibility = View.VISIBLE
        }
    }
}