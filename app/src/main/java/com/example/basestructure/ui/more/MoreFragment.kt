package com.example.basestructure.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentMoreBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MoreFragment : BaseFragment<FragmentMoreBinding,MoreViewModel>(
    layoutId = R.layout.fragment_more,
    viewModelClass = MoreViewModel::class.java
) {
    override fun onInitDataBinding() {
        viewClicks()
    }
    private fun viewClicks() {
        with(binding){
            selectLanguage.setOnClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                if (activity?.packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) {
                    startActivity(intent)
                }
            }
            privacy.setOnClickListener {
                findNavController().navigate(R.id.action_moreFragment_to_privacyPolicyFragment)
            }
            helpp.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("speakwithaitr@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, R.string.subject)
                    putExtra(Intent.EXTRA_TEXT, R.string.write_your_message_here)
                }
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(Intent.createChooser(intent, getString(R.string.send_with_email)))
                }
            }
            logOut.setOnClickListener {
                Toast.makeText(requireContext(),R.string.logut,Toast.LENGTH_SHORT).show()
                Firebase.auth.signOut()
                findNavController().navigate(R.id.action_moreFragment_to_mainFragment)
            }
        }
    }
}