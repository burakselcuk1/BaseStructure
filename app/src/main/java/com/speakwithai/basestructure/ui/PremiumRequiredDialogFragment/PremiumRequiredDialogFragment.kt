package com.speakwithai.basestructure.ui.PremiumRequiredDialogFragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.BillingManager
import com.speakwithai.basestructure.databinding.DialogPremiumRequiredBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint

class PremiumRequiredDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var billingManager: BillingManager

    private var _binding: DialogPremiumRequiredBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogPremiumRequiredBinding.inflate(inflater, container, false)
        setCoordinatorLayoutStyle(container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheetBackground()
        setupClickListeners()
    }

    private fun setCoordinatorLayoutStyle(container: ViewGroup?) {
        val coordinatorLayout = container?.parent as? CoordinatorLayout
        coordinatorLayout?.setBackgroundResource(R.drawable.rounded_corner_left)
    }

    private fun setupBottomSheetBackground() {
        val bottomSheet = (requireView().parent as View)
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setupClickListeners() {
        with(binding) {
            premiumCancelButton.setOnClickListener { dismiss() }

            constraintLayout3.setOnClickListener {
                setSelectedStyle(constraintLayout3, bozo)
            }

            bozo.setOnClickListener {
                setSelectedStyle(bozo, constraintLayout3)
            }

            continuee.setOnClickListener { handleBillingProcess() }
        }
    }


    private fun setSelectedStyle(selected: View, unselected: View) {
        selected.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_green)
        unselected.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
    }

    private fun handleBillingProcess() {
        val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                // Satın alma başarılı
                for (purchase in purchases) {
                    // Satın alınan ürünleri burada işleyebilirsiniz
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
