package com.speakwithai.basestructure.ui.mainActivity


import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.speakwithai.basestructure.base.BaseActivity
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.CheckDayWorker
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.common.utils.MessageManager
import com.speakwithai.basestructure.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    layoutId = R.layout.activity_main, viewModelClass = MainViewModel::class.java
) {

    override fun onInitDataBinding() {

        val periodicWorkRequest = PeriodicWorkRequestBuilder<CheckDayWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)

        MessageManager.initialize(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.chatFragment2 -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

 /*       viewModel.userStatus.observe(this, Observer { status ->
            when(status) {
                UserStatus.PREMIUM -> {
                    Toast.makeText(this,"Premium", Toast.LENGTH_SHORT).show()

                }
                UserStatus.NON_PREMIUM -> {
                    Toast.makeText(this,"Değil", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this,"Belirsiz", Toast.LENGTH_SHORT).show()

                }
            }
        })*/
    }
}
