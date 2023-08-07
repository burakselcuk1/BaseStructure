package com.speakwithai.basestructure.common

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.speakwithai.basestructure.common.enums.UserStatus
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class BillingManager @Inject constructor(private val context: Context) {

    private val defaultPurchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (!purchases.isNullOrEmpty()) {
                    _userStatus.value = UserStatus.PREMIUM
                } else {
                    _userStatus.value = UserStatus.NON_PREMIUM
                }
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                _userStatus.value = UserStatus.PREMIUM
            }
            else -> {
                _userStatus.value = UserStatus.NON_PREMIUM
            }
        }
    }

    var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(defaultPurchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    private val _purchases = MutableLiveData<List<Purchase>>()
    val purchases: LiveData<List<Purchase>> get() = _purchases

    private val _userStatus = MutableLiveData<UserStatus>().apply { value = UserStatus.UNKNOWN }
    val userStatus: LiveData<UserStatus> get() = _userStatus

    private val contextRef: WeakReference<Context> = WeakReference(context)

    fun setPurchasesUpdatedListener(listener: PurchasesUpdatedListener) {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
        billingClient = BillingClient.newBuilder(contextRef.get()!!)
            .setListener(listener)
            .enablePendingPurchases()
            .build()
    }

    fun startConnection(param: BillingClientStateListener) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {

                param.onBillingSetupFinished(billingResult)
            }
            override fun onBillingServiceDisconnected() {
                param.onBillingServiceDisconnected()
            }
        })
    }

    fun initiatePurchase(context: Context, skuDetails: SkuDetails) {
        if (context !is Activity || !billingClient.isReady) {
            return
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()

        try {
            val response = billingClient.launchBillingFlow(context, flowParams)
            if (response.responseCode != BillingClient.BillingResponseCode.OK) {
            }
        } catch (e: Exception) {
            // Exception'ı işlemek veya loglamak için gereken işlemler burada yapılabilir.
        }
    }

    fun querySkuDetailsAsync(params: SkuDetailsParams.Builder, responseListener: (BillingResult, List<SkuDetails>?) -> Unit) {
        billingClient.querySkuDetailsAsync(params.build(), responseListener)
    }

    fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (!purchaseList.isNullOrEmpty()) {
                    _purchases.value = purchaseList
                    _userStatus.value = UserStatus.PREMIUM
                } else {
                    _purchases.value = emptyList()
                    _userStatus.value = UserStatus.NON_PREMIUM
                }
            } else {
                _userStatus.value = UserStatus.UNKNOWN
            }
        }
    }
}
