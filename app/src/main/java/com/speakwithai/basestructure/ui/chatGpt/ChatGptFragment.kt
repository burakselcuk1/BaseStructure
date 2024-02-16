package com.speakwithai.basestructure.ui.chatGpt

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentChatGptBinding
import java.io.File
import android.os.Environment
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.common.AdManager
import com.speakwithai.basestructure.common.AnalyticsHelper


class ChatGptFragment : BaseFragment<FragmentChatGptBinding, ChatGptViewModel>(
    layoutId = R.layout.fragment_chat_gpt,
    viewModelClass = ChatGptViewModel::class.java
) {
    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("ChatGptFragment","ChatGptFragment",requireContext())
        setListeners()
    }

    private fun setListeners() {
        with(binding){
            launchGpt.setOnClickListener {
                AdManager.loadAd(requireContext(), "ca-app-pub-3940256099942544/1033173712")
                AdManager.showAd(requireActivity())
                openCustomTab("https://chat.openai.com/")
            }
            floatingActionButtonGoogleBard.setOnClickListener {
                findNavController().popBackStack()
            }
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun openCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        context?.let {
            customTabsIntent.launchUrl(it, Uri.parse(url))
        }
    }
}
