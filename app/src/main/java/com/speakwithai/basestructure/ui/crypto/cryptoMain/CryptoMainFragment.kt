package com.speakwithai.basestructure.ui.crypto.cryptoMain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentCryptoMainBinding


class CryptoMainFragment : BaseFragment<FragmentCryptoMainBinding, CryptoMainViewModel>(
    layoutId = R.layout.fragment_crypto_main,
    viewModelClass = CryptoMainViewModel::class.java
) {

    override fun onInitDataBinding() {
        val navView: BottomNavigationView = binding.bottomNavigationView2

        // Belirli bir NavHostFragment'tan NavController almak için
        val navHostFragment = childFragmentManager.findFragmentById(R.id.cryptoFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        setListeners()
    }

    private fun setListeners() {
        with(binding){
            floatingActionButton.setOnClickListener{
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()

            }
        }
    }
}