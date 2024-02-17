package com.speakwithai.basestructure.premium

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.common.BillingManager
import com.speakwithai.basestructure.common.enums.UserStatus
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject

@AndroidEntryPoint
class PremiumFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_premium, container, false)
    }
    @Inject
    lateinit var billingManager: BillingManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.logScreenView("PremiumFragment","PremiumFragment",requireContext())

        val gifImageView = view.findViewById<GifImageView>(R.id.imageView5)
        val gifImageViewTwo = view.findViewById<GifImageView>(R.id.imageView7)
        val gifImageViewThree = view.findViewById<GifImageView>(R.id.imageView8)
        val premiumButton = view.findViewById<GifImageView>(R.id.gifImageView)
        val price = view.findViewById<TextView>(R.id.price)
        val gifDrawable = gifImageView.drawable as GifDrawable
        val gifDrawableTwo = gifImageViewTwo.drawable as GifDrawable
        val gifDrawableThree = gifImageViewThree.drawable as GifDrawable

        val handler = Handler()
        handler.postDelayed({
            if (gifDrawable.isRunning) {
                gifDrawable.stop()
                gifDrawableTwo.stop()
                gifDrawableThree.stop()
            }
        }, 3000)
        val handler1 = Handler()
        handler1.postDelayed({
            if (gifDrawable.isRunning) {
                price.visibility = View.VISIBLE
            }
        }, 1500)

        premiumButton.applyClickShrink()
        premiumButton.setOnClickListener {
            handleBillingProcess()
        }




    }


    private fun handleBillingProcess() {
        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                // Satın alma başarılı
                Toast.makeText(requireContext(), getString(R.string.premium_user), Toast.LENGTH_SHORT).show()
            } else {
                // Satın alma başarısız
            }
        }

        billingManager.setPurchasesUpdatedListener(purchasesUpdatedListener)

        billingManager.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryAndInitiatePurchase()
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })
    }

    private fun queryAndInitiatePurchase() {
        val skuList = listOf("premium")
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)

        billingManager.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                skuDetailsList.forEach { skuDetails ->
                    billingManager.initiatePurchase(requireActivity(),skuDetails)
                }
            }
        }
    }
}