package com.example.basestructure.ui.mainFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

// Edittext'e tıklanınca diğer fragment'a gidilir
        binding.editText.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment2)
        }

// Ikon'a tıklandığında başka bir işlem yapılır (örneğin, bir Toast mesajı gösterilir)
        binding.textInputLayout.setEndIconOnClickListener {
            Toast.makeText(context, "Icon clicked!", Toast.LENGTH_SHORT).show()
        }




    }


}