package com.example.basestructure.ui.splah

import android.animation.Animator
import android.content.Intent
import com.airbnb.lottie.LottieAnimationView
import com.example.basestructure.R
import com.example.basestructure.base.BaseActivity
import com.example.basestructure.databinding.ActivitySplashBinding
import com.example.basestructure.ui.mainActivity.MainActivity


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(
    viewModelClass = SplashViewModel::class.java,
    layoutId = com.example.basestructure.R.layout.activity_splash
) {


    override fun onInitDataBinding() {
        supportActionBar?.hide()

        binding.animationView.setAnimation("progress/lol.json")
        binding.animationView.setImageAssetsFolder("progress/")
        binding.animationView.playAnimation()

        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                // Animasyon bittiğinde MainActivity'ye geçiş yapılıyor
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}