package com.speakwithai.basestructure.premium

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
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
import android.animation.Animator
import com.speakwithai.basestructure.databinding.FragmentPremiumBinding

@AndroidEntryPoint
class PremiumFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPremiumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPremiumBinding.inflate(inflater, container, false)
        return binding.root    }
    @Inject
    lateinit var billingManager: BillingManager
    val premiumButton = view?.findViewById<GifImageView>(R.id.gifImageView)
    var premiumItemsLayout = view?.findViewById<View>(R.id.premium_items)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gifImageView = view.findViewById<GifImageView>(R.id.imageView5)
        val gifImageViewTwo = view.findViewById<GifImageView>(R.id.imageView7)
        val gifImageViewThree = view.findViewById<GifImageView>(R.id.imageView8)

        val gifDrawable = gifImageView.drawable as GifDrawable
        val gifDrawableTwo = gifImageViewTwo.drawable as GifDrawable
        val gifDrawableThree = gifImageViewThree.drawable as GifDrawable


        AnalyticsHelper.logScreenView("PremiumFragment","PremiumFragment",requireContext())

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
                binding.price.visibility = View.VISIBLE
            }
        }, 1500)

        binding.gifImageView.applyClickShrink()
        binding.gifImageView.setOnClickListener {
            handleBillingProcess()

        }
    }

    private fun handleBillingProcess() {
        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                // Satın alma başarılı
                with(binding){
                    imageView5.visibility = View.GONE
                    imageView7.visibility = View.GONE
                    imageView8.visibility = View.GONE
                    price.visibility = View.GONE
                    binding.gifImageView.visibility = View.GONE
                    premiumText.visibility = View.GONE
                    premiumButton?.visibility = View.GONE
                    premiumItems.linear1.visibility = View.GONE
                    premiumItems.linear2.visibility = View.GONE
                }
                with(binding){
                    animationView.setAnimation("premiumanimation.json")
                    animationView.playAnimation()
                    animationView.addAnimatorListener(object : Animator.AnimatorListener {

                        override fun onAnimationStart(p0: Animator) {
                            premiumItemsLayout?.visibility = View.GONE

                        }

                        override fun onAnimationEnd(p0: Animator) {
                            animationView.visibility = View.GONE
                            price.visibility = View.GONE

                        }

                        override fun onAnimationCancel(p0: Animator) {
                        }

                        override fun onAnimationRepeat(p0: Animator) {
                        }
                    })
                }
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