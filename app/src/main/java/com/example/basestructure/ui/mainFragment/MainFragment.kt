package com.example.basestructure.ui.mainFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.databinding.FragmentMainBinding
import com.example.basestructure.ui.chat.ChatFragment


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(
    layoutId = R.layout.fragment_main,
    viewModelClass = MainFragmentViewModel::class.java
) {
    override fun onInitDataBinding() {

        binding.editText.setOnClickListener { view ->
            // Klavyenin açılmasını önlemek için focus'ı kaldır
            view.clearFocus()

            // Eğer klavye açıksa, kapat
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)

            // Navigasyon işlemini gerçekleştir
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment2)
        }



    }


}