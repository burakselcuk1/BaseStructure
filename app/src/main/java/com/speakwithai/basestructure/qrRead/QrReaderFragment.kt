package com.speakwithai.basestructure.qrRead


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.databinding.FragmentQrReaderBinding


class QrReaderFragment : BaseFragment<FragmentQrReaderBinding, QrReadViewModel>(
    layoutId = R.layout.fragment_qr_reader,
    viewModelClass = QrReadViewModel::class.java
) {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
    override fun onInitDataBinding() {
        binding.qrRead.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // İzin zaten verilmiş, QR kod okuyucuyu başlat
                startQrReader()
            } else {
                // İzin iste
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            floatingActionButtonGoogleBard.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // İzin verildi, QR kod okuyucuyu başlat
            startQrReader()
        } else {
            // İzin reddedildi, kullanıcıya bilgi ver
            Toast.makeText(context, getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startQrReader() {
        val intent = Intent(requireContext(), CaptureActivity::class.java)
        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(resultCode, data)
            if (result.contents != null) {
                showDialogWithContent(result.contents)
            }
        }else{
            Toast.makeText(requireContext(),getString(R.string.permission_required),Toast.LENGTH_SHORT).show()
        }
    }
    private fun showDialogWithContent(qrContent: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.qr_code_content))
        builder.setMessage(qrContent)
        builder.setPositiveButton(getString(R.string.okay)) { dialog, which ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(qrContent))
            startActivity(browserIntent)
        }
        val dialog = builder.create()
        dialog.show()
    }


}