package com.speakwithai.basestructure.ui.cryptoNews.newsDetail

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentNewsDetailBinding


class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>(
    layoutId = R.layout.fragment_news_detail,
    viewModelClass = NewsDetailViewModel::class.java
) {
    override fun onInitDataBinding() {
        val url = arguments?.getString("url")

        with(binding) {

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressBar.visibility = View.GONE
                }
            }

            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true

            if (url != null) {
                webView.loadUrl(url)
            }
            floatingActionButtonGoogleBard.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }
        }

        setListeners()

    }

    private fun setListeners() {
        with(binding){
            floatingActionButtonGoogleBard.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}