package com.speakwithai.basestructure.ui.textToImage

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AdManager
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.databinding.FragmentTextToImageBinding


class TextToImageFragment : BaseFragment<FragmentTextToImageBinding, TextToImageViewModel>(
    layoutId = R.layout.fragment_text_to_image,
    viewModelClass = TextToImageViewModel::class.java
) {
    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("TextToImageFragment","TextToImageFragment",requireContext())
        setListeners()
    }

    private fun setListeners() {
            with(binding){
                launchGpt.setOnClickListener {
                   /* AdManager.loadAd(requireContext(), "ca-app-pub-3940256099942544/1033173712")
                    AdManager.showAd(requireActivity())*/
                    openCustomTab("https://labs.openai.com/")
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