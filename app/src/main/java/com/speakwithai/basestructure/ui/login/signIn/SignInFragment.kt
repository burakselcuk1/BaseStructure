package com.speakwithai.basestructure.ui.login.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>(
    layoutId = R.layout.fragment_sign_in,
    viewModelClass = SignInViewModel::class.java
) {
    val auth = FirebaseAuth.getInstance()

    override fun onInitDataBinding() {


        binding.createAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emaillLogin.text.toString()
            val password = binding.passswordLogin.text.toString()
            signInWithEmailAndPassword(email,password)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Giriş başarılı
                    val user = auth.currentUser
                    findNavController().navigate(R.id.action_signInFragment_to_moreFragment)
                } else {
                    // Giriş başarısız
                    val exception = task.exception
                    Toast.makeText(requireContext(),R.string.unsuccess, Toast.LENGTH_SHORT).show()
                }
            }
    }


}