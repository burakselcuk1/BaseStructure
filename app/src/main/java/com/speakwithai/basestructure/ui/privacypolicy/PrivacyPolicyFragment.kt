package com.speakwithai.basestructure.ui.privacypolicy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.speakwithai.basestructure.R


class PrivacyPolicyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: com.speakwithai.basestructure.ui.privacypolicy.PrivacyPolicyFragmentArgs by navArgs()
        val isim = args.isim

        Toast.makeText(requireContext(),isim,Toast.LENGTH_SHORT).show()
    }

}