package com.speakwithai.basestructure.common

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    private val _userStatus = MutableLiveData<UserStatus>()
    val userStatus: MutableLiveData<UserStatus>  = _userStatus

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
        Log.d("bozo","Bağlantı başlatılıyor...")

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("bozo","Bağlantı başarıyla kuruldu.")
                } else {
                    Log.d("bozo","Bağlantı başarısız. Hata kodu: ${billingResult.responseCode}, Debug mesajı: ${billingResult.debugMessage}")
                }

                param.onBillingSetupFinished(billingResult)
            }

            override fun onBillingServiceDisconnected() {
                Log.d("bozo","Bağlantı kesildi. Bağlantıyı yeniden başlatılıyor...")

                // Ekstra detaylar sağlamak için aşağıdaki gibi log mesajları ekleyebilirsiniz.
                Log.d("bozo","Şu anda bağlantı durumu: ${billingClient.connectionState}")
                Log.d("bozo","Şu anda hazır mı? ${billingClient.isReady}")

                // Diğer istediğiniz detaylar burada olabilir.

                // Bağlantıyı yeniden başlatma öncesi bekleme
                Handler(Looper.getMainLooper()).postDelayed({
                    startConnection(this)
                }, 1000) // Örnek olarak 1 saniye
            }


            /*   override fun onBillingServiceDisconnected() {
                   Timber.e("Bağlantı kesildi. Bağlantıyı yeniden başlatılıyor...")
                   // Bağlantıyı yeniden başlat
                   param.onBillingServiceDisconnected()
                   startConnection(this)
               }*/
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
                    _userStatus.postValue(UserStatus.PREMIUM)
                    Log.d("bozo","KULLANICI PREMIUM")

                } else {
                    _userStatus.postValue(UserStatus.NON_PREMIUM)
                    _purchases.value = emptyList()
                    Log.d("bozo","KULLANICI PREMIUM DEĞİL")

                }
            } else {
                _userStatus.postValue(UserStatus.UNKNOWN)
                Log.d("bozo","KULLANICI UNKNOWN.")

            }
        }
    }
}
