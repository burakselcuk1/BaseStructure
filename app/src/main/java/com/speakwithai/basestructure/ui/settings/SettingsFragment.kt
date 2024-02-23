package com.speakwithai.basestructure.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.databinding.FragmentSettingsBinding
import android.provider.Settings
import com.speakwithai.basestructure.BuildConfig
import com.speakwithai.basestructure.ui.settings.navigation.SettingsNavigation
import com.speakwithai.basestructure.ui.settings.navigation.SettingsNavigationImpl


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(
    layoutId = R.layout.fragment_settings,
    viewModelClass = SettingsViewModel::class.java
) {
    val navigator: SettingsNavigation = SettingsNavigationImpl()

    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("SettingsFragment","SettingsFragment",requireContext())
        navigator.bind(findNavController())
        setListenersr()
    }

    private fun setListenersr() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            backArrow.applyClickShrink()
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            val versionName = "v${BuildConfig.VERSION_NAME}"
            version.text = versionName.toString()

            imageViewProfile.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            changeLanguage.setOnClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
            notificationSetting.setOnClickListener {
                val intent = Intent().apply {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                        }
                        else -> {
                            action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            putExtra("app_package", requireContext().packageName)
                            putExtra("app_uid", requireContext().applicationInfo.uid)
                        }
                    }
                }
                startActivity(intent)
            }
            profile.setOnClickListener {
                navigator.navigateToProfileFragment()
            }
            powerSetting.setOnClickListener {
                val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                startActivity(intent)
            }
            batteryOptimise.setOnClickListener {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }
            appSetting.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
    }
}