package com.speakwithai.basestructure.ui.login.singUp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.base.BaseFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.FragmentSignUpBinding


class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpFragmentViewModel>(
    layoutId = R.layout.fragment_sign_up,
    viewModelClass = SignUpFragmentViewModel::class.java
) {
    val auth = Firebase.auth
    val firestore = FirebaseFirestore.getInstance()

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
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(),user.toString(), Toast.LENGTH_SHORT).show()
                    val uid = user?.uid
                    if (uid != null) {
                        createUserDocument(uid)
                    }
                    findNavController().navigate(R.id.action_signUpFragment_to_moreFragment)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    fun createUserDocument(uid: String) {
        val userRef = firestore.collection("users").document(uid)

        val data = hashMapOf(
            "isPremium" to false
            // İstediğiniz diğer alanları da buraya ekleyebilirsiniz
        )

        userRef.set(data)
            .addOnSuccessListener {
                // Belge oluşturma başarılı
                Log.d("Firestore", "Belge oluşturma başarılı")
            }
            .addOnFailureListener { e ->
                // Belge oluşturma başarısız
                Log.e("Firestore", "Belge oluşturma başarısız", e)
            }

    }



}