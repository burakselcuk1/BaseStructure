package com.speakwithai.basestructure.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.speakwithai.basestructure.R
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    layoutId = R.layout.fragment_profile,
    viewModelClass = ProfileViewModel::class.java
) {
    override fun onInitDataBinding() {
        setListeners()
        fetchUserNameAndSurname()
    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            pleaseLogin.applyClickShrink()
            pleaseLogin.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
            }
        }
    }

    private fun fetchUserNameAndSurname() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val email = user.email
            binding.emailText.text = email ?: getString(R.string.please_login)

            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val phone = snapshot.child("phone").getValue(String::class.java)
                    binding.nameText.text = name ?: getString(R.string.please_login)
                    binding.phoneText.text = phone ?: getString(R.string.please_login)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding.pleaseLogin.visibility = View.VISIBLE
            binding.userInformation.visibility = View.GONE
        }
    }
}