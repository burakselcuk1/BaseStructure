package com.speakwithai.basestructure.ui.mainFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.databinding.FragmentMainBinding
import com.speakwithai.basestructure.ui.mainFragment.navigation.MainNavigation
import com.speakwithai.basestructure.ui.mainFragment.navigation.MainNavigationImpl


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(
    layoutId = R.layout.fragment_main,
    viewModelClass = MainFragmentViewModel::class.java
) {
    val navigator: MainNavigation = MainNavigationImpl()

    override fun onInitDataBinding() {
        navigator.bind(findNavController())
        setListeners()

    }
    private fun setListeners() {
        with(binding) {
            getStartedBtn.setOnClickListener {
                getStartedBtn.setBackgroundResource(R.drawable.button_degrade_background_click)
                navigator.navigateToPickUpFragment()
            }
        }
    }
}
