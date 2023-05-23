package com.example.basestructure.ui.mainFragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.databinding.FragmentMainBinding
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(
    layoutId = R.layout.fragment_main,
    viewModelClass = MainFragmentViewModel::class.java
) {
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_GALLERY_IMAGE = 2
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    override fun onInitDataBinding() {
        binding.editText.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment2)
        }

        binding.textInputLayout.setEndIconOnClickListener {
            val options = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your picture")

            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Camera" -> {
                        askCameraPermissionAndOpenCamera()
                    }
                    options[item] == "Gallery" -> {
                        openGallery()
                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE)
    }

    private fun askCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    // İzin reddedildi. Burada uygun eylemi gerçekleştirebilirsiniz.
                }
                return
            }
            else -> {
                // Diğer izin isteklerini işleyin
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                   recognizeTextFromImage(imageBitmap)
            } else if (requestCode == REQUEST_GALLERY_IMAGE) {
                val selectedImage = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                  recognizeTextFromImage(bitmap)
            }
        }
    }

    private fun recognizeTextFromImage(imageBitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(imageBitmap)
        val recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer()

        recognizer.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                val text = firebaseVisionText.text
                Log.d("MLKit", "Recognized text: $text")
                Toast.makeText(requireContext(),text.toString(),Toast.LENGTH_SHORT).show()

                // text is the recognized text from image
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

}
