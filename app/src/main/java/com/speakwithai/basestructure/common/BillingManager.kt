package com.speakwithai.basestructure.common

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingManager(private val context: Context, private val listener: PurchasesUpdatedListener) {

    var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(listener)
        .enablePendingPurchases()
        .build()


    fun startConnection(param: BillingClientStateListener) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d("BillingManager", "onBillingSetupFinished: ${billingResult.responseCode}")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("BillingManager", "Bağlantı başarılı!")
                    // Uygulama içi faturalandırma API'leri burada kullanılabilir
                } else {
                    Log.d("BillingManager", "Bağlantı başarısız! Hata kodu: ${billingResult.responseCode}")
                }
                // Dışarıdan gelen listener'ın metodu çağrılıyor
                param.onBillingSetupFinished(billingResult)
            }

            override fun onBillingServiceDisconnected() {
                Log.d("BillingManager", "onBillingServiceDisconnected: Bağlantı kesildi")
                // Bağlantı kesildiğinde ne yapılması gerektiğini burada tanımlayabilirsiniz
                // Dışarıdan gelen listener'ın metodu çağrılıyor
                param.onBillingServiceDisconnected()
            }
        })
    }


    fun initiatePurchase(skuDetails: SkuDetails) {
        Log.d("BillingManager", "Satın alma işlemi başlatılıyor: ${skuDetails.sku}")
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val response = billingClient.launchBillingFlow(context as Activity, flowParams)
        Log.d("BillingManager", "launchBillingFlow sonucu: ${response.responseCode}")
    }

    fun querySkuDetailsAsync(params: SkuDetailsParams.Builder, responseListener: (BillingResult, List<SkuDetails>?) -> Unit) {
        Log.d("BillingManager", "SKU detayları sorgulanıyor")
        billingClient.querySkuDetailsAsync(params.build(), responseListener)
    }
}
