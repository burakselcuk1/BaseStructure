package com.speakwithai.basestructure.ui.textToSpeech

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SsmlVoiceGender
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.TextToSpeechSettings
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.AdManager
import com.speakwithai.basestructure.databinding.ActivityTextToSpeechBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Date


class TextToSpeechActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextToSpeechBinding
    private var languageCode: String = "tr-TR"
    private var ssmlGender: SsmlVoiceGender = SsmlVoiceGender.FEMALE
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer()
        binding = ActivityTextToSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var voiceValue = binding.editText.text.toString()

        with(binding) {
            download.setOnClickListener {
                downloadLastFile()
            }
            backArrow.setColorFilter(ContextCompat.getColor(this@TextToSpeechActivity, R.color.white))
            backArrow.setOnClickListener {
                onBackPressed()
            }
            playSound.applyClickShrink()
            playSound.setOnClickListener {
                playSound(it)
                AdManager.loadAd(this@TextToSpeechActivity, "ca-app-pub-3940256099942544/1033173712")
                AdManager.showAd(this@TextToSpeechActivity)
            }
            stop.applyClickShrink()
            stop.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
            }
            continueVoice.applyClickShrink()
            continueVoice.setOnClickListener {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
            }
        }


        val languages = arrayOf(
            getString(R.string.english),
            getString(R.string.german),
            getString(R.string.russian),
            getString(R.string.french),
            getString(R.string.italian),
            getString(R.string.turkish)
        )
        val spinner: Spinner = findViewById(R.id.spinnerLanguage)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                setLanguage(selectedLanguage)
                synthesizeSpeech(voiceValue,voiceValue)
            }
        }



        val genderOptions = arrayOf(
            getString(R.string.woman),
            getString(R.string.man),
        )
        val spinnerGender: Spinner = findViewById(R.id.spinnerGender)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter


        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGender = genderOptions[position]
                setGender(selectedGender)
                synthesizeSpeech(voiceValue,voiceValue)
            }
        }


    }


    fun playSound(view: View) {

        binding.stop.visibility = View.VISIBLE
        binding.continueVoice.visibility = View.VISIBLE

        val textFromEditText = binding.editText.text.toString().trim()
        val fileName = getOutputFileName()

        if (textFromEditText.isNotEmpty()) {
            synthesizeSpeech(textFromEditText, fileName)
        } else {
            val hataMesaji = getString(R.string.empy_message)
            Toast.makeText(this, hataMesaji, Toast.LENGTH_SHORT).show()
        }
        val file = File(filesDir, fileName)

        if (file.exists()) {
            try {
                binding.recordTextview.visibility = View.VISIBLE
                binding.download.visibility = View.VISIBLE
                val fis = FileInputStream(file)
                val fd = fis.fd
                mediaPlayer.reset()
                mediaPlayer.setDataSource(fd)
                mediaPlayer.prepare()
                mediaPlayer.start()
                val lastFileName = getLastSavedFileName()
                val sonKayitString = getString(R.string.last_record)
                val finalText = "$sonKayitString $lastFileName"
                binding.recordTextview.text = finalText

            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.not_find_voice_file), Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, getString(R.string.not_find_voice_file), Toast.LENGTH_SHORT).show()
        }
    }

    fun synthesizeSpeech(text: String, fileName: String) {
        val credentials = GoogleCredentials.fromStream(assets.open("inspired-data-395823-8475dae64168.json"))
        val textToSpeechSettings = TextToSpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()

        val client = TextToSpeechClient.create(textToSpeechSettings)

        val input = SynthesisInput.newBuilder().setText(text).build()

        val voicesResponse = client.listVoices("")
        val availableVoices = voicesResponse.voicesList.filter { it.languageCodesList.contains(languageCode) }

        val selectedVoice = availableVoices.find { it.ssmlGender == ssmlGender }
        val voiceName = selectedVoice?.name ?: "$languageCode-Standard-A"

        val voice = VoiceSelectionParams.newBuilder()
            .setLanguageCode(languageCode)
            .setName(voiceName)
            .setSsmlGender(ssmlGender)
            .build()

        val audioConfig = AudioConfig.newBuilder()
            .setAudioEncoding(AudioEncoding.MP3)
            .build()

        val response = client.synthesizeSpeech(input, voice, audioConfig)
        val audioContents = response.audioContent

        val file = File(filesDir, fileName)

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    val out: OutputStream = FileOutputStream(file)
                    out.write(audioContents.toByteArray())
                    out.close()
                } else {
                    Toast.makeText(this, getString(R.string.not_find_voice_file), Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this, getString(R.string.not_find_voice_file), Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


        client.close()
    }



    fun setLanguage(language: String) {
        languageCode = when (language) {
            getString(R.string.turkish) -> "tr-TR"
            getString(R.string.english) -> "en-US"
            getString(R.string.russian) -> "ru-RU"
            getString(R.string.french) -> "fr-FR"
            getString(R.string.german) -> "de-DE"
            getString(R.string.italian) -> "it-IT"
            else -> "en-US"
        }
    }


    fun setGender(gender: String) {
        ssmlGender = when (gender) {
            getString(R.string.woman) -> SsmlVoiceGender.FEMALE
            getString(R.string.man) -> SsmlVoiceGender.MALE
            else -> SsmlVoiceGender.FEMALE
        }
    }

    private fun getOutputFileName(): String {
        val editText: EditText = binding.editText
        val textFromEditText = editText.text.toString().trim()
        val date = Date().time
        return if (textFromEditText.isNotEmpty()) {
            "$date.mp3"
        } else {
            "output.mp3"
        }
    }

    private fun getLastSavedFileName(): String {
        val directory = filesDir
        val files = directory.list()

        files.sortByDescending {
            File(directory, it).lastModified()
        }

        if (files.isNotEmpty()) {
            return files[0]
        } else {
            return getString(R.string.not_find_voice_file)
        }
    }

    fun downloadLastFile() {
        val lastFileName = getLastSavedFileName()
        val sourceFile = File(filesDir, lastFileName)
        val destinationFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), lastFileName)

        if (sourceFile.exists()) {
            try {
                val source = FileInputStream(sourceFile).channel
                val destination = FileOutputStream(destinationFile).channel
                destination.transferFrom(source, 0, source.size())
                source.close()
                destination.close()

                Toast.makeText(this, getString(R.string.file_downloaded) + lastFileName, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.file_not_downloaded), Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, getString(R.string.not_find_voice_file), Toast.LENGTH_SHORT).show()
        }
    }
}
