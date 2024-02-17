package com.speakwithai.basestructure.ui.crypto.cryptoHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.databinding.FragmentCryptoHomePageBinding
import com.speakwithai.basestructure.ui.crypto.adapter.CoinsAdapter
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CryptoHomePageFragment : BaseFragment<FragmentCryptoHomePageBinding, CryptoHomePageViewModel>(
    layoutId = R.layout.fragment_crypto_home_page,
    viewModelClass = CryptoHomePageViewModel::class.java
) {
    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("CryptoHomePageFragment","CryptoHomePageFragment",requireContext())

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.indigo_500));
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchCoins()
            setupObservers()
        }
        this.displayLoading()

        this.setupObservers()
        setListeners()
    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.coinToDisplayFlow.collect{result->
                when (result) {
                    is Resoource.Loading ->{
                    }
                    is Resoource.Success -> {
                        binding.rvCoinsList.adapter = CoinsAdapter(result.data) { coin ->
                            onItemClickListener(coin)

                        }
                        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
                    }
                    is Resoource.Error -> {
                    }

                }
            }
        }
    }

    private fun displayCoinsList() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
        binding.llSubHeader.visibility = View.VISIBLE
    }

    private fun displayLoading() {
        binding.txtFavouriteCoins.visibility = View.GONE
        binding.llSubHeader.visibility = View.GONE
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.cpiFavouriteCoinLoadingIndicator.visibility = View.GONE
    }

    private fun displayFavouriteCoins() {
        binding.txtFavouriteCoins.visibility = View.VISIBLE
        binding.rvFavouriteCoins.visibility = View.VISIBLE
    }

    private fun hideFavouriteCoins() {
        binding.txtFavouriteCoins.visibility = View.GONE
        binding.rvFavouriteCoins.visibility = View.GONE
    }

    private fun onItemClickListener(coinUiModel: CoinUiModel) {
        val bundle = bundleOf("coin" to coinUiModel)
        findNavController().navigate(R.id.action_cryptoHomePageFragment_to_cryptoDetailFragment, bundle)

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}