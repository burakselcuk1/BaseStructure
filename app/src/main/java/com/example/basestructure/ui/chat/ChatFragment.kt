package com.example.basestructure.ui.chat

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.adapter.MessageAdapter
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.common.ChildOption
import com.example.basestructure.databinding.FragmentChatBinding
import com.example.chatgptapp.model.Message
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.ViewUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.reflect.Array.get
import java.lang.reflect.Array.set
import java.util.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(
    layoutId = R.layout.fragment_chat,
    viewModelClass = ChatViewModel::class.java

) {
    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 100
        private const val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101

    }
    private var speechRecognizer: SpeechRecognizer? = null

    val prefs: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)
    }
    private lateinit var adapter: MessageAdapter

    var messageCount: Int
        get() = prefs.getInt("messageCount", 0)
        set(value) {
            prefs.edit().putInt("messageCount", value).apply()
        }

    var lastMessageTime: Long
        get() = prefs.getLong("lastMessageTime", 0)
        set(value) {
            prefs.edit().putLong("lastMessageTime", value).apply()
        }

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onInitDataBinding() {


        adapter = MessageAdapter(mutableListOf())
        adapter.setMessages(emptyList())
        binding.recyclerView.adapter = adapter

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

      //  viewModel.clearAllMessages()

        val llm = LinearLayoutManager(requireContext())
        llm.stackFromEnd = true
        binding.recyclerView.layoutManager = llm

        val adapter = MessageAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter

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

        viewModel.botTyping.observe(this) { isTyping ->
            if (isTyping) {
                adapter.addMessage(Message("${R.string.typing}", Message.SENT_BY_BOT, viewModel.getCurrentTimestamp()))
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

        viewModel.isMessageComplete.observe(viewLifecycleOwner) { isComplete ->
            adapter.setCopyIconVisibility(isComplete)
        }

        // Get the clicked child name from the arguments
        val clickedChildName = arguments?.getString("clickedChildName")
        handleChildOption(clickedChildName)

        binding.sendBtn.setOnClickListener {

            ViewUtils.hideKeyboard(it)
            if (messageCount < 3) {

            val question = binding.messageEditText.text.toString()
            viewModel.sendMessage(question)
            binding.messageEditText.setText("")
            incrementMessageCount()
            }else{
                showPremiumRequiredDialog()
            }
        }

        binding.microphone.setOnClickListener {
            startVoiceInput() // Start speech to text
            val mp = MediaPlayer.create(requireContext(), R.raw.microphone)
            mp.start()
        }

        binding.microphone.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // User released the button, stop listening
                // You might need to implement the stopListening() function depending on your needs.
                stopListening()
            }
            false // Don't consume the event
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

    fun incrementMessageCount() {
        val currentTimeMillis = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000

        // Eğer son mesaj gönderiminden 24 saat geçmişse sayaç sıfırlanır
        if (currentTimeMillis - lastMessageTime > oneDayMillis) {
            messageCount = 0
        }

        // Sayaç arttırılır ve son mesaj zamanı güncellenir
        messageCount++
        lastMessageTime = currentTimeMillis
    }

    private fun showPremiumRequiredDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.dialog_premium_required, null)
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
    }


    fun scrollToBottom() {
        val targetPosition = viewModel.allMessages.value?.size?.minus(1) ?: 0
        if (targetPosition >= 0) {
            binding.recyclerView.post {
                binding.recyclerView.smoothScrollToPosition(targetPosition)
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
                        when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> Log.e("SpeechRecognition", "Audio recording error")
                            SpeechRecognizer.ERROR_CLIENT -> Log.e("SpeechRecognition", "Client side error")
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.e("SpeechRecognition", "Insufficient permissions")
                            SpeechRecognizer.ERROR_NETWORK -> Log.e("SpeechRecognition", "Network error")
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.e("SpeechRecognition", "Network timeout")
                            SpeechRecognizer.ERROR_NO_MATCH -> Log.e("SpeechRecognition", "No match")
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.e("SpeechRecognition", "RecognitionService busy")
                            SpeechRecognizer.ERROR_SERVER -> Log.e("SpeechRecognition", "Server error")
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.e("SpeechRecognition", "No speech input")
                            else -> Log.e("SpeechRecognition", "Unknown error")
                        }
                    }


                    override fun onResults(bundle: Bundle?) {
                        val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches != null && matches.isNotEmpty()) {
                            val text = matches[0] // En iyi sonucu alır
                            // Metni EditText'e yerleştir
                            binding.messageEditText.setText(text)
                        }
                    }

                    override fun onPartialResults(bundle: Bundle?) {
                        val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches != null && matches.isNotEmpty()) {
                            val text = matches[0] // En iyi sonucu alır
                            // Kısmi metni EditText'e yerleştir
                            binding.messageEditText.setText(text)
                        }
                    }

                    override fun onEvent(p0: Int, p1: Bundle?) {
                        TODO("Not yet implemented")
                    }

                    // You may need to implement more methods here depending on your needs
                })
            }

            try {
                speechRecognizer?.startListening(intent)
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Üzgünüm, cihazınız bu özelliği desteklemiyor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    // Burada dönüştürülen metni işleyebilirsiniz.
                    binding.messageEditText.text = Editable.Factory.getInstance().newEditable(result.toString())
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                // İstek iptal edilirse, sonuç dizileri boş olacak.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // İzin verildi, ses tanıma işlemine devam et
                    startVoiceInput()
                } else {
                    // İzin verilmedi, işlemi durdur veya kullanıcıyı bilgilendir
                    Toast.makeText(requireContext(), "Mikrofon izni reddedildi.", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Diğer izin isteklerini işle
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
    fun handleChildOption(clickedChildName: String?) {
        val clickedChildOption = ChildOption.values().find { it.displayName == clickedChildName }
        when(clickedChildOption) {
            ChildOption.EV_KIRALAMA -> {
                prepareEditText(getString(R.string.house_rent))
            }
            ChildOption.BILET_ALMA -> {
                prepareEditText(getString(R.string.take_ticket))
            }
            ChildOption.RESTORAN_TAVSIYESI -> {
                prepareEditText(getString(R.string.suggest_restaurant))
            }
            ChildOption.GEZILECEK_YERLER -> {
                prepareEditText(getString(R.string.travel))
            }
            ChildOption.ISIM_URETICI -> {
                prepareEditText(getString(R.string.name_crator))
            }
            ChildOption.ILISKI_TAVSIYELERI -> {
                prepareEditText(getString(R.string.suggest_relationship))
            }
            ChildOption.BASLIK_FIKIRLERI -> {
                prepareEditText(getString(R.string.suggest_title))
            }
            ChildOption.SIIR_YAZMA -> {
                prepareEditText(getString(R.string.write_document))
            }
            ChildOption.IS_ILANI -> {
                prepareEditText(getString(R.string.job_posts))
            }
            ChildOption.HUCRE_ORGANELLERI -> {
                prepareEditText(getString(R.string.cell))
            }
            ChildOption.IKLIM_DEGISIKLIGI -> {
                prepareEditText(getString(R.string.climate))
            }
            ChildOption.EVRIM_TEORISI -> {
                prepareEditText(getString(R.string.evolution))
            }
            ChildOption.SAC_UZATMAK -> {
                prepareEditText(getString(R.string.hair))
            }
            ChildOption.DAHA_IYI_UYKU -> {
                prepareEditText(getString(R.string.sleep))
            }
            ChildOption.SABAH_RUTINI -> {
                prepareEditText(getString(R.string.routine))
            }
            ChildOption.KITAP_ONERILERI -> {
                prepareEditText(getString(R.string.book))
            }
            else -> {
                // Beklenmeyen bir durum
            }
        }
    }
    fun prepareEditText(text: String) {
        binding.messageEditText.setText(text)
        binding.microphone.visibility = View.GONE
        binding.sendBtn.visibility = View.VISIBLE
    }
}