package com.example.basestructure.ui.login.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>(
    layoutId = R.layout.fragment_sign_in,
    viewModelClass = SignInViewModel::class.java
) {
    override fun onInitDataBinding() {
        binding.createAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

}