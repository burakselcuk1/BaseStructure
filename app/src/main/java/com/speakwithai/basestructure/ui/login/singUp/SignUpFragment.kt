package com.speakwithai.basestructure.ui.login.singUp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.base.BaseFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.EmailFormatter
import com.speakwithai.basestructure.databinding.FragmentSignUpBinding
import com.speakwithai.basestructure.ui.login.navigation.LoginNavigation
import com.speakwithai.basestructure.ui.login.navigation.LoginNavigationImpl


class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpFragmentViewModel>(
    layoutId = R.layout.fragment_sign_up,
    viewModelClass = SignUpFragmentViewModel::class.java
) {
    val auth = Firebase.auth
    val navigator: LoginNavigation = LoginNavigationImpl()


    override fun onInitDataBinding() {
        navigator.bind(findNavController())
        setListeners()
        binding.apply {
            myProgressButton.cardView.setOnClickListener {
                val name = signupComponent.name.text.toString().trim()
                val email = signupComponent.email.text.toString().trim()
                val countryCode = signupComponent.ccpPhone.selectedCountryCode()
                val phoneNumber = signupComponent.phone.text
                val phone = "$countryCode$phoneNumber"


                val password = signupComponent.password.text.toString().trim()

                when {
                    name.isEmpty() -> Toast.makeText(context, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
                    phone.isEmpty() -> Toast.makeText(context, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
                    email.isEmpty() -> Toast.makeText(context, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
                    password.isEmpty() -> Toast.makeText(context, getString(R.string.fill_all), Toast.LENGTH_SHORT).show()
                    password.length < 6 -> Toast.makeText(context, getString(R.string.password_too_short), Toast.LENGTH_SHORT).show()
                    !EmailFormatter.isValidEmail(email) -> Toast.makeText(context, getString(R.string.invalid_mail), Toast.LENGTH_SHORT).show()
                    else -> {
                        myProgressButton.progressBar.visibility = View.VISIBLE
                        registerUser(email, password, name, phone)
                    }
                }
            }
        }
    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    uid?.let { uid ->
                        val userMap = hashMapOf(
                            "name" to name,
                            "phone" to phone
                        )
                        val database = FirebaseDatabase.getInstance().getReference("users")
                        database.child(uid).setValue(userMap)
                            .addOnSuccessListener {
                                binding.myProgressButton.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                                navigator.navigateToPickUpFragment()
                            }
                            .addOnFailureListener {
                                binding.myProgressButton.progressBar.visibility = View.GONE
                                Toast.makeText(requireContext(), getString(R.string.register_non_success), Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {

                }
            }
    }



}