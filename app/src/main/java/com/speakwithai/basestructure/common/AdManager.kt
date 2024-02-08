package com.speakwithai.basestructure.common

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

object AdManager {
    private var mInterstitialAd: InterstitialAd? = null

    fun loadAd(context: Context, adUnitId: String) {
        val adRequest = AdManagerAdRequest.Builder().build()

        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                mInterstitialAd = null
            }
        })
    }

    fun showAd(activity: Activity) {
        mInterstitialAd?.show(activity)
    }

    fun loadBannerAd(context: Context, adContainer: ViewGroup, adUnitId: String) {
        val adView = AdView(context)

        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adUnitId

        adContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
