package com.speakwithai.basestructure.ui.cryptoNews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.databinding.FragmentCryptoNewsBinding
import com.speakwithai.basestructure.ui.cryptoNews.adapter.NewsRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CryptoNewsFragment : BaseFragment<FragmentCryptoNewsBinding, CryptoNewsViewModel>(
    layoutId = R.layout.fragment_crypto_news,
    viewModelClass = CryptoNewsViewModel::class.java
) {
    private lateinit var newsAdapter: NewsRVAdapter

    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("CryptoNewsFragment","CryptoNewsFragment",requireContext())
        viewModel.getCoinNews()
        initRecyclerView()
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initRecyclerView() {
        // Adapter'ı başlatın, ancak henüz veri seti yok
        newsAdapter = NewsRVAdapter(emptyList())
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        binding.rvNews.adapter = newsAdapter
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coinNewsResponse.collect { resource ->
                when(resource) {
                    is Resoource.Loading -> {
                        // Yükleme durumu
                    }
                    is Resoource.Success -> {
                        binding?.shimmerLayoutNews?.stopShimmer()
                        binding?.shimmerLayoutNews?.visibility = View.GONE
                        binding?.rvNews?.visibility = View.VISIBLE

                        // Yeni veriler geldiğinde yeni bir adapter oluşturun ve ayarlayın
                        resource.data?.data?.let { newData ->
                            newsAdapter = NewsRVAdapter(newData)
                            binding.rvNews.adapter = newsAdapter
                        }
                    }
                    is Resoource.Error -> {
                        // Hata durumu
                    }
                    else -> {}
                }
            }
        }
    }
}