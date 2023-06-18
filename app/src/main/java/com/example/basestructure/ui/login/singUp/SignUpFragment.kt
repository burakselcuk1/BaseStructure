package com.example.basestructure.ui.login.singUp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentSignUpBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpFragmentViewModel>(
    layoutId = R.layout.fragment_sign_up,
    viewModelClass = SignUpFragmentViewModel::class.java
) {
    override fun onInitDataBinding() {

        binding.signUp.setOnClickListener {

            val email = binding.emaill.text.toString()
            val password = binding.passwordd.text.toString()
            signup(email.toString(), password.toString())
        }



    }

    private fun signup(email: String, password: String) {
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(requireContext(), "Email or Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(),user.toString(), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUpFragment_to_moreFragment)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }


}