package com.speakwithai.basestructure.ui.chatGpt

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentChatGptBinding
import java.io.File
import android.os.Environment
import android.widget.Toast


class ChatGptFragment : BaseFragment<FragmentChatGptBinding, ChatGptViewModel>(
    layoutId = R.layout.fragment_chat_gpt,
    viewModelClass = ChatGptViewModel::class.java
) {
    private val REQUEST_CODE_PICK_AUDIO = 101
    private var selectedAudioFile: File? = null

    override fun onInitDataBinding() {

    }
}
