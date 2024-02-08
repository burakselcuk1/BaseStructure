package com.speakwithai.basestructure.ui.login.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.EmailFormatter
import com.speakwithai.basestructure.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>(
    layoutId = R.layout.fragment_sign_in,
    viewModelClass = SignInViewModel::class.java
) {
    val auth = FirebaseAuth.getInstance()

    override fun onInitDataBinding() {
    setListeners()

    }
    private fun setListeners() {
        with(binding) {
            login.textView1.text = getString(R.string.login)
            createAccount.setOnClickListener {
               // navigator.navigateToRegisterFragment()
            }
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            /*     googleLogin.setOnClickListener {
                     val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignInClient)
                     startActivityForResult(signInIntent, RC_SIGN_IN)
                 }*/

            login.cardView.setOnClickListener {
                val email = textInputLayout3.editText?.text.toString().trim()
                val password = textInputLayout4.editText?.text.toString().trim()

                when {
                    email.isEmpty() || password.isEmpty() ->
                        Toast.makeText(context, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
                    !EmailFormatter.isValidEmail(email) ->
                        Toast.makeText(context, getString(R.string.invalid_mail), Toast.LENGTH_SHORT).show()
                    else ->{
                        login.progressBar.visibility = View.VISIBLE
                        signInWithEmail(email, password)
                    }
                }
            }
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.login.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    //navigator.navigateToPickUpFragment()
                } else {
                    binding.login.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.login_non_success), Toast.LENGTH_SHORT).show()
                }
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