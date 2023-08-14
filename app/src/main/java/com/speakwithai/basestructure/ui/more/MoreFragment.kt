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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.FragmentMoreBinding
import kotlin.math.log

class MoreFragment : BaseFragment<FragmentMoreBinding, MoreViewModel>(
    layoutId = R.layout.fragment_more,
    viewModelClass = MoreViewModel::class.java
) {
    override fun onInitDataBinding() {
        viewClicks()
    }

    private fun viewClicks() {
        with(binding) {
            selectLanguage.setOnClickListener { selectLanguage() }
            privacy.setOnClickListener { navigateToPrivacyPolicy() }
            shareApp.setOnClickListener { shareApp() }
            helpp.setOnClickListener { help() }
            linkToPlayStore.setOnClickListener { openPlayStore() }
            checkForUpdates()
        }
    }

    private fun FragmentMoreBinding.selectLanguage() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        if (activity?.packageManager?.let { it1 -> intent.resolveActivity(it1) } != null) {
            startActivity(intent)
        }
    }

    private fun navigateToPrivacyPolicy() {
        findNavController().navigate(R.id.action_moreFragment_to_privacyPolicyFragment)
    }

    private fun FragmentMoreBinding.shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareMessage = getString(R.string.share_app)
        val url = PLAY_STORE_URL
        shareIntent.putExtra(Intent.EXTRA_TEXT, "$shareMessage$url")
        val shareChooserTitle = getString(R.string.share_app_two)
        startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
    }

    private fun FragmentMoreBinding.help() {
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

    private fun FragmentMoreBinding.openPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(PLAY_STORE_URL)
        startActivity(intent)
    }

    private fun FragmentMoreBinding.checkForUpdates() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val latestVersion = firebaseRemoteConfig.getDouble("latest_version")
                    val appContext = context
                    if (appContext != null) {
                        val currentVersion = appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionName.toDouble()
                        if (currentVersion < latestVersion) {
                            updateVersin.visibility = View.VISIBLE
                            updateVersin.setOnClickListener {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse(PLAY_STORE_URL)
                                startActivity(intent)
                            }
                        }
                    } else {
                        return@addOnCompleteListener
                    }
                }
            }
    }

    companion object {
        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.speakwithai.basestructure"
    }
}
