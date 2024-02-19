package com.speakwithai.basestructure.ui.qrCreator


import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AdManager
import com.speakwithai.basestructure.common.BillingManager
import com.speakwithai.basestructure.common.enums.UserStatus
import com.speakwithai.basestructure.databinding.FragmentQrCreaterBinding
import dagger.hilt.android.AndroidEntryPoint
import qrcode.QRCode
import java.io.OutputStream
import javax.inject.Inject

@AndroidEntryPoint
class QrCreaterFragment : BaseFragment<FragmentQrCreaterBinding, QrCreaterViewModel>(
    layoutId = R.layout.fragment_qr_creater,
    viewModelClass = QrCreaterViewModel::class.java
) {
    @Inject
    lateinit var billingManager: BillingManager
    override fun onInitDataBinding() {

        setListeners()

    }

    private fun setListeners() {
        with(binding){
            download.setOnClickListener {
                onDownloadButtonClicked(requireContext(),binding.image)
                Toast.makeText(requireContext(), getString(R.string.download_started), Toast.LENGTH_SHORT).show()

                billingManager.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            billingManager.queryPurchases() // Satın almaları sorgula
                        } else {
                            // Billing bağlantısı kurulamadı, gerekli hata işlemleri buraya gelebilir
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        // Billing servisi bağlantısı kesildi, gerektiğinde yeniden bağlanma işlemleri buraya gelebilir
                    }
                })

                billingManager.userStatus.observe(viewLifecycleOwner) { userStatus ->
                    when (userStatus) {
                        UserStatus.PREMIUM -> {

                        }

                        UserStatus.NON_PREMIUM -> {
                            AdManager.loadAd(requireContext(), "ca-app-pub-3940256099942544/1033173712")
                            AdManager.showAd(requireActivity())
                        }

                        UserStatus.UNKNOWN -> {
                            AdManager.loadAd(requireContext(), "ca-app-pub-3940256099942544/1033173712")
                            AdManager.showAd(requireActivity())

                        }else->{
                        AdManager.loadAd(requireContext(), "ca-app-pub-3940256099942544/1033173712")
                        AdManager.showAd(requireActivity())
                    }
                    }
                }
            }
            create.setOnClickListener {
                if (userMessage.text.toString().isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_enter_url), Toast.LENGTH_SHORT).show()
                } else {
                    val qrBitmap = generateQRCodeBitmap(userMessage.text.toString())
                    image.setImageBitmap(qrBitmap)
                    download.visibility = View.VISIBLE
                }
            }

            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            floatingActionButtonGoogleBard.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    fun generateQRCodeBitmap(text: String): Bitmap {
        val qrCode = QRCode(text)

        val qrGraphics = qrCode.render()
        return qrGraphics.nativeImage() as Bitmap
    }

    fun saveImageToGallery(context: Context, bitmap: Bitmap, filename: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SuperAiApp")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
        }
    }

    fun onDownloadButtonClicked(context: Context, imageView: ImageView) {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        saveImageToGallery(context, bitmap, "qrcode.png")
    }
}