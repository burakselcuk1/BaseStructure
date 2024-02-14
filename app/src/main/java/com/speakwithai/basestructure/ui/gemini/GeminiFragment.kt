package com.speakwithai.basestructure.ui.gemini


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.ai.client.generativeai.type.content
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.databinding.FragmentGeminiBinding
import com.speakwithai.basestructure.ui.gemini.adapter.ChatAdapter
import com.speakwithai.basestructure.ui.gemini.adapter.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class GeminiFragment : Fragment() {
    private var _binding: FragmentGeminiBinding? = null
    private val binding: FragmentGeminiBinding get() = _binding!!
    private val viewModel by viewModels<GeminiViewModel>()
    private var pickBitmap = mutableListOf<Bitmap?>()
    private var fullResponse: String = ""
    private val REQUEST_CODE_CAMERA = 100
    private lateinit var imageAdapter: ImageAdapter
    private val chatAdapter = ChatAdapter()
    private val messageList = mutableListOf<Pair<String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeminiBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnalyticsHelper.logScreenView("GeminiFragment","GeminiFragment",requireContext())
        setAdapter()
        sendPrompt()
        observe()
        binding.backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.progressBar.indeterminateDrawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            Color.WHITE, BlendModeCompat.SRC_IN)
        imageAdapter = ImageAdapter(onDeleteClickListener = { index ->
            if (pickBitmap.isNotEmpty() && index >= 0 && index < pickBitmap.size) {
                pickBitmap.removeAt(index)
                imageAdapter.notifyItemRemoved(index)
                imageAdapter.setImageList(pickBitmap)

            } else {
                // Hata durumu
            }
        })

        binding.imageRv.adapter = imageAdapter

        binding.imageBtn.setOnClickListener {
            pickPhoto()
        }
    }

    private fun setAdapter(){
        binding.chatRv.adapter = chatAdapter
    }
    private fun sendPrompt() {
        binding.chatSendd.setOnClickListener {
            val userMessage = binding.chatPromptTextEt.text.toString()

            if (userMessage.isNullOrEmpty()){
                Toast.makeText(requireContext(),getString(R.string.please_enter_prompt),Toast.LENGTH_SHORT).show()
            }else{
                if (pickBitmap.isEmpty()){
                    val userMessage = binding.chatPromptTextEt.text.toString()
                    viewModel.geminiChat(userMessage)
                    messageList.add(Pair(userMessage,ChatAdapter.VIEW_TYPE_USER))
                    chatAdapter.setMessages(messageList)
                    scrollPosition()
                    binding.chatPromptTextEt.setText("")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.chatSendd.visibility = View.GONE
                    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                }else{
                    messageList.add(Pair(userMessage,ChatAdapter.VIEW_TYPE_USER))
                    chatAdapter.setMessages(messageList)
                    scrollPosition()
                    CoroutineScope(Dispatchers.IO).launch {
                        val inputContent = content {

                            pickBitmap.forEach{
                                it?.let {
                                    image(compressBitmap(it))
                                }
                            }
                            text(userMessage)
                        }
                        viewModel.geminiPromptResponse(inputContent)
                    }
                    binding.chatPromptTextEt.setText("")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.chatSendd.visibility = View.GONE
                    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                }
            }
        }
    }

    private fun scrollPosition(){
        binding.chatRv.smoothScrollToPosition(chatAdapter.itemCount - 1)

    }

    private fun pickPhoto() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.pick_image))
            .setMessage(getString(R.string.camera_or_gallery))
            .setPositiveButton(getString(R.string.camera)) { dialog, which ->
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestCameraPermission()

                }
                pickCameraIntent()
            }
            .setNegativeButton(getString(R.string.gallery)) { dialog, which ->
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestStoragePermission()
                }
                galleryLauncher.launch("image/*")
            }
            .create()
        dialog.show()
    }

    private fun requestCameraPermission() {
        cameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun requestStoragePermission() {
        galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private fun pickCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                pickBitmap.add(bitmap)
                imageAdapter.setImageList(pickBitmap)
            }
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return BitmapFactory.decodeByteArray(
            byteArrayOutputStream.toByteArray(),
            0,
            byteArrayOutputStream.size()
        )
    }

    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner){content ->
            content?.text?.let {
                binding.progressBar.visibility = View.GONE
                binding.chatSendd.visibility = View.VISIBLE
                messageList.add(Pair(it,ChatAdapter.VIEW_TYPE_GEMINI))
                chatAdapter.setMessages(messageList)
                scrollPosition()
            }
        }
        viewModel.promptResponse.observe(viewLifecycleOwner){content ->
            content?.text?.let {
                binding.progressBar.visibility = View.GONE
                binding.chatSendd.visibility = View.VISIBLE
                messageList.add(Pair(it,ChatAdapter.VIEW_TYPE_GEMINI))
                chatAdapter.setMessages(messageList)
                scrollPosition()
            }
        }
    }

    private fun galleryIntent() {
        startActivity(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }

    @Suppress("DEPRECATION")
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            }.also { bitmap ->
                pickBitmap.add(bitmap)
                imageAdapter.setImageList(pickBitmap)
            }
        }
    }

    private val galleryPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryIntent()
        }
    }

    private val cameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickCameraIntent()
        }
    }

}