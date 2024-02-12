package com.speakwithai.basestructure.ui.pickUp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AdManager.loadBannerAd
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.databinding.FragmentPickUpBinding
import com.speakwithai.basestructure.premium.PremiumFragment
import com.speakwithai.basestructure.ui.pickUp.navigation.PickUpNavigation
import com.speakwithai.basestructure.ui.pickUp.navigation.PickUpNavigationImple
import com.speakwithai.basestructure.ui.textToSpeech.TextToSpeechActivity


class PickUpFragment : BaseFragment<FragmentPickUpBinding, PickUpViewModel>(
    layoutId = R.layout.fragment_pick_up,
    viewModelClass = PickUpViewModel::class.java
) {
    val navigator: PickUpNavigation = PickUpNavigationImple()

    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("PickUpFragment","PickUpFragment",requireContext())
        navigator.bind(findNavController())
        showPremiumDialog()
        setListeners()
        setListenerBurgerMenu()
        updateLoginLogoutText()
        loadAdMod()
        fetchUserNameAndSurname()
        if (!areNotificationsEnabled()) {
            showNotificationPermissionDialog()
        }
    }

    private fun loadAdMod() {
        val adContainer = view?.findViewById<ViewGroup>(R.id.adView)
        if (adContainer != null) {
            loadBannerAd(requireContext(), adContainer, "ca-app-pub-3940256099942544/6300978111")
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
        return notificationManagerCompat.areNotificationsEnabled()
    }
    private fun showNotificationPermissionDialog() {
        val customDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_alert, null)
        val imageView = customDialogView.findViewById<ImageView>(R.id.cancel)
        val button = customDialogView.findViewById<Button>(R.id.goes_to_notification_setting_button)

        button.setOnClickListener {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setView(customDialogView)
            .create()

        imageView.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun showPremiumDialog() {
        val prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("isBottomSheetShown", false)) {
            Handler(Looper.getMainLooper()).postDelayed({
                val editor = prefs.edit()
                editor.putBoolean("isBottomSheetShown", true)
                editor.apply()

                val bottomSheet = PremiumFragment()
                bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
            }, 2000) // 2000 milisaniye = 2 saniye
        }
    }


    private fun fetchUserNameAndSurname() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = user.uid
            val databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)

                    val headerView = binding.navView.getHeaderView(0)
                    val userNameAndSurnameTextView = headerView.findViewById<TextView>(R.id.user_name_and_surname)

                    userNameAndSurnameTextView.text = name ?: getString(R.string.please_login)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun setListenerBurgerMenu() {
        val navigationView = requireView().findViewById<NavigationView>(R.id.navView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.instagram -> {
                    val url = "https://www.instagram.com/superaiapp/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)

                }
                R.id.setting -> {
                    navigator.navigateToSettingsFragment()
                }
                R.id.linkedin -> {
                    val url = "https://www.linkedin.com/company/101372439/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)                }
                R.id.mail -> {
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("buraksoftware1@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
                        putExtra(Intent.EXTRA_TEXT, getString(R.string.your_message))
                    }

                    val chooser = Intent.createChooser(emailIntent, getString(R.string.select_email_app))

                    if (emailIntent.resolveActivity(requireContext().packageManager) != null) {
                        startActivity(chooser)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.no_email_app), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val drawerLayout = requireView().findViewById<DrawerLayout>(R.id.drawerLayout)
            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
    }

    private fun setListeners() {
        with(binding){
            drawerMenuIcon.setOnClickListener {
                openCloseNavigationDrawer(it)
            }
            val headerView = binding.navView.getHeaderView(0)
            val userNameAndSurnameTextView = headerView.findViewById<TextView>(R.id.user_name_and_surname)
            userNameAndSurnameTextView.setOnClickListener {
                navigator.navigateToLoginFragment()
            }


            with(pickUpItems) {

                crypto.applyClickShrink()
                crypto.setOnClickListener {
                    navigator.navigateToCryptoFragment()
                    trackButtonClick("crypto")
                }

                googleBardButton.applyClickShrink()
                googleBardButton.setOnClickListener {
                   // navigator.navigateToGoogleBardFragment()
                    trackButtonClick("googleBardButton")
                }

                microsoftBing.applyClickShrink()
                microsoftBing.setOnClickListener {
                    //navigator.navigateToBingBardFragment()
                    trackButtonClick("microsoftBing")
                }

                imageStock.applyClickShrink()
                imageStock.setOnClickListener {
                    navigator.navigateToWallpaperFragment()
                    trackButtonClick("imageStock")

                }
                qrGenerator.applyClickShrink()
                qrGenerator.setOnClickListener {
                    navigator.navigateToQrGeneratorFragment()
                    trackButtonClick("qrCreateFragment")
                }
                qrReader.applyClickShrink()
                qrReader.setOnClickListener {
                    navigator.navigateToQrReaderFragment()
                    trackButtonClick("qrReaderFragment")

                }


                metaAi.applyClickShrink()
                metaAi.setOnClickListener{
                    navigator.navigateToMetaAiFragment()
                    trackButtonClick("metaAi")
                }

                musicStock.applyClickShrink()
                musicStock.setOnClickListener {
                    //navigator.navigateToMusicFragment()
                    trackButtonClick("musicStock")
                }
                textToSpeech.applyClickShrink()
                textToSpeech.setOnClickListener {
                    val intent = Intent(requireContext(), TextToSpeechActivity::class.java)
                    startActivity(intent)
                    trackButtonClick("textToSpeech")
                }
                textToImage.applyClickShrink()
                textToImage.setOnClickListener {
                    //navigator.navigateToTextToImageFragment()
                    trackButtonClick("textToImage")
                }
                gptButton.applyClickShrink()
                gptButton.setOnClickListener {
                    navigator.navigateToChatGptFragment()
                    trackButtonClick("gptButton")
                }
                cryptoNews.applyClickShrink()
                cryptoNews.setOnClickListener {
                    navigator.navigateToCrpytoNewsFragment()
                    trackButtonClick("cryptoNews")
                }
                premiumIcon.setOnClickListener {
                    val bottomSheet = PremiumFragment()
                    bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                    val drawerLayout = requireView().findViewById<DrawerLayout>(R.id.drawerLayout)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    trackButtonClick("premiumIcon")

                }
                loginFra.setOnClickListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        FirebaseAuth.getInstance().signOut()
                        navigator.navigateToLoginFragment()
                    } else {
                        navigator.navigateToLoginFragment()
                    }
                }

            }

        }
    }

    fun openCloseNavigationDrawer(view: View) {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun updateLoginLogoutText() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.loginFra.text = getString(R.string.log_out)
            binding.loginFra.setBackgroundResource(R.drawable.nav_header_background_logout)
        } else {
            binding.loginFra.text = getString(R.string.login)
        }
    }

    fun trackButtonClick(buttonName: String) {
        val analytics = FirebaseAnalytics.getInstance(requireContext())
        val params = Bundle()
        params.putString(buttonName, buttonName)

        analytics.logEvent("Button_Click", params)
    }
}