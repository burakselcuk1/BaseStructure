package com.speakwithai.basestructure.ui.more

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
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.ui.more.navigation.MoreFragmentNavigatiion
import com.speakwithai.basestructure.ui.more.navigation.MoreFragmentNavigatiionImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.FragmentMoreBinding
import kotlin.math.log

class MoreFragment : BaseFragment<FragmentMoreBinding,MoreViewModel>(
    layoutId = R.layout.fragment_more,
    viewModelClass = MoreViewModel::class.java
) {
    private val navigator : MoreFragmentNavigatiion = MoreFragmentNavigatiionImpl()
    override fun onInitDataBinding() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  navigator.bind(findNavController())  // burada navController'a bir değer atanıyor
        viewClicks()

    }
    private fun viewClicks() {
        val user = Firebase.auth.currentUser

        with(binding){
            selectLanguage.setOnClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                if (activity?.packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) {
                    startActivity(intent)
                }
            }
            privacy.setOnClickListener {
              //  findNavController().navigate(R.id.action_moreFragment_to_privacyPolicyFragment)
                val args ="lale"

                navigator.navigateToPrivacy(args)
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
            /*login.setOnClickListener {
                findNavController().navigate(R.id.action_moreFragment_to_signInFragment)
            }*/
            logOut.setOnClickListener {
                Toast.makeText(requireContext(),R.string.logut,Toast.LENGTH_SHORT).show()
                Firebase.auth.signOut()
                findNavController().navigate(R.id.action_moreFragment_to_mainFragment)
            }
            email.text = user?.email ?: "No user logged in"

            val user = Firebase.auth.currentUser
            if (user != null) {
                // Kullanıcı giriş yapmış, girişe özel özellikleri göster
                constraintLayout2.visibility = View.VISIBLE
                logOut.visibility = View.VISIBLE
                //login.visibility = View.GONE
            } else {
                // Kullanıcı giriş yapmamış, girişe özel özellikleri gizle
            }
        }
    }
}